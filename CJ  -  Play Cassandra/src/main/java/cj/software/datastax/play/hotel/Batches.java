package cj.software.datastax.play.hotel;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ExecutionInfo;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

public class Batches
{
	private Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] pArgs)
	{
		Batches lInstance = new Batches();
		lInstance.playSomeThings();
	}

	private void playSomeThings()
	{
		String lHostname = System.getProperty("host");
		if (lHostname == null)
		{
			throw new IllegalStateException("System Property \"host\" not set");
		}

		this.logger.info("connecting to %s...", lHostname);
		try (Cluster lCluster = Cluster.builder().addContactPoint(lHostname).build())
		{
			String lKeyspaceName = "hotel";
			this.logger.info("connected! now open session on keyspace %s", lKeyspaceName);
			try (Session lSession = lCluster.connect(lKeyspaceName))
			{
				this.logger.info("session opened, now start to play around");
				this.insertHotelAndPoi(lSession, "40822", "Luisenhof", "02104-123454321", "Neanderthalmuseum");
			}
		}
	}

	private void insertHotelAndPoi(
			Session pSession,
			String pHotelId,
			String pHotelName,
			String pHotelPhone,
			String pPoiName)
	{
		//@formatter:off
		SimpleStatement lInsertHotel = new SimpleStatement(
				"INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?)",
				pHotelId, pHotelName, pHotelPhone);
		SimpleStatement lInsertHotelPoi = new SimpleStatement(
				"INSERT INTO hotels_by_poi (poi_name, hotel_id, name, phone) VALUES (?, ?, ?, ?)",
				pPoiName, pHotelId, pHotelName, pHotelPhone);
		//@formatter:on
		BatchStatement lHotelBatch = new BatchStatement();
		lHotelBatch.add(lInsertHotel);
		lHotelBatch.add(lInsertHotelPoi);
		this.logger.info("INSERT HOTEL (%s, %s, %s) AND ASSIGN TO POI %s...", pHotelId, pHotelName, pHotelPhone,
				pPoiName);
		ResultSet lResultSet = pSession.execute(lHotelBatch);
		List<ExecutionInfo> lExecutionInfos = lResultSet.getAllExecutionInfo();
		for (ExecutionInfo bExecutionInfo : lExecutionInfos)
		{
			this.logger.info("insertHotelAndPoi: Execution Info %s", bExecutionInfo);
		}
	}
}
