package com.benchmark.reporting;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for benchmark report generation.
 */
public class ReportingConfig {
    private String outputDirectory;
    private List<String> formats;
    private boolean generateGraphs;
    private List<String> graphTypes;
    
    public ReportingConfig() {
        this.formats = new ArrayList<>();
        this.graphTypes = new ArrayList<>();
        this.generateGraphs = false;
    }
    
    public String getOutputDirectory() {
        return outputDirectory;
    }
    
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
    
    public List<String> getFormats() {
        return formats;
    }
    
    public void setFormats(List<String> formats) {
        this.formats = formats;
    }
    
    public boolean isGenerateGraphs() {
        return generateGraphs;
    }
    
    public void setGenerateGraphs(boolean generateGraphs) {
        this.generateGraphs = generateGraphs;
    }
    
    public List<String> getGraphTypes() {
        return graphTypes;
    }
    
    public void setGraphTypes(List<String> graphTypes) {
        this.graphTypes = graphTypes;
    }
    
    /**
     * Creates a builder for ReportingConfig.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder class for ReportingConfig.
     */
    public static class Builder {
        private final ReportingConfig config;
        
        private Builder() {
            this.config = new ReportingConfig();
        }
        
        public Builder outputDirectory(String outputDirectory) {
            config.setOutputDirectory(outputDirectory);
            return this;
        }
        
        public Builder addFormat(String format) {
            config.getFormats().add(format);
            return this;
        }
        
        public Builder formats(List<String> formats) {
            config.setFormats(formats);
            return this;
        }
        
        public Builder generateGraphs(boolean generateGraphs) {
            config.setGenerateGraphs(generateGraphs);
            return this;
        }
        
        public Builder addGraphType(String graphType) {
            config.getGraphTypes().add(graphType);
            return this;
        }
        
        public Builder graphTypes(List<String> graphTypes) {
            config.setGraphTypes(graphTypes);
            return this;
        }
        
        public ReportingConfig build() {
            return config;
        }
    }
} 