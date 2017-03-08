package cn.com.iresearch.ray.urlanalyz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import cn.com.iresearch.ray.conf.IrsConfiguration;
/**
 * 
 * @author Ray
 * get the InputStream
 *
 */
public class RourseCenter implements Serializable {
	public static InputStream getResourceAsInputSteam(String propertiesName, String dictName) {
		Configuration conf = IrsConfiguration.getInstance();
		ThrowLoop throwLoop = new ThrowLoop();
		//添加hdfs的规则文件的流（优先）
		throwLoop.add(new ThrowLoop.ThrowRunner(propertiesName,dictName, conf) {
			// Loads from local hdfs
			@Override
			public InputStream runner() throws Exception {
				
				String filePath = (new StringBuilder()
						.append(conf.get(this.fileProperty)).append(this.fileName))
						.toString();
				Path modelPath = new Path(filePath);
				System.out.println("[IRESEARCH INFO] Tring loads from HDFS. "+modelPath.toString());
				InputStream is = RourseCenter.getHDFSInputStreamFromModel(modelPath, conf);
				if(is == null)
                    throw new IOException((new StringBuilder()).append("[IRESEARCH WARNING] ").append(this.fileName).append(" Error:  HDFS FileSystem get path error. ").toString());
				else{
					System.out.println("[IRESEARCH INFO] Loads "+this.fileName+" from HDFS success .");
					return is;
				}
			}
		});
		
		//添加本地的规则文件的流
		throwLoop.add(new ThrowLoop.ThrowRunner(propertiesName,dictName, conf) {
			
			@Override
			public InputStream runner() throws Exception {
				System.out.println("[IRESEARCH INFO] Tring ... loads from local.");
				String filePath = (new StringBuilder()
				.append(conf.get(this.fileProperty+".local") == null?"":conf.get(this.fileProperty+".local"))
				.append(this.fileName)) 
				.toString();
				File file = new File(filePath);
				InputStream is = RourseCenter.getHDFSInputStreamFromlocal(file);
				if(is == null)
                    throw new IOException((new StringBuilder()).append("[IRESEARCH WARNING] ").append(this.fileName).append(" Error:  local FileSystem get path error. ").toString());
				else{
					System.out.println("[IRESEARCH INFO] Loads "+this.fileName+" from local success .");
					return is;
				}
			}
		});
		
		InputStream in = throwLoop.run();
		return in;
	}
	
	public static InputStream getLocalResourceAsInputSteam(String propertiesName, String dictName) {
		Configuration conf = IrsConfiguration.getInstance();
		ThrowLoop throwLoop = new ThrowLoop();
		//添加本地的规则文件的流
				throwLoop.add(new ThrowLoop.ThrowRunner(propertiesName,dictName, conf) {
					
					@Override
					public InputStream runner() throws Exception {
						System.out.println("[IRESEARCH INFO] Tring ... loads from local.");
						String filePath = (new StringBuilder()
						.append(conf.get(this.fileProperty)).append(this.fileName)) 
						.toString();
						File file = new File(filePath);
						InputStream is = RourseCenter.getHDFSInputStreamFromlocal(file);
						if(is == null)
		                    throw new IOException((new StringBuilder()).append("[IRESEARCH WARNING] ").append(this.fileName).append(" Error:  local FileSystem get path error. ").toString());
						else{
							System.out.println("[IRESEARCH INFO] Loads "+this.fileName+" from local success .");
							return is;
						}
					}
				});
				
				InputStream in = throwLoop.run();
				return in;
	}
	
	protected static InputStream getHDFSInputStreamFromlocal(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	protected static InputStream getHDFSInputStreamFromModel(Path modelPath,
			Configuration conf) throws IOException {
		FileSystem fs;
		if (conf == null)
			conf = IrsConfiguration.getInstance();
		fs = FileSystem.get(conf);
		
		if (fs.exists(modelPath))
			return fs.open(modelPath);
		else
			return null;
	}

}
