package cj.software.datastax.play.logic.hotel;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ExecutionInfo;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class SimpleStatements
{
	private Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] pArgs)
	{
		SimpleStatements lInstance = new SimpleStatements();
		lInstance.doSomeThings(pArgs);
	}

	private void doSomeThings(String[] pArgs)
	{
		if (pArgs.length != 3)
		{
			throw new IllegalArgumentException("Usage: " + SimpleStatements.class.getName() + " HotelId Name Phone");
		}
		this.doSomeThings(pArgs[0], pArgs[1], pArgs[2]);
	}

	private void doSomeThings(String pHotelId, String pName, String pPhone)
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
				this.logger.info("session opened, now insert hotel id \"%s\"", pHotelId);
				this.insertHotel(lSession, pHotelId, pName, pPhone);
				this.listHotels(lSession);

				PreparedStatement lPrepInsert = lSession
						.prepare("INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?)");
				this.insertHotelPrepared(lSession, lPrepInsert, pHotelId, pName, pPhone);
				PreparedStatement lPrepSelect = lSession.prepare("SELECT * FROM hotels WHERE id = ?");
				this.listHotelsPrepared(lSession, lPrepSelect, pHotelId);

				this.insertByQueryBuilder(lCluster, lSession, pHotelId, pName);
				this.selectByQueryBuilder(lCluster, lSession, pHotelId);

				this.playWithExists(lSession);
			}
		}
	}

	private void insertHotel(Session pSession, String pHotelId, String pName, String pPhone)
	{
		String lInsert = "INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?)";
		SimpleStatement lInsertStmt = new SimpleStatement(lInsert, pHotelId, pName, pPhone);
		ResultSet lInsertRS = pSession.execute(lInsertStmt);
		this.protocolResultSet(lInsertRS, "Insert");
	}

	private void listHotels(Session pSession)
	{
		SimpleStatement lHotelSelect = new SimpleStatement("select * from hotels");
		ResultSet lRS = pSession.execute(lHotelSelect);
		this.iterate(lRS, "List");
	}

	private void insertHotelPrepared(
			Session pSession,
			PreparedStatement pStmt,
			String pHotelId,
			String pName,
			String pPhone)
	{
		String lHotelId = pHotelId + ".wxyz";
		String lName = pName + "abc";
		String lPhone = pPhone + "-123";
		this.logger.info("insert via prepared %s", lHotelId);
		BoundStatement lBound = pStmt.bind(lHotelId, lName, lPhone);
		ResultSet lBoundRS = pSession.execute(lBound);
		this.protocolResultSet(lBoundRS, "Bound");
	}

	private void listHotelsPrepared(Session pSession, PreparedStatement pStmt, String pHotelId)
	{
		BoundStatement lBound = pStmt.bind(pHotelId);
		ResultSet lRS = pSession.execute(lBound);
		this.iterate(lRS, "Prepared");
	}

	private void insertByQueryBuilder(Cluster pCluster, Session pSession, String pHotelId, String pName)
	{
		//@formatter:off
		UserType lUserType = pCluster
				.getMetadata()
				.getKeyspace("hotel")
				.getUserType("address");
		UDTValue lAddress = lUserType.newValue()
				.setString("street", "Düsselring")
				.setString("city", "Mettmann")
				.setString("postal_code","40822");
		
		BuiltStatement lInsert = QueryBuilder
				.insertInto("hotels")
				.value("id", pHotelId)
				.value("name", pName)
				.value("address", lAddress);
		//@formatter:on
		ResultSet lRS = pSession.execute(lInsert);
		this.protocolResultSet(lRS, "QueryBuilder");
	}

	private void selectByQueryBuilder(Cluster pCluster, Session pSession, String pHotelId)
	{
		//@formatter:off
		BuiltStatement lSelect = QueryBuilder
				.select()
				.all()
				.from("hotels")
				.where(eq("id", pHotelId));
		ResultSet lRS = pSession.execute(lSelect);
		//@formatter:on
		this.iterate(lRS, "QueryBuilder");
	}

	private void protocolResultSet(ResultSet pRS, String pScenario)
	{
		this.logger.info("%s: ResultSet: %s", pScenario, pRS);
		this.logger.info("%s: was applied: %s", pScenario, String.valueOf(pRS.wasApplied()));
		ExecutionInfo lExecutionInfo = pRS.getExecutionInfo();
		this.logger.info("%s: Execution Info: %s", pScenario, lExecutionInfo);
		this.logger.info("%s: Incoming Payload: %s", pScenario, lExecutionInfo.getIncomingPayload());
	}

	private void iterate(ResultSet pRS, String pScenario)
	{
		for (Row bRow : pRS.all())
		{
			String lId = bRow.getString("id");
			String lName = bRow.getString("name");
			String lPhone = bRow.getString("phone");
			UDTValue lAddress = bRow.getUDTValue("address");
			this.logger.info("%s: found hotel id %s, name \"%s\", phone %s", pScenario, lId, lName, lPhone);
			if (lAddress != null)
			{
				String lStreet = lAddress.getString("street");
				String lCity = lAddress.getString("city");
				String lStateOrProvince = lAddress.getString("state_or_province");
				String lPostalCode = lAddress.getString("postal_code");
				String lCountry = lAddress.getString("country");
				this.logger.info("%s: Hotel's address is Street %s City %s State %s ZIP-Code %s Country %s", pScenario,
						lStreet, lCity, lStateOrProvince, lPostalCode, lCountry);
			}
		}
	}

	private void playWithExists(Session pSession)
	{
		SimpleStatement lDelete = new SimpleStatement("DELETE FROM hotels WHERE ID = ?", "ABC123");
		pSession.execute(lDelete);

		SimpleStatement lInsertIfExists = new SimpleStatement(
				"INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?) IF NOT EXISTS", "ABC123",
				"Super Hotel at WestWorld", "1-123-345-567");
		ResultSet lResultSet = pSession.execute(lInsertIfExists);
		boolean lWasApplied = lResultSet.wasApplied();
		if (lWasApplied)
		{
			Row lRow = lResultSet.one();
			boolean lRowApplied = lRow.getBool("[applied]");
			this.logger.info("row applied: %s", String.valueOf(lRowApplied));
		}
		else
		{
			this.logger.info("already exists: %s", "ABC123");
		}
		lResultSet = pSession.execute(lInsertIfExists);
		lWasApplied = lResultSet.wasApplied();
		this.logger.info("2nd was applied: %s", String.valueOf(lWasApplied));
	}
}
