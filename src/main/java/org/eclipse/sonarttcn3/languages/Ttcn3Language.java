/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.languages;

import org.eclipse.sonarttcn3.settings.Ttcn3Properties;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

public class Ttcn3Language extends AbstractLanguage {
	public static final String NAME = "TTCN3";
	public static final String KEY = "ttcn3";
	private final Configuration configuration;
	
	public Ttcn3Language(Configuration configuration) {
	    super(KEY, NAME);
		this.configuration = configuration;
	}

	@Override
	public String[] getFileSuffixes() {
		return configuration.getStringArray(Ttcn3Properties.FILE_SUFFIXES_KEY);
	}
}
