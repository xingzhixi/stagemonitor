package org.stagemonitor.benchmark.profiler.simple;

import com.google.caliper.Benchmark;
import com.google.caliper.api.VmOptions;
import org.stagemonitor.requestmonitor.profiler.CallStackElement;

@VmOptions({"-Xmx6144m", "-Xms6144m", "-XX:NewSize=6000m", "-XX:MaxNewSize=6000m" })
public class SimpleProfilerBenchmark {

	private AroundProfilerTest aroundProfilerTest = new AroundProfilerTest();
	private BeforeAfterProfilerTest beforeAfterProfilerTest = new BeforeAfterProfilerTest();

	@Benchmark
	public int aroundProfiler(int iter) {
		int dummy = 0;
		CallStackElement root = null;
		for (int i = 0; i < iter; i++) {
			root = new CallStackElement("root");
			AroundProfiler.setMethodCallRoot(root);
			dummy |= aroundProfilerTest.method1();
			AroundProfiler.stop(root);
		}
		if (!root.getChildren().get(0).getSignature().contains("method1")) {
			throw new IllegalStateException("profiling did not work");
		}
		return dummy;
	}

	@Benchmark
	public int beforeAfterProfiler(int iter) {
		int dummy = 0;
		CallStackElement root = null;
		for (int i = 0; i < iter; i++) {
			root = new CallStackElement("root");
			BeforeAfterProfiler.setMethodCallRoot(root);
			dummy |= beforeAfterProfilerTest.method1();
			BeforeAfterProfiler.stop();
		}
		if (!root.getChildren().get(0).getSignature().contains("method1")) {
			throw new IllegalStateException("profiling did not work");
		}
		return dummy;
	}
}
