package cj.software.datastax.play.hotel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ExecutionInfo;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

public class SimpleStatements
{
	private Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] pArgs)
	{
		SimpleStatements lInstance = new SimpleStatements();
		lInstance.insertHotel(pArgs);
	}

	private void insertHotel(String[] pArgs)
	{
		if (pArgs.length != 3)
		{
			throw new IllegalArgumentException("Usage: " + SimpleStatements.class.getName() + " HotelId Name Phone");
		}
		this.insertHotel(pArgs[0], pArgs[1], pArgs[2]);
	}

	private void insertHotel(String pHotelId, String pName, String pPhone)
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
				logger.info("session opened, now insert hotel id \"%s\"", pHotelId);
				this.insertHotel(lSession, pHotelId, pName, pPhone);
				this.listHotels(lSession);
			}
		}
	}

	private void insertHotel(Session pSession, String pHotelId, String pName, String pPhone)
	{
		String lInsert = "INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?)";
		SimpleStatement lInsertStmt = new SimpleStatement(lInsert, pHotelId, pName, pPhone);
		ResultSet lInsertRS = pSession.execute(lInsertStmt);
		logger.info("Insert ResultSet: %s", lInsertRS);
		logger.info("Insert was applied: %s", String.valueOf(lInsertRS.wasApplied()));
		ExecutionInfo lExecutionInfo = lInsertRS.getExecutionInfo();
		logger.info("Execution Info: %s", lExecutionInfo);
		logger.info("Incoming Payload: %s", lExecutionInfo.getIncomingPayload());
	}

	private void listHotels(Session pSession)
	{
		SimpleStatement lHotelSelect = new SimpleStatement("select * from hotels");
		ResultSet lRS = pSession.execute(lHotelSelect);
		for (Row bRow : lRS.all())
		{
			String lId = bRow.getString("id");
			String lName = bRow.getString("name");
			String lPhone = bRow.getString("phone");
			logger.info("found hotel id %s, name \"%s\", phone %s", lId, lName, lPhone);
		}
	}
}
