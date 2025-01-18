from benchmark.core import BenchmarkSuite
from benchmark.parameters import BenchmarkParameters
from random import random_float64

struct SortingBenchmark(BenchmarkSuite):
    var algorithm: String
    var sizes: List[Int]
    
    fn __init__(inout self, algorithm: String, sizes: List[Int]):
        self.algorithm = algorithm
        self.sizes = sizes
    
    fn get_name(self) -> String:
        return "Sorting_" + self.algorithm
    
    fn get_category(self) -> String:
        return "sorting"
    
    fn get_language(self) -> String:
        return "mojo"
    
    fn execute(self) raises:
        for size in self.sizes:
            var data = self.generate_random_array(size)
            
            match self.algorithm.lower():
                case "quicksort":
                    self.quick_sort(data, 0, data.size - 1)
                case "mergesort":
                    self.merge_sort(data, 0, data.size - 1)
                case "heapsort":
                    self.heap_sort(data)
                else:
                    raise "Unsupported sorting algorithm: " + self.algorithm
            
            # Verify sorting
            if not self.is_sorted(data):
                raise "Array not properly sorted"
    
    fn generate_random_array(self, size: Int) -> List[Float64]:
        var array = List[Float64](capacity=size)
        for _ in range(size):
            array.append(random_float64())
        return array
    
    fn is_sorted(self, array: List[Float64]) -> Bool:
        for i in range(1, array.size):
            if array[i-1] > array[i]:
                return False
        return True
    
    fn quick_sort(self, inout array: List[Float64], low: Int, high: Int):
        if low < high:
            var pi = self.partition(array, low, high)
            self.quick_sort(array, low, pi - 1)
            self.quick_sort(array, pi + 1, high)
    
    fn partition(self, inout array: List[Float64], low: Int, high: Int) -> Int:
        var pivot = array[high]
        var i = low - 1
        
        for j in range(low, high):
            if array[j] <= pivot:
                i += 1
                self.swap(array, i, j)
        
        self.swap(array, i + 1, high)
        return i + 1
    
    fn merge_sort(self, inout array: List[Float64], left: Int, right: Int):
        if left < right:
            var mid = (left + right) // 2
            self.merge_sort(array, left, mid)
            self.merge_sort(array, mid + 1, right)
            self.merge(array, left, mid, right)
    
    fn merge(self, inout array: List[Float64], left: Int, mid: Int, right: Int):
        var temp = List[Float64](capacity=right - left + 1)
        var i = left
        var j = mid + 1
        
        while i <= mid and j <= right:
            if array[i] <= array[j]:
                temp.append(array[i])
                i += 1
            else:
                temp.append(array[j])
                j += 1
        
        while i <= mid:
            temp.append(array[i])
            i += 1
        
        while j <= right:
            temp.append(array[j])
            j += 1
        
        for k in range(temp.size):
            array[left + k] = temp[k]
    
    fn heap_sort(self, inout array: List[Float64]):
        var n = array.size
        
        # Build heap
        for i in range(n // 2 - 1, -1, -1):
            self.heapify(array, n, i)
        
        # Extract elements from heap
        for i in range(n - 1, 0, -1):
            self.swap(array, 0, i)
            self.heapify(array, i, 0)
    
    fn heapify(self, inout array: List[Float64], n: Int, i: Int):
        var largest = i
        var left = 2 * i + 1
        var right = 2 * i + 2
        
        if left < n and array[left] > array[largest]:
            largest = left
        
        if right < n and array[right] > array[largest]:
            largest = right
        
        if largest != i:
            self.swap(array, i, largest)
            self.heapify(array, n, largest)
    
    fn swap(self, inout array: List[Float64], i: Int, j: Int):
        var temp = array[i]
        array[i] = array[j]
        array[j] = temp 