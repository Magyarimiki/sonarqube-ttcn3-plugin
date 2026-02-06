/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3;

import org.eclipse.sonarttcn3.languages.Ttcn3Language;
import org.eclipse.sonarttcn3.languages.Ttcn3QualityProfile;
import org.eclipse.sonarttcn3.rules.TitanRulesDefinition;
import org.eclipse.sonarttcn3.settings.Ttcn3Properties;
import org.sonar.api.Plugin;

/**
 * Sonarqube plugin for the Titan TTCN3 static analyzer
 */
public class TitanPlugin implements Plugin {
	@Override
	public void define(Context context) {
		context.addExtensions(
			Ttcn3Language.class,
			Ttcn3QualityProfile.class,
			TitanRulesDefinition.class,
			Ttcn3Sensor.class
		);

		context.addExtension(Ttcn3Properties.getProperties());
	}
}
