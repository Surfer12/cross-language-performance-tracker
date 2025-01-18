package com.benchmark.core.benchmarks;

import com.benchmark.core.BenchmarkSuite;
import com.benchmark.core.BenchmarkParameters;
import java.util.Arrays;
import java.util.Random;

/**
 * Benchmark implementation for sorting algorithms.
 */
public class SortingBenchmark implements BenchmarkSuite {
    private final String algorithm;
    private final int[] sizes;
    private final Random random;
    
    public SortingBenchmark(String algorithm, int[] sizes) {
        this.algorithm = algorithm;
        this.sizes = sizes.clone();
        this.random = new Random();
    }
    
    @Override
    public String getName() {
        return "Sorting_" + algorithm;
    }
    
    @Override
    public String getCategory() {
        return "sorting";
    }
    
    @Override
    public String getLanguage() {
        return "java";
    }
    
    @Override
    public void execute() throws Exception {
        for (int size : sizes) {
            int[] data = generateRandomArray(size);
            
            switch (algorithm.toLowerCase()) {
                case "quicksort":
                    quickSort(data, 0, data.length - 1);
                    break;
                case "mergesort":
                    mergeSort(data, 0, data.length - 1);
                    break;
                case "heapsort":
                    heapSort(data);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported sorting algorithm: " + algorithm);
            }
            
            // Verify sorting
            if (!isSorted(data)) {
                throw new RuntimeException("Array not properly sorted");
            }
        }
    }
    
    private int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt();
        }
        return array;
    }
    
    private boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false;
            }
        }
        return true;
    }
    
    // QuickSort implementation
    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }
    
    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        
        swap(array, i + 1, high);
        return i + 1;
    }
    
    // MergeSort implementation
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }
    
    private void merge(int[] array, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        
        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }
        
        while (i <= mid) {
            temp[k++] = array[i++];
        }
        
        while (j <= right) {
            temp[k++] = array[j++];
        }
        
        System.arraycopy(temp, 0, array, left, temp.length);
    }
    
    // HeapSort implementation
    private void heapSort(int[] array) {
        int n = array.length;
        
        // Build heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }
        
        // Extract elements from heap
        for (int i = n - 1; i > 0; i--) {
            swap(array, 0, i);
            heapify(array, i, 0);
        }
    }
    
    private void heapify(int[] array, int n, int i) {
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
            swap(array, i, largest);
            heapify(array, n, largest);
        }
    }
    
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
} 