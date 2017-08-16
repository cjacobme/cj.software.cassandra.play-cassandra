package cj.software.datastax.play;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;

public class Connect
{
	private Logger logger = LoggerFactory.getLogger(Connect.class);

	public static void main(String[] pArgs)
	{
		Connect lConnect = new Connect();
		lConnect.connectToHotel();
	}

	private void connectToHotel()
	{
		try (Cluster lCluster = Cluster.builder().addContactPoint("localhost").build())
		{
			logger.info("connected");
		}
	}
}
