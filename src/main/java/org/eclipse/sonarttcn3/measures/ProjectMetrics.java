/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.measures;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public class ProjectMetrics implements Metrics {
    public static final Metric<Integer> NOF_TTCN3_MODULES =
        new Metric.Builder("NOF_TTCN3_MODULES", "Number of TTCN3 modules", Metric.ValueType.INT)
            .setDescription("Number of TTCN3 modules in the project")
            .setDirection(Metric.DIRECTION_BETTER)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_SIZE)
            .create();
    
    public static final Metric<Integer> NOF_ASN1_MODULES =            
        new Metric.Builder("NOF_ASN1_MODULES", "Number of ASN1 modules", Metric.ValueType.INT)
            .setDescription("Number of ASN1 modules in the project")
            .setDirection(Metric.DIRECTION_BETTER)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_SIZE)
            .create();
            
    static Map<String, Metric<Integer>> projectMetrics = Map.of(
        "NOF_TTCN3_MODULES", NOF_TTCN3_MODULES,
        "NOF_ASN1_MODULES", NOF_ASN1_MODULES
    );

    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(
            NOF_TTCN3_MODULES,
            NOF_ASN1_MODULES
        );
    }
    
    public static Metric<Integer> getMetric(final String key) {
        return projectMetrics.get(key);
    }
}
