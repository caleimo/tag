package cn.com.iresearch.ray.conf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;


public class IrsConfiguration extends Configuration {
	private static final Log LOG = LogFactory.getLog(IrsConfiguration.class);
	private static final IrsConfiguration ireConfig = new IrsConfiguration();
	
	public static Configuration getInstance(){
		return ireConfig;
	}
	
	/**
	 * add custom configuration 
	 */
    private IrsConfiguration()
    {
        initIrsConfiguration(this);
        this.addResource("ire-tag.xml");
    }

	private void initIrsConfiguration(Configuration conf) {
		//conf.set("fs.defaultFS", "hdfs://irs01:8020");
		//conf.set("mapreduce.framework.name", "yarn");
		//conf.set("yarn.resourcemanager.hostname", "irs01");
        conf.addResource("hdfs-site.xml");
	}
	
	


}
