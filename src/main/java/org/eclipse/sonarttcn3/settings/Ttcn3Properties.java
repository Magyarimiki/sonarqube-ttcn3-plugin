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
import org.sonar.api.resources.Qualifiers;

public class Ttcn3Properties {
	public static final String FILE_SUFFIXES_KEY = "sonar.ttcn3.file.suffixes";
	public static final String FILE_SUFFIXES_DEFAULT_VALUE = ".ttcn";
	
	private Ttcn3Properties() {
		
	}
	
	public static List<PropertyDefinition> getProperties() {
	    return Arrays.asList(PropertyDefinition.builder(FILE_SUFFIXES_KEY)
	      .multiValues(true)
	      .defaultValue(FILE_SUFFIXES_DEFAULT_VALUE)
	      .category("Ttcn3")
	      .name("Ttcn3")
	      .description("Ttcn3")
	      .onQualifiers(Qualifiers.PROJECT)
	      .build());
	  }
}
