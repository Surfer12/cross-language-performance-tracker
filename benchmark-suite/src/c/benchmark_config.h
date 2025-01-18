#ifndef BENCHMARK_CONFIG_H
#define BENCHMARK_CONFIG_H

#include <stdbool.h>

// Forward declarations
typedef struct metrics_collector_t metrics_collector_t;
typedef struct report_generator_t report_generator_t;

// Benchmark metrics structure
typedef struct {
    double execution_time;
    size_t memory_usage;
    double cpu_utilization;
    uint64_t cache_misses;
} benchmark_metrics_t;

// Benchmark suite structure
typedef struct benchmark_suite_t {
    char* name;
    int (*execute)(struct benchmark_suite_t* self);
    void* context;
    void (*cleanup)(struct benchmark_suite_t* self);
} benchmark_suite_t;

// Reporting configuration
typedef struct {
    char** formats;
    int num_formats;
    char** graph_types;
    int num_graph_types;
} reporting_config_t;

// Main configuration structure
typedef struct {
    bool parallel_execution;
    int timeout_seconds;
    char* output_dir;
    char* log_level;
    int num_suites;
    reporting_config_t reporting;
} benchmark_config_t;

// Configuration functions
benchmark_config_t* load_benchmark_config(const char* config_path);
void destroy_benchmark_config(benchmark_config_t* config);

// Benchmark suite functions
benchmark_suite_t** prepare_benchmark_suites(benchmark_config_t* config);
void destroy_benchmark_suites(benchmark_suite_t** suites, int num_suites);

// Metrics collection functions
metrics_collector_t* create_metrics_collector(void);
void collect_system_metrics(metrics_collector_t* collector, benchmark_metrics_t* metrics);
void destroy_metrics_collector(metrics_collector_t* collector);

// Report generation functions
report_generator_t* create_report_generator(void);
void add_benchmark_result(report_generator_t* reporter, const char* suite_name, benchmark_metrics_t* metrics);
void generate_benchmark_reports(report_generator_t* reporter, reporting_config_t config);
void destroy_report_generator(report_generator_t* reporter);

#endif // BENCHMARK_CONFIG_H 