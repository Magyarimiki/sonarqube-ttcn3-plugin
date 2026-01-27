/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.sonarttcn3.rules.TitanRulesDefinition;
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
	private static final String REPORT_PATH_KEY = "sonar.titan.reportPaths";
	private static final String DEFAULT_REPORT_PATH = ".titan_compile"; 
	private static final String REPORT_REGEX_DEF = "sonar.titan.regex";
	private static final String DEFAULT_REGEX_DEF = "(?<file>[^:]+):::(?<line>[0-9]+):::(?<message>.+):::(?<rulekey>.+)";
	
	private static final String MATCH_GROUP_FILE = "file";
	private static final String MATCH_GROUP_LINE = "line";
	private static final String MATCH_GROUP_RULEKEY = "rulekey";
	private static final String MATCH_GROUP_MESSAGE = "message";

	private static final Logger LOG = LoggerFactory.getLogger(Ttcn3Sensor.class);
	
	protected SensorContext context;
	private final Checks<Rule> checks;
	private final ActiveRules activeRules;
	
	public Ttcn3Sensor(CheckFactory checkFactory, ActiveRules activeRules) {
		this.activeRules = activeRules;	
		checks = checkFactory.create("ttcn3");
		checks.addAnnotatedChecks(TitanRulesDefinition.getRules());
	}
	
	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name(getClass().getName());
//		descriptor.onlyOnLanguages(Ttcn3Language.KEY);
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
				LOG.error("Mandatory named capture group `" + group + "` is missing from regex");
				isMissing = true;
			}
		}

		return isMissing;
	}

	protected void processReport() {
	    String reportRegex = getRegex();
	    LOG.info("Using regex " + reportRegex);

	    Pattern pattern = null;
	    try {
	    	pattern = Pattern.compile(reportRegex);
	    } catch (PatternSyntaxException e) {
	    	LOG.error("Failed to compile report regex.");
	    	return;
	    }
		if (checkMandatoryMatchingGroups(reportRegex)) {
			return;
		}

	    LOG.info("Report path: " + getReportPath());
		final String reportPath = getReportPath();

	    FileSystem fs = context.fileSystem();
	    FilePredicates p = fs.predicates();
		final InputFile reportFile = fs.inputFile(p.hasPath(reportPath));
		if (reportFile == null) {
			LOG.error("Report file not found: " + reportPath);
			return;
		}

		for (InputFile file : fs.inputFiles(p.hasFilename(getReportPath()))) {
			try (BufferedReader br = new BufferedReader(new FileReader(file.toString()))) {
				String str;
				int lineNr = 0;
				while ((str = br.readLine()) != null) {
					Matcher matcher = pattern.matcher(str);
					if (matcher.matches()) {
						final String filename = matcher.group(MATCH_GROUP_FILE);
						final String line = matcher.group(MATCH_GROUP_LINE);
						final String message = matcher.group(MATCH_GROUP_MESSAGE);
						String rulekey = matcher.group(MATCH_GROUP_RULEKEY);
						
						try {
							/** Sonarqube line numbers start from 1, while titan indexes from 0 */
							lineNr = Integer.parseInt(line) + 1;
							if (lineNr < 1) {
								continue;
							}
						} catch (NumberFormatException e) {
							LOG.warn("Illegal line numer" + lineNr);
							continue;
						}
						
						RuleKey key = RuleKey.of("ttcn3", rulekey);
						if (activeRules.find(key) == null) {
							key = RuleKey.of("ttcn3", "Titanium");
						}
						if (fs.hasFiles(p.hasPath(filename))) {
							InputFile input = fs.inputFile(p.hasPath(filename));
							NewIssue newIssue = context.newIssue();
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
								LOG.warn("File `" + filename + "` has no line number " + lineNr);
								continue;
							}
						}
					}
            	}
			}
	        catch (IOException e) {
	            LOG.error("Error while reading compilation file.");
	        }
		}
	}
}
