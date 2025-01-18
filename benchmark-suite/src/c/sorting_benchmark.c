#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "include/benchmark_suite.h"

// Sorting Benchmark Context
typedef struct {
    char* algorithm;
    int* sizes;
    size_t num_sizes;
} sorting_benchmark_context_t;

// Function prototypes for sorting algorithms
static void quick_sort(int* array, int low, int high);
static int partition(int* array, int low, int high);
static void merge_sort(int* array, int left, int right);
static void merge(int* array, int left, int mid, int right);
static void heap_sort(int* array, int n);
static void heapify(int* array, int n, int i);
static void swap(int* a, int* b);

// Benchmark suite function implementations
static const char* sorting_benchmark_get_name(benchmark_suite_t* suite) {
    sorting_benchmark_context_t* ctx = (sorting_benchmark_context_t*)suite->context;
    static char name[256];
    snprintf(name, sizeof(name), "Sorting_%s", ctx->algorithm);
    return name;
}

static const char* sorting_benchmark_get_category(benchmark_suite_t* suite) {
    return "sorting";
}

static const char* sorting_benchmark_get_language(benchmark_suite_t* suite) {
    return "c";
}

static int sorting_benchmark_execute(benchmark_suite_t* suite) {
    sorting_benchmark_context_t* ctx = (sorting_benchmark_context_t*)suite->context;
    
    // Seed random number generator
    srand(time(NULL));
    
    for (size_t s = 0; s < ctx->num_sizes; s++) {
        int size = ctx->sizes[s];
        int* data = malloc(size * sizeof(int));
        
        // Generate random array
        for (int i = 0; i < size; i++) {
            data[i] = rand();
        }
        
        // Sort based on algorithm
        if (strcmp(ctx->algorithm, "quicksort") == 0) {
            quick_sort(data, 0, size - 1);
        } else if (strcmp(ctx->algorithm, "mergesort") == 0) {
            merge_sort(data, 0, size - 1);
        } else if (strcmp(ctx->algorithm, "heapsort") == 0) {
            heap_sort(data, size);
        } else {
            free(data);
            return -1;  // Unsupported algorithm
        }
        
        // Verify sorting
        for (int i = 1; i < size; i++) {
            if (data[i-1] > data[i]) {
                free(data);
                return -1;  // Not sorted
            }
        }
        
        free(data);
    }
    
    return 0;
}

static benchmark_parameters_t* sorting_benchmark_get_parameters(benchmark_suite_t* suite) {
    return benchmark_parameters_create();
}

static void sorting_benchmark_cleanup(benchmark_suite_t* suite) {
    sorting_benchmark_context_t* ctx = (sorting_benchmark_context_t*)suite->context;
    free(ctx->algorithm);
    free(ctx->sizes);
    free(ctx);
}

static int sorting_benchmark_validate(benchmark_suite_t* suite) {
    sorting_benchmark_context_t* ctx = (sorting_benchmark_context_t*)suite->context;
    
    if (!ctx->algorithm || strlen(ctx->algorithm) == 0) {
        return -1;
    }
    
    if (!ctx->sizes || ctx->num_sizes == 0) {
        return -1;
    }
    
    return 0;
}

// Create a sorting benchmark suite
benchmark_suite_t* create_sorting_benchmark(const char* algorithm, int* sizes, size_t num_sizes) {
    benchmark_suite_t* suite = malloc(sizeof(benchmark_suite_t));
    sorting_benchmark_context_t* ctx = malloc(sizeof(sorting_benchmark_context_t));
    
    ctx->algorithm = strdup(algorithm);
    ctx->sizes = malloc(num_sizes * sizeof(int));
    memcpy(ctx->sizes, sizes, num_sizes * sizeof(int));
    ctx->num_sizes = num_sizes;
    
    suite->context = ctx;
    suite->get_name = sorting_benchmark_get_name;
    suite->execute = sorting_benchmark_execute;
    suite->get_category = sorting_benchmark_get_category;
    suite->get_language = sorting_benchmark_get_language;
    suite->get_parameters = sorting_benchmark_get_parameters;
    suite->cleanup = sorting_benchmark_cleanup;
    suite->validate = sorting_benchmark_validate;
    
    return suite;
}

// Sorting algorithm implementations
static void quick_sort(int* array, int low, int high) {
    if (low < high) {
        int pi = partition(array, low, high);
        quick_sort(array, low, pi - 1);
        quick_sort(array, pi + 1, high);
    }
}

static int partition(int* array, int low, int high) {
    int pivot = array[high];
    int i = low - 1;
    
    for (int j = low; j < high; j++) {
        if (array[j] <= pivot) {
            i++;
            swap(&array[i], &array[j]);
        }
    }
    
    swap(&array[i + 1], &array[high]);
    return i + 1;
}

static void merge_sort(int* array, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        merge_sort(array, left, mid);
        merge_sort(array, mid + 1, right);
        merge(array, left, mid, right);
    }
}

static void merge(int* array, int left, int mid, int right) {
    int left_size = mid - left + 1;
    int right_size = right - mid;
    
    int* left_array = malloc(left_size * sizeof(int));
    int* right_array = malloc(right_size * sizeof(int));
    
    memcpy(left_array, array + left, left_size * sizeof(int));
    memcpy(right_array, array + mid + 1, right_size * sizeof(int));
    
    int i = 0, j = 0, k = left;
    
    while (i < left_size && j < right_size) {
        if (left_array[i] <= right_array[j]) {
            array[k++] = left_array[i++];
        } else {
            array[k++] = right_array[j++];
        }
    }
    
    while (i < left_size) {
        array[k++] = left_array[i++];
    }
    
    while (j < right_size) {
        array[k++] = right_array[j++];
    }
    
    free(left_array);
    free(right_array);
}

static void heap_sort(int* array, int n) {
    // Build max heap
    for (int i = n / 2 - 1; i >= 0; i--) {
        heapify(array, n, i);
    }
    
    // Extract elements from heap
    for (int i = n - 1; i > 0; i--) {
        swap(&array[0], &array[i]);
        heapify(array, i, 0);
    }
}

static void heapify(int* array, int n, int i) {
    int largest = i;
    int left = 2 * i + 1;
    int right = 2 * i + 2;
    
    if (left < n && array[left] > array[largest]) {
        largest = left;
    }
    
    if (right < n && array[right] > array[largest]) {
        largest = right;
    }
    
    if (largest != i) {
        swap(&array[i], &array[largest]);
        heapify(array, n, largest);
    }
}

static void swap(int* a, int* b) {
    int temp = *a;
    *a = *b;
    *b = temp;
} 