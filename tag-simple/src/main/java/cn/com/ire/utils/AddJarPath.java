package cn.com.ire.utils;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.lang.SecurityException;
import org.apache.hadoop.security.authorize.AuthorizationException;
public class AddJarPath {
	public static void addTmpJar(String jarPath, Configuration conf) throws IOException {  
		   System.setProperty("path.separator", ":");  
		    FileSystem fs = FileSystem.getLocal(conf);  
		    String newJarPath = new Path(jarPath).makeQualified(fs).toString();  
		    String tmpjars = conf.get("tmpjars");  
		    if (tmpjars == null || tmpjars.length() == 0) {  
		        conf.set("tmpjars", newJarPath);  
		    } else {  
		        conf.set("tmpjars", tmpjars + "," + newJarPath);  
		    }  
		}  


}
