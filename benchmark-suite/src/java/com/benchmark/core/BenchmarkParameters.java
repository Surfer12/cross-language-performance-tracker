package com.benchmark.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds parameters for benchmark execution.
 */
public class BenchmarkParameters {
    private final Map<String, Object> parameters;
    
    public BenchmarkParameters() {
        this.parameters = new HashMap<>();
    }
    
    /**
     * Sets a parameter value.
     */
    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }
    
    /**
     * Gets a parameter value.
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key, Class<T> type) {
        Object value = parameters.get(key);
        if (value == null) {
            return null;
        }
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException(
                String.format("Parameter '%s' is of type %s, not %s",
                    key, value.getClass().getName(), type.getName())
            );
        }
        return (T) value;
    }
    
    /**
     * Gets a parameter value with a default.
     */
    public <T> T getParameter(String key, Class<T> type, T defaultValue) {
        T value = getParameter(key, type);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Gets all parameters.
     */
    public Map<String, Object> getAllParameters() {
        return new HashMap<>(parameters);
    }
    
    /**
     * Checks if a parameter exists.
     */
    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }
    
    /**
     * Removes a parameter.
     */
    public void removeParameter(String key) {
        parameters.remove(key);
    }
    
    /**
     * Clears all parameters.
     */
    public void clearParameters() {
        parameters.clear();
    }
    
    /**
     * Creates a builder for BenchmarkParameters.
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder class for BenchmarkParameters.
     */
    public static class Builder {
        private final BenchmarkParameters params;
        
        private Builder() {
            this.params = new BenchmarkParameters();
        }
        
        public Builder parameter(String key, Object value) {
            params.setParameter(key, value);
            return this;
        }
        
        public BenchmarkParameters build() {
            return params;
        }
    }
} 