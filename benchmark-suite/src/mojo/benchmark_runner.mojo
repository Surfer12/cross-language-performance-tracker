from python import Python
from benchmark.core import BenchmarkConfig, MetricsCollector
from benchmark.utils import Timer
from benchmark.reporting import ReportGenerator

struct BenchmarkRunner:
    var config: BenchmarkConfig
    var metrics_collector: MetricsCollector
    var report_generator: ReportGenerator

    fn __init__(inout self, config_path: String):
        # Load configuration from YAML
        let py = Python.import_module("yaml")
        with open(config_path, "r") as f:
            self.config = py.safe_load(f)
        self.metrics_collector = MetricsCollector()
        self.report_generator = ReportGenerator()

    fn run_benchmarks(self) raises:
        let suites = self.prepare_benchmark_suites()
        
        if self.config.parallel_execution:
            self.run_parallel(suites)
        else:
            self.run_sequential(suites)
        
        self.generate_reports()

    fn prepare_benchmark_suites(self) -> List[BenchmarkSuite]:
        # Implementation will prepare benchmark suites based on config
        return []

    fn run_parallel(self, suites: List[BenchmarkSuite]) raises:
        # Parallel execution using Mojo's parallel constructs
        @parameter
        fn run_suite(suite: BenchmarkSuite):
            try:
                let timer = Timer()
                let metrics = self.metrics_collector.collect(suite.execute)
                self.report_generator.add_result(suite.name, metrics)
            except:
                print("Failed to run suite:", suite.name)

        parallelize[run_suite](suites)

    fn run_sequential(self, suites: List[BenchmarkSuite]) raises:
        for suite in suites:
            try:
                let timer = Timer()
                let metrics = self.metrics_collector.collect(suite.execute)
                self.report_generator.add_result(suite.name, metrics)
            except:
                print("Failed to run suite:", suite.name)

    fn generate_reports(self):
        self.report_generator.generate_reports(self.config.reporting)

fn main() raises:
    if len(ARGV) < 2:
        print("Usage: benchmark_runner <config-path>")
        return 1
    
    let runner = BenchmarkRunner(ARGV[1])
    runner.run_benchmarks() 