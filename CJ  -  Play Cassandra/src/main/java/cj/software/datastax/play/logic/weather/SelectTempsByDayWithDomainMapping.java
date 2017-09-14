package cj.software.datastax.play.logic.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

import cj.software.datastax.play.domain.weather.TemperatureByDay;

public class SelectTempsByDayWithDomainMapping
{
	private Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] pArgs)
	{
		SelectTempsByDayWithDomainMapping lInstance = new SelectTempsByDayWithDomainMapping();
		lInstance.selectTemperatures();
	}

	public void selectTemperatures()
	{
		String lHostname = System.getProperty("host");
		if (lHostname == null)
		{
			throw new IllegalStateException("System Property \"host\" not set");
		}

		String lKeyspaceName = System.getProperty("keyspace");
		if (lKeyspaceName == null)
		{
			throw new IllegalStateException("System Property \"keyspace\" not set");
		}

		this.logger.info("now connect to %s...", lHostname);

		try (Cluster lCluster = Cluster.builder().addContactPoint(lHostname).build())
		{
			this.logger.info("connected!");
			this.logger.info("now open session on keyspace \"%s\"", lKeyspaceName);
			try (Session lSession = lCluster.connect(lKeyspaceName))
			{
				this.logger.info("session opened");
				this.selectTemperatures(lSession);
			}
		}
	}

	public void selectTemperatures(Session pSession)
	{
		this.logger.info("now select all temperatures by day...");
		MappingManager lMappingManager = new MappingManager(pSession);
		WeatherAccessor lWeatherAccessor = lMappingManager.createAccessor(WeatherAccessor.class);
		Result<TemperatureByDay> lList = lWeatherAccessor.listTemperaturesByDay();
		for (TemperatureByDay bTemp : lList)
		{
			this.logger.info(bTemp.toString());
		}
	}
}
