/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.rules;

public class TitaniumRule {
	private String key;
	private String name;
	private String description;

	TitaniumRule(final String key, final String name, final String description) {
		this.key = key;
		this.name = name;
		this.description = description;
	}
	
	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
