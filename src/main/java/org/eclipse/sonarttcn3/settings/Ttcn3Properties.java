/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.settings;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.config.PropertyDefinition;

public class Ttcn3Properties {
	public static final String FILE_SUFFIXES_KEY = "sonar.ttcn3.file.suffixes";
	public static final String FILE_SUFFIXES_DEFAULT_VALUE = ".ttcn,.ttcn3,.asn,.ttcnpp";
	
	private Ttcn3Properties() {
		
	}
	
	public static List<PropertyDefinition> getProperties() {
	    return Arrays.asList(PropertyDefinition.builder(FILE_SUFFIXES_KEY)
			.defaultValue(FILE_SUFFIXES_DEFAULT_VALUE)
			.name("Ttcn3 file suffixes")
			.description("List of ttcn3 file suffixes")
			.multiValues(true)
		    .build());
	  }
}
