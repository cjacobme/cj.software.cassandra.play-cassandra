package cj.software.datastax.play.logic.movie;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

import cj.software.datastax.play.domain.movie.Movie;
import cj.software.datastax.play.domain.movie.User;

public class SelectBasics
{
	private Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] pArgs)
	{
		try
		{
			SelectBasics lInstance = new SelectBasics();
			lInstance.dumpMovies();
		}
		catch (Throwable pThrowable)
		{
			pThrowable.printStackTrace(System.err);
		}
	}

	private void dumpMovies()
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
				List<Movie> lMovies = this.selectMovies(lSession);
				for (Movie bMovie : lMovies)
				{
					this.logger.info(bMovie);
				}
				List<User> lUsers = this.selectUsers(lSession);
				for (User bUser : lUsers)
				{
					this.logger.info(bUser);
				}
			}
		}
	}

	public List<Movie> selectMovies(Session pSession)
	{
		this.logger.info("now start to select all movies");
		MappingManager lMappingManager = new MappingManager(pSession);
		MovieAccessor lMovieAccessor = lMappingManager.createAccessor(MovieAccessor.class);
		Result<Movie> lList = lMovieAccessor.listMovies();
		List<Movie> lResult = new ArrayList<>();
		lList.forEach(m -> lResult.add(m));
		return lResult;
	}

	public List<User> selectUsers(Session pSession)
	{
		this.logger.info("now start to select all users");
		MappingManager lMappingManager = new MappingManager(pSession);
		UserAccessor lUserAccessor = lMappingManager.createAccessor(UserAccessor.class);
		Result<User> lList = lUserAccessor.listUsers();
		List<User> lResult = new ArrayList<>();
		lList.forEach(pUser -> lResult.add(pUser));
		return lResult;
	}
}
