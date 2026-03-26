/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 ******************************************************************************/
package org.eclipse.sonarttcn3.measures;

import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

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

    public static final Metric<Integer> NOF_ALTSTEPS =
        new Metric.Builder("NOF_ALTSTEPS", "Number of altsteps", Metric.ValueType.INT)
            .setDescription("Number of altsteps in the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> NOF_TESTCASES =
        new Metric.Builder("NOF_TESTCASES", "Number of test cases", Metric.ValueType.INT)
            .setDescription("Number of test cases in the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> IN_ENVY =
        new Metric.Builder("IN_ENVY", "Internal feature envy", Metric.ValueType.INT)
            .setDescription("Number of references to entities inside the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> OUT_ENVY =
        new Metric.Builder("OUT_ENVY", "External feature envy", Metric.ValueType.INT)
            .setDescription("Number of references to entities outside the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();
    
    public static final Metric<Integer> NOF_FUNCTIONS =
        new Metric.Builder("NOF_FUNCTIONS", "Number of functions", Metric.ValueType.INT)
            .setDescription("Number of functions in the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> NOF_FIXME =
        new Metric.Builder("NOF_FIXME", "Fixme comments", Metric.ValueType.INT)
            .setDescription("Number of comments beginning with \"FIXME\"")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> TIMES_IMPORTED =
        new Metric.Builder("TIMES_IMPORTED", "Imported count", Metric.ValueType.INT)
            .setDescription("Times the module was imported by other modules")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> NOF_IMPORTS =
        new Metric.Builder("NOF_IMPORTS", "Number of imports", Metric.ValueType.INT)
            .setDescription("Count of module importations in the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> EFFERENT_COUPLING =
        new Metric.Builder("EFFERENT_COUPLING", "External assignment references", Metric.ValueType.INT)
            .setDescription("Number of referred assignments that are defined outside the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> AFFERENT_COUPLING =
        new Metric.Builder("AFFERENT_COUPLING", "Internal assignment references", Metric.ValueType.INT)
            .setDescription("Number of referred assignments that are defined inside the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> INSTABILITY =
        new Metric.Builder("INSTABILITY", "Efferent to (efferent plus afferent) coupling ratio", Metric.ValueType.INT)
            .setDescription("Efferent to (efferent plus afferent) coupling ratio")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    public static final Metric<Integer> LINES_OF_CODE =
        new Metric.Builder("LINES_OF_CODE", "Lines of code", Metric.ValueType.INT)
            .setDescription("Number of code lines in the module")
            .setDirection(Metric.DIRECTION_BETTER)
            .setDomain("Ttcn3Plugin")
            .setQualitative(false)
            .create();

    static Map<String, Metric<Integer>> moduleMetrics = Map.ofEntries(
        entry("NOF_STATEMENTS", NOF_STATEMENTS),
        entry("NOF_ALTSTEPS", NOF_ALTSTEPS),
        entry("NOF_TESTCASES", NOF_TESTCASES),
        entry("IN_ENVY", IN_ENVY),
        entry("OUT_ENVY", OUT_ENVY),
        entry("NOF_FUNCTIONS", NOF_FUNCTIONS),
        entry("NOF_FIXME", NOF_FIXME),
        entry("TIMES_IMPORTED", TIMES_IMPORTED),
        entry("NOF_IMPORTS", NOF_IMPORTS),
        entry("EFFERENT_COUPLING", EFFERENT_COUPLING),
        entry("AFFERENT_COUPLING", AFFERENT_COUPLING),
        entry("INSTABILITY", INSTABILITY),
        entry("LINES_OF_CODE", LINES_OF_CODE)
    );

    @Override
    public List<Metric> getMetrics() {
        return List.copyOf(moduleMetrics.values());
    }

    public static Metric<Integer> getMetric(final String key) {
        return moduleMetrics.get(key);
    }
}
