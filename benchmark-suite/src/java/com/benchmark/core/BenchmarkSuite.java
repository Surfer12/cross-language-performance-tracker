package com.benchmark.core;

/**
 * Interface for benchmark suites that can be executed by the benchmark runner.
 */
public interface BenchmarkSuite {
    /**
     * Gets the name of this benchmark suite.
     */
    String getName();
    
    /**
     * Executes the benchmark suite.
     * @throws Exception if the benchmark fails
     */
    void execute() throws Exception;
    
    /**
     * Gets the category of this benchmark suite (e.g., "sorting", "matrix", "ai_inference").
     */
    String getCategory();
    
    /**
     * Gets the implementation language of this benchmark suite.
     */
    String getLanguage();
    
    /**
     * Gets any additional parameters specific to this benchmark suite.
     */
    default BenchmarkParameters getParameters() {
        return new BenchmarkParameters();
    }
    
    /**
     * Performs any necessary cleanup after benchmark execution.
     */
    default void cleanup() {
        // Default implementation does nothing
    }
    
    /**
     * Validates that the benchmark suite is properly configured.
     * @throws IllegalStateException if the configuration is invalid
     */
    default void validate() throws IllegalStateException {
        if (getName() == null || getName().isEmpty()) {
            throw new IllegalStateException("Benchmark suite name must not be empty");
        }
        if (getCategory() == null || getCategory().isEmpty()) {
            throw new IllegalStateException("Benchmark category must not be empty");
        }
        if (getLanguage() == null || getLanguage().isEmpty()) {
            throw new IllegalStateException("Implementation language must not be empty");
        }
    }
} 