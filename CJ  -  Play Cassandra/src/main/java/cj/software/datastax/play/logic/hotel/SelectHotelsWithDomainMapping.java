package cj.software.datastax.play.logic.hotel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

import cj.software.datastax.play.domain.hotel.Hotel;

public class SelectHotelsWithDomainMapping
{
	private Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] pArgs)
	{
		SelectHotelsWithDomainMapping lInstance = new SelectHotelsWithDomainMapping();
		lInstance.doSomeThings();
	}

	public void doSomeThings()
	{
		String lHostname = System.getProperty("host");
		if (lHostname == null)
		{
			throw new IllegalStateException("System Property \"host\" not set");
		}

		logger.info("connecting to %s...", lHostname);
		try (Cluster lCluster = Cluster.builder().addContactPoint(lHostname).build())
		{
			String lKeyspaceName = "hotel";
			logger.info("connected! now open session on keyspace %s", lKeyspaceName);
			try (Session lSession = lCluster.connect(lKeyspaceName))
			{
				logger.info("session opened");
				this.selectByMap(lSession);
			}
		}
	}

	private void selectByMap(Session pSession)
	{
		logger.info("selecting all hotels from database...");
		MappingManager lMappingManager = new MappingManager(pSession);
		HotelAccessor lAccessor = lMappingManager.createAccessor(HotelAccessor.class);
		Result<Hotel> lAllHotels = lAccessor.getAll();
		for (Hotel bHotel : lAllHotels.all())
		{
			logger.info(bHotel.toString());
		}
	}
}
