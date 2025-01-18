#ifndef BENCHMARK_SUITE_H
#define BENCHMARK_SUITE_H

#include <stdbool.h>
#include <stddef.h>

// Forward declaration of benchmark parameters
typedef struct benchmark_parameters_t benchmark_parameters_t;

// Benchmark suite structure
typedef struct benchmark_suite_t {
    // Name of the benchmark suite
    const char* (*get_name)(struct benchmark_suite_t* self);
    
    // Execute the benchmark suite
    int (*execute)(struct benchmark_suite_t* self);
    
    // Get the benchmark category
    const char* (*get_category)(struct benchmark_suite_t* self);
    
    // Get the implementation language
    const char* (*get_language)(struct benchmark_suite_t* self);
    
    // Get benchmark parameters
    benchmark_parameters_t* (*get_parameters)(struct benchmark_suite_t* self);
    
    // Cleanup after benchmark
    void (*cleanup)(struct benchmark_suite_t* self);
    
    // Validate benchmark configuration
    int (*validate)(struct benchmark_suite_t* self);
    
    // Private data for the benchmark suite
    void* context;
} benchmark_suite_t;

// Benchmark parameters structure
typedef struct benchmark_parameters_t {
    // Add parameters storage mechanism
    void* data;
    
    // Set a parameter
    void (*set_parameter)(struct benchmark_parameters_t* self, 
                          const char* key, void* value);
    
    // Get a parameter
    void* (*get_parameter)(struct benchmark_parameters_t* self, 
                           const char* key);
    
    // Check if parameter exists
    bool (*has_parameter)(struct benchmark_parameters_t* self, 
                          const char* key);
    
    // Remove a parameter
    void (*remove_parameter)(struct benchmark_parameters_t* self, 
                             const char* key);
} benchmark_parameters_t;

// Create a new benchmark suite
benchmark_suite_t* benchmark_suite_create(
    const char* (*get_name)(benchmark_suite_t* self),
    int (*execute)(benchmark_suite_t* self),
    const char* (*get_category)(benchmark_suite_t* self),
    const char* (*get_language)(benchmark_suite_t* self),
    benchmark_parameters_t* (*get_parameters)(benchmark_suite_t* self),
    void (*cleanup)(benchmark_suite_t* self),
    int (*validate)(benchmark_suite_t* self)
);

// Destroy a benchmark suite
void benchmark_suite_destroy(benchmark_suite_t* suite);

// Create benchmark parameters
benchmark_parameters_t* benchmark_parameters_create();

// Destroy benchmark parameters
void benchmark_parameters_destroy(benchmark_parameters_t* params);

#endif // BENCHMARK_SUITE_H 