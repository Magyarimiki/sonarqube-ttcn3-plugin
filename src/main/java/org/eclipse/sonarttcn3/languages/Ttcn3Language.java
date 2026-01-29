/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.languages;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

public class Ttcn3Language extends AbstractLanguage {
	public static final String NAME = "TTCN3";
	public static final String KEY = "ttcn";
	
	private final Configuration config;
	
	public Ttcn3Language(Configuration config) {
	    super(KEY, NAME);
	    this.config = config;
	}
	
	@Override
	public String[] getFileSuffixes() {
		return new String[] { 
			"ttcn",
			"ttcn3",
			"ttcnpp",
			"asn"
		};
	}
}
