package cn.com.iresearch.ray.urlanalyz;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;

public class ThrowLoop {
	private List<ThrowRunner> loops;

	public ThrowLoop() {
		this.loops = new LinkedList();
	}

	public void add(ThrowRunner runner) {
		this.loops.add(runner);
	}

	public InputStream run() {
		int i = 0;
		while (i < this.loops.size()) {
			try {
				InputStream is = ((ThrowRunner) this.loops.get(i)).runner();
				return is;
			} catch (Exception e) {
				System.err.println(e.getMessage());
				++i;
			}
		}
		return null;
	}

	public static abstract class ThrowRunner {
		public  String fileProperty;
		public  String fileName;
		public  Configuration conf;

		public ThrowRunner(String fileProperty,String fileName, Configuration conf) {
			this.fileProperty = fileProperty;
			this.fileName = fileName;
			this.conf = conf;
		}

		public abstract InputStream runner() throws Exception;
	}
}
