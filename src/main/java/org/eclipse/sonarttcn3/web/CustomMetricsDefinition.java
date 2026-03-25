/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.Page.Scope;
import org.sonar.api.web.page.PageDefinition;

import static org.sonar.api.web.page.Page.Qualifier.PROJECT;

public class CustomMetricsDefinition implements PageDefinition {
    @Override
    public void define(Context context) {
        context.addPage(
            Page.builder("ttcn3/module_metrics")
                .setName("Module metrics")
                .setScope(Scope.COMPONENT)
                .setComponentQualifiers(PROJECT)
                .build()
        );
    }
}
