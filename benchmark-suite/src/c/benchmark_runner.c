#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>
#include "benchmark_config.h"
#include "metrics_collector.h"
#include "report_generator.h"

typedef struct {
    benchmark_suite_t* suite;
    metrics_collector_t* collector;
    report_generator_t* reporter;
} benchmark_context_t;

// Global configuration
static benchmark_config_t* config = NULL;

// Thread-safe results collection
static pthread_mutex_t results_mutex = PTHREAD_MUTEX_INITIALIZER;

void* run_benchmark_suite(void* arg) {
    benchmark_context_t* ctx = (benchmark_context_t*)arg;
    benchmark_metrics_t metrics;
    
    // Execute benchmark and collect metrics
    clock_t start = clock();
    int result = ctx->suite->execute(ctx->suite);
    clock_t end = clock();
    
    if (result != 0) {
        fprintf(stderr, "Failed to execute benchmark suite: %s\n", ctx->suite->name);
        return NULL;
    }
    
    // Collect metrics
    metrics.execution_time = ((double)(end - start)) / CLOCKS_PER_SEC;
    collect_system_metrics(ctx->collector, &metrics);
    
    // Thread-safe results addition
    pthread_mutex_lock(&results_mutex);
    add_benchmark_result(ctx->reporter, ctx->suite->name, &metrics);
    pthread_mutex_unlock(&results_mutex);
    
    return NULL;
}

int run_benchmarks(const char* config_path) {
    // Load configuration
    config = load_benchmark_config(config_path);
    if (!config) {
        fprintf(stderr, "Failed to load configuration from: %s\n", config_path);
        return 1;
    }
    
    // Initialize components
    metrics_collector_t* collector = create_metrics_collector();
    report_generator_t* reporter = create_report_generator();
    
    // Prepare benchmark suites
    benchmark_suite_t** suites = prepare_benchmark_suites(config);
    if (!suites) {
        fprintf(stderr, "Failed to prepare benchmark suites\n");
        return 1;
    }
    
    if (config->parallel_execution) {
        // Parallel execution using pthreads
        pthread_t* threads = malloc(config->num_suites * sizeof(pthread_t));
        benchmark_context_t* contexts = malloc(config->num_suites * sizeof(benchmark_context_t));
        
        for (int i = 0; i < config->num_suites; i++) {
            contexts[i].suite = suites[i];
            contexts[i].collector = collector;
            contexts[i].reporter = reporter;
            
            if (pthread_create(&threads[i], NULL, run_benchmark_suite, &contexts[i]) != 0) {
                fprintf(stderr, "Failed to create thread for suite: %s\n", suites[i]->name);
                return 1;
            }
        }
        
        // Wait for all threads to complete
        for (int i = 0; i < config->num_suites; i++) {
            pthread_join(threads[i], NULL);
        }
        
        free(threads);
        free(contexts);
    } else {
        // Sequential execution
        benchmark_context_t ctx;
        ctx.collector = collector;
        ctx.reporter = reporter;
        
        for (int i = 0; i < config->num_suites; i++) {
            ctx.suite = suites[i];
            run_benchmark_suite(&ctx);
        }
    }
    
    // Generate reports
    generate_benchmark_reports(reporter, config->reporting);
    
    // Cleanup
    destroy_metrics_collector(collector);
    destroy_report_generator(reporter);
    destroy_benchmark_config(config);
    destroy_benchmark_suites(suites, config->num_suites);
    
    return 0;
}

int main(int argc, char** argv) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <config-path>\n", argv[0]);
        return 1;
    }
    
    return run_benchmarks(argv[1]);
} 