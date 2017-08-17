package cj.software.datastax.play;

import java.net.InetAddress;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Configuration;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.ProtocolVersion;

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

			Set<Host> lHosts = lMeta.getAllHosts();
			for (Host bHost : lHosts)
			{
				String lDatacenter = bHost.getDatacenter();
				String lRack = bHost.getRack();
				InetAddress lAddress = bHost.getAddress();
				logger.info("Host: Datacenter = %s, Rack = %s, Address = %s", lDatacenter, lRack, lAddress);
			}

			Configuration lConfiguration = lCluster.getConfiguration();
			ProtocolOptions lProtocolOptions = lConfiguration.getProtocolOptions();
			ProtocolVersion lProtocolVersion = lProtocolOptions.getProtocolVersion();
			logger.info("Protocol Version: %s", lProtocolVersion);
		}
	}
}
