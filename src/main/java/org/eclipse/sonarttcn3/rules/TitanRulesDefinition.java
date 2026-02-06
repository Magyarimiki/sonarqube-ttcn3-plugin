/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.sonarttcn3.languages.Ttcn3Language;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;
import org.eclipse.sonarttcn3.rules.TitaniumRules;

public class TitanRulesDefinition implements RulesDefinition {
	private static List<NewRule> rules = new ArrayList<>(); 
	
	@Override
	public void define(Context context) {
		NewRepository repository = context.createRepository("ttcn3", Ttcn3Language.KEY).setName("ttcn3");
		
		/** default rule for smells that are not annotated in Titanium */
		rules.add(repository.createRule("Titanium")
			.setName("Titanium")
			.setHtmlDescription("Generic rule for titanium code smells")
			.addTags("titan"));
		
		for (final TitaniumRule titaniumRule : TitaniumRules.importedRules) {
			final NewRule rule = repository.createRule(titaniumRule.getKey())
		      .setName(titaniumRule.getName())
		      .addTags("titan")
			  .setSeverity(titaniumRule.getSeverity())
		      .setHtmlDescription(titaniumRule.getDescription());
			
			final String tagRegex = "^[a-z0-9\\+#\\-\\.]+$";
			final Pattern pattern = Pattern.compile(tagRegex);
			if (titaniumRule.getTags().length > 0) {
				for (final String tag : titaniumRule.getTags()) {
					final Matcher matcher = pattern.matcher(tag);
					if (!matcher.matches()) {
						continue;
					}
					rule.addTags(tag);
				}
			}

			rules.add(rule);
		}
		repository.done();
	}
	
	public static List<NewRule> getRules() {
		return rules;
	}
}
