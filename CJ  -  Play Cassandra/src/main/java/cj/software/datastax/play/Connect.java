package cj.software.datastax.play;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;

public class Connect
{
	private Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] pArgs)
	{
		Connect lConnect = new Connect();
		lConnect.displayClusterInfos();
	}

	private void displayClusterInfos()
	{
		String lHost = System.getProperty("host");
		logger.info("connecting to \"%s\"", lHost);
		try (Cluster lCluster = Cluster.builder().addContactPoint(lHost).build())
		{
			Metadata lMeta = lCluster.getMetadata();
			logger.info("Connected to cluster \"%s\" meta \"%s\"", lCluster.getClusterName(), lMeta.getClusterName());
		}
	}
}
