/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.sonarttcn3.languages.Ttcn3Language;
import org.eclipse.sonarttcn3.rules.TitanRulesDefinition;
import org.eclipse.titan.lsp.commandline.CommandLineConfiguration;
import org.eclipse.titan.lsp.commandline.CommandLineExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.Rule;
import org.sonar.api.scanner.sensor.ProjectSensor;

public class Ttcn3Sensor implements ProjectSensor {
	private static final String REPORT_PATH_KEY = "sonar.ttcn3.reportPaths";
	private static final String ENABLE_OOP = "sonar.ttcn3.enableOOP";
	private static final String ENABLE_REALTIME = "sonar.ttcn3.enableRealtime";

	private static final String DEFAULT_REPORT_PATH = ".titan_compile"; 
	private static final String REPORT_REGEX_DEF = "sonar.titan.regex";
	private static final String DEFAULT_REGEX_DEF = "(?<file>[^:]+):::(?<line>[0-9]+):::(?<message>.+):::(?<rulekey>.+)";
	private static final String TTCN3 = Ttcn3Language.KEY;
	
	private static final String MATCH_GROUP_FILE = "file";
	private static final String MATCH_GROUP_LINE = "line";
	private static final String MATCH_GROUP_RULEKEY = "rulekey";
	private static final String MATCH_GROUP_MESSAGE = "message";

	private static final Logger LOG = LoggerFactory.getLogger(Ttcn3Sensor.class);
	
	protected SensorContext context;
	private final Checks<Rule> checks;
	private final ActiveRules activeRules;

	private Pattern pattern;
	private FileSystem fs;
	private FilePredicates p;
	
	public Ttcn3Sensor(CheckFactory checkFactory, ActiveRules activeRules) {
		this.activeRules = activeRules;	
		checks = checkFactory.create(TTCN3);
		checks.addAnnotatedChecks(TitanRulesDefinition.getRules());
	}
	
	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor
			.name(getClass().getName());
	}

	@Override
	public void execute(SensorContext context) {
		this.context = context;
		LOG.info("Eclipse Titan TTCN3 Sonarqube plugin");
		
		processReport();
	}
	
	private String getRegex() {
	    return context.config().get(REPORT_REGEX_DEF).orElse(DEFAULT_REGEX_DEF);
	}
	
	// TODO :: support for multiple paths
	private String getReportPath() {
	    return context.config().get(REPORT_PATH_KEY).orElse(DEFAULT_REPORT_PATH);
	}

	private boolean checkMandatoryMatchingGroups(final String regex) {
		final String[] groups = { 
			MATCH_GROUP_FILE,
			MATCH_GROUP_LINE,
			MATCH_GROUP_MESSAGE,
			MATCH_GROUP_RULEKEY
		};

		boolean isMissing = false;
		for (final String group : groups) {
			if (!regex.contains("(?<" + group + ">")) {
				LOG.error("Mandatory named capture group `{}` is missing from regex", group);
				isMissing = true;
			}
		}

		return isMissing;
	}

	protected void processReport() {
	    String reportRegex = getRegex();
	    LOG.debug("Using regex {}", reportRegex);

	    pattern = null;
	    try {
	    	pattern = Pattern.compile(reportRegex);
	    } catch (PatternSyntaxException e) {
	    	LOG.error("Failed to compile report regex.");
	    	return;
	    }
		if (checkMandatoryMatchingGroups(reportRegex)) {
			return;
		}

		final String reportPath = getReportPath();
	    LOG.info("Report path: {}", reportPath);

		fs = context.fileSystem();
	    p = fs.predicates();
	    final Path baseDir = context.fileSystem().baseDir().toPath();
		final Path report = baseDir.resolve(reportPath);

		if (!Files.exists(report)) {
			LOG.info("Report file `{}` not found, executing compiler", report);
			final CommandLineConfiguration config = new CommandLineConfiguration();
			config.rootFolder = fs.baseDir().getAbsolutePath();
			config.saFormat = "%f:::%l:::%m:::%k";
			config.suppressStdout = true;
			config.ttcnErrorMarkers = false;
			config.ttcnWarningMarkers = false;
			config.oopEnabled = context.config().getBoolean(ENABLE_OOP).orElse(false);
			config.realtimeEnabled = context.config().getBoolean(ENABLE_REALTIME).orElse(false);
			final CommandLineExecutor executor = new CommandLineExecutor(config);
			String analyzerOutput = "";
			try {
				analyzerOutput = executor.runCommandLineAnalyzer();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try (final BufferedReader br = new BufferedReader(new StringReader(analyzerOutput))) {
				processReader(br);
			}
	        catch (IOException e) {
	            e.printStackTrace();
	        }

			return;
		}

		try (final BufferedReader br = Files.newBufferedReader(report)) {
			processReader(br);
		}
		catch (IOException e) {
			LOG.error("Error while reading compilation file.");
		}
	}

	private void processReader(final BufferedReader reader) throws IOException {
		String str;
		while ((str = reader.readLine()) != null) {
			processLine(str);
		}
	}

	private void processLine(final String inputLine) {
		final Matcher matcher = pattern.matcher(inputLine);
		if (matcher.matches()) {
			final String filename = matcher.group(MATCH_GROUP_FILE);
			final String line = matcher.group(MATCH_GROUP_LINE);
			final String message = matcher.group(MATCH_GROUP_MESSAGE);
			final String rulekey = matcher.group(MATCH_GROUP_RULEKEY);
			
			int lineNr = 0;
			try {
				/** Sonarqube line numbers start from 1, while titan indexes from 0 */
				lineNr = Integer.parseInt(line) + 1;
				if (lineNr < 1) {
					return;
				}
			} catch (NumberFormatException e) {
				LOG.warn("Illegal line number {}", lineNr);
				return;
			}
			
			RuleKey key = RuleKey.of(TTCN3, rulekey);
			if (activeRules.find(key) == null) {
				key = RuleKey.of(TTCN3, "Titanium");
			}
			if (fs.hasFiles(p.hasPath(filename))) {
				final InputFile input = fs.inputFile(p.hasPath(filename));
				final NewIssue newIssue = context.newIssue();
				try {
					newIssue
						.forRule(key)
						.at(newIssue.newLocation()
							.on(input)
							.at(input.selectLine(lineNr))
							.message(message))
						.overrideSeverity(Severity.MINOR)
						.save();
				} catch (IllegalArgumentException e) {
					LOG.warn("File `{}` has no line number {}", filename, lineNr);
				}
			}
		} 
	}
}
