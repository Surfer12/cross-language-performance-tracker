package com.benchmark.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Main benchmark runner class that coordinates benchmark execution
 * across different languages and collects results.
 */
public class BenchmarkRunner {
    private final BenchmarkConfig config;
    private final MetricsCollector metricsCollector;
    private final ReportGenerator reportGenerator;

    public BenchmarkRunner(String configPath) {
        this.config = BenchmarkConfig.fromYaml(configPath);
        this.metricsCollector = new MetricsCollector();
        this.reportGenerator = new ReportGenerator();
    }

    /**
     * Executes benchmarks according to configuration
     */
    public void runBenchmarks() {
        List<BenchmarkSuite> suites = prepareBenchmarkSuites();
        
        if (config.isParallelExecutionEnabled()) {
            runParallel(suites);
        } else {
            runSequential(suites);
        }
        
        generateReports();
    }

    private List<BenchmarkSuite> prepareBenchmarkSuites() {
        // Implementation will prepare benchmark suites based on config
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void runParallel(List<BenchmarkSuite> suites) {
        CompletableFuture<?>[] futures = suites.stream()
            .map(suite -> CompletableFuture.runAsync(() -> runSuite(suite)))
            .toArray(CompletableFuture[]::new);
        
        CompletableFuture.allOf(futures).join();
    }

    private void runSequential(List<BenchmarkSuite> suites) {
        suites.forEach(this::runSuite);
    }

    private void runSuite(BenchmarkSuite suite) {
        try {
            Map<String, Object> metrics = metricsCollector.collect(() -> suite.execute());
            reportGenerator.addResult(suite.getName(), metrics);
        } catch (Exception e) {
            System.err.println("Failed to run suite: " + suite.getName());
            e.printStackTrace();
        }
    }

    private void generateReports() {
        reportGenerator.generateReports(config.getReportingConfig());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: BenchmarkRunner <config-path>");
            System.exit(1);
        }

        BenchmarkRunner runner = new BenchmarkRunner(args[0]);
        runner.runBenchmarks();
    }
} 