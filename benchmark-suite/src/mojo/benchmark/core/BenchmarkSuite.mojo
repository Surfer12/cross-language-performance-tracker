from benchmark.parameters import BenchmarkParameters

trait BenchmarkSuite:
    """
    Interface for benchmark suites that can be executed by the benchmark runner.
    """
    
    fn get_name(self) -> String:
        """
        Gets the name of this benchmark suite.
        """
        ...
    
    fn execute(self) raises:
        """
        Executes the benchmark suite.
        Raises an exception if the benchmark fails.
        """
        ...
    
    fn get_category(self) -> String:
        """
        Gets the category of this benchmark suite 
        (e.g., "sorting", "matrix", "ai_inference").
        """
        ...
    
    fn get_language(self) -> String:
        """
        Gets the implementation language of this benchmark suite.
        """
        ...
    
    fn get_parameters(self) -> BenchmarkParameters:
        """
        Gets any additional parameters specific to this benchmark suite.
        """
        return BenchmarkParameters()
    
    fn cleanup(self):
        """
        Performs any necessary cleanup after benchmark execution.
        """
        pass
    
    fn validate(self) raises:
        """
        Validates that the benchmark suite is properly configured.
        Raises an IllegalStateException if the configuration is invalid.
        """
        if not self.get_name():
            raise "Benchmark suite name must not be empty"
        
        if not self.get_category():
            raise "Benchmark category must not be empty"
        
        if not self.get_language():
            raise "Implementation language must not be empty" 