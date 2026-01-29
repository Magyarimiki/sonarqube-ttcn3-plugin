/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.languages;

import org.eclipse.sonarttcn3.rules.TitanRulesDefinition;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.server.rule.RulesDefinition.NewRule;

public class Ttcn3QualityProfile implements BuiltInQualityProfilesDefinition {
	@Override
	public void define(Context context) {
		NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("Titanium", Ttcn3Language.KEY);
	    profile.setDefault(true);
	    
	    for (NewRule rule : TitanRulesDefinition.getRules()) {
	    	profile.activateRule("ttcn3", rule.key());
	    }
	    
	    profile.done();
	}
}
