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

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

public class ModuleMetrics implements Metrics {
    public static final Metric<Integer> NOF_STATEMENTS =
        new Metric.Builder("NOF_STATEMENTS", "Number of statements", Metric.ValueType.INT)
            .setDescription("Number of statements in the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

     static Map<String, Metric<Integer>> moduleMetrics = Map.of(
        "NOF_STATEMENTS", NOF_STATEMENTS
    );

    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(
            NOF_STATEMENTS
        );
    }

    public static Metric<Integer> getMetric(final String key) {
        return moduleMetrics.get(key);
    }
}
