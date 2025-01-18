package com.benchmark.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generates benchmark reports in various formats.
 */
public class ReportGenerator {
    private final Map<String, Map<String, Object>> results;
    private final ObjectMapper jsonMapper;
    private final CsvMapper csvMapper;
    
    public ReportGenerator() {
        this.results = new ConcurrentHashMap<>();
        this.jsonMapper = new ObjectMapper();
        this.csvMapper = new CsvMapper();
    }
    
    /**
     * Adds benchmark results for a suite.
     */
    public void addResult(String suiteName, Map<String, Object> metrics) {
        results.put(suiteName, new HashMap<>(metrics));
    }
    
    /**
     * Generates reports in specified formats.
     */
    public void generateReports(ReportingConfig config) throws IOException {
        Path outputDir = Path.of(config.getOutputDirectory());
        Files.createDirectories(outputDir);
        
        for (String format : config.getFormats()) {
            switch (format.toLowerCase()) {
                case "json":
                    generateJsonReport(outputDir);
                    break;
                case "csv":
                    generateCsvReport(outputDir);
                    break;
                case "html":
                    generateHtmlReport(outputDir);
                    break;
                default:
                    System.err.println("Unsupported format: " + format);
            }
        }
        
        if (config.isGenerateGraphs()) {
            generateGraphs(outputDir, config.getGraphTypes());
        }
    }
    
    private void generateJsonReport(Path outputDir) throws IOException {
        File jsonFile = outputDir.resolve("benchmark_results.json").toFile();
        jsonMapper.writerWithDefaultPrettyPrinter()
                 .writeValue(jsonFile, results);
    }
    
    private void generateCsvReport(Path outputDir) throws IOException {
        File csvFile = outputDir.resolve("benchmark_results.csv").toFile();
        
        // Create CSV schema from results
        Set<String> metrics = new HashSet<>();
        results.values().forEach(m -> metrics.addAll(m.keySet()));
        
        CsvSchema.Builder schemaBuilder = CsvSchema.builder()
            .addColumn("suite_name");
        metrics.forEach(schemaBuilder::addColumn);
        
        // Convert results to rows
        List<Map<String, Object>> rows = new ArrayList<>();
        results.forEach((suite, metrics_) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("suite_name", suite);
            row.putAll(metrics_);
            rows.add(row);
        });
        
        csvMapper.writer(schemaBuilder.build())
                .writeValue(csvFile, rows);
    }
    
    private void generateHtmlReport(Path outputDir) throws IOException {
        File htmlFile = outputDir.resolve("benchmark_results.html").toFile();
        
        StringBuilder html = new StringBuilder()
            .append("<!DOCTYPE html>\n")
            .append("<html><head><title>Benchmark Results</title>")
            .append("<style>")
            .append("table { border-collapse: collapse; width: 100%; }")
            .append("th, td { border: 1px solid black; padding: 8px; text-align: left; }")
            .append("th { background-color: #f2f2f2; }")
            .append("</style></head><body>")
            .append("<h1>Benchmark Results</h1>")
            .append("<table><tr><th>Suite</th>");
        
        // Get all metric names
        Set<String> metrics = new HashSet<>();
        results.values().forEach(m -> metrics.addAll(m.keySet()));
        metrics.forEach(metric -> html.append("<th>").append(metric).append("</th>"));
        html.append("</tr>");
        
        // Add results
        results.forEach((suite, metrics_) -> {
            html.append("<tr><td>").append(suite).append("</td>");
            metrics.forEach(metric -> 
                html.append("<td>")
                    .append(metrics_.getOrDefault(metric, "N/A"))
                    .append("</td>")
            );
            html.append("</tr>");
        });
        
        html.append("</table></body></html>");
        Files.writeString(htmlFile.toPath(), html.toString());
    }
    
    private void generateGraphs(Path outputDir, List<String> graphTypes) {
        // Implementation for generating graphs (e.g., using JFreeChart)
        // This would create visual representations of the benchmark results
        throw new UnsupportedOperationException("Graph generation not implemented yet");
    }
} 