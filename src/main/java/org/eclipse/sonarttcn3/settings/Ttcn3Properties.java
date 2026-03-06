/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.settings;

import java.util.List;

import org.eclipse.sonarttcn3.languages.Ttcn3Language;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import static java.util.Arrays.asList;

public class Ttcn3Properties {
	public static final String FILE_SUFFIXES_KEY = "sonar.ttcn3.file.suffixes";
	public static final String FILE_SUFFIXES_DEFAULT_VALUE = ".ttcn,.ttcn3,.ttcnpp,.asn";
	public static final String ENABLE_OOP_KEY = "sonar.ttcn3.enableOOP";
	public static final String ENABLE_REALTIME_KEY = "sonar.ttcn3.enableRealtime";
	
	private Ttcn3Properties() {
		
	}
	
	public static List<PropertyDefinition> getProperties() {
		final PropertyDefinition[] props = {
			PropertyDefinition.builder(FILE_SUFFIXES_KEY)
				.defaultValue(FILE_SUFFIXES_DEFAULT_VALUE)
				.name("File suffixes")
				.description("List of TTCN3 file suffixes")
				.onQualifiers(Qualifiers.PROJECT)
				.multiValues(true)
				.category(Ttcn3Language.NAME)
				.build(),	

				PropertyDefinition.builder(ENABLE_OOP_KEY)
					.type(PropertyType.BOOLEAN)
					.defaultValue("false")
					.name("Enable OOP")
					.description("Enable the TTCN3 OOP extension")
					.onQualifiers(Qualifiers.PROJECT)
					.multiValues(false)
					.category(Ttcn3Language.NAME)
					.build(),	
				
				PropertyDefinition.builder(ENABLE_REALTIME_KEY)
					.type(PropertyType.BOOLEAN)
					.defaultValue("false")
					.name("Enable realtime")
					.description("Enable the TTCN3 realtime extension")
					.onQualifiers(Qualifiers.PROJECT)
					.multiValues(false)
					.category(Ttcn3Language.NAME)
					.build()
			};

	    return asList(props);
	  }
}
