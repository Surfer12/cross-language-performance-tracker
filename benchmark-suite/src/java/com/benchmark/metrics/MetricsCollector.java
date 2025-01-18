package com.benchmark.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Collects performance metrics during benchmark execution.
 */
public class MetricsCollector {
    private final ThreadMXBean threadBean;
    private final MemoryMXBean memoryBean;

    public MetricsCollector() {
        this.threadBean = ManagementFactory.getThreadMXBean();
        this.memoryBean = ManagementFactory.getMemoryMXBean();
    }

    /**
     * Collects metrics while executing the provided task.
     */
    public Map<String, Object> collect(Callable<Void> task) throws Exception {
        Map<String, Object> metrics = new HashMap<>();
        
        // Enable CPU time measurement
        threadBean.setThreadCpuTimeEnabled(true);
        
        // Record start metrics
        long startTime = System.nanoTime();
        long startCpuTime = threadBean.getCurrentThreadCpuTime();
        long startMemory = memoryBean.getHeapMemoryUsage().getUsed();
        
        // Execute the task
        task.call();
        
        // Record end metrics
        long endTime = System.nanoTime();
        long endCpuTime = threadBean.getCurrentThreadCpuTime();
        long endMemory = memoryBean.getHeapMemoryUsage().getUsed();
        
        // Calculate metrics
        metrics.put("execution_time_ms", (endTime - startTime) / 1_000_000.0);
        metrics.put("cpu_time_ms", (endCpuTime - startCpuTime) / 1_000_000.0);
        metrics.put("memory_used_bytes", endMemory - startMemory);
        metrics.put("peak_memory_bytes", memoryBean.getHeapMemoryUsage().getMax());
        
        return metrics;
    }
    
    /**
     * Collects current system metrics without executing a task.
     */
    public Map<String, Object> collectSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("available_processors", Runtime.getRuntime().availableProcessors());
        metrics.put("total_memory_bytes", Runtime.getRuntime().totalMemory());
        metrics.put("free_memory_bytes", Runtime.getRuntime().freeMemory());
        metrics.put("max_memory_bytes", Runtime.getRuntime().maxMemory());
        
        return metrics;
    }
} 