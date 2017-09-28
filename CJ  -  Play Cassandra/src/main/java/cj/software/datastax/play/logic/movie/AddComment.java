package cj.software.datastax.play.logic.movie;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.utils.UUIDs;

import cj.software.datastax.play.domain.movie.Movie;
import cj.software.datastax.play.domain.movie.User;

public class AddComment
{
	private Logger logger = LogManager.getFormatterLogger();

	public static void main(String[] pArgs)
	{
		try
		{
			AddComment lInstance = new AddComment();
			lInstance.addComment();
		}
		catch (Throwable pException)
		{
			pException.printStackTrace(System.err);
		}
	}

	private void addComment()
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
				SelectBasics lSelBasics = new SelectBasics();
				List<Movie> lMovies = lSelBasics.selectMovies(lSession);
				List<User> lUsers = lSelBasics.selectUsers(lSession);
				for (Movie bMovie : lMovies)
				{
					for (User bUser : lUsers)
					{
						String lLongText = "this is a long and futile comment of user " + bUser.getFirstName() + " "
								+ bUser.getLastName() + " on movie " + bMovie.getTitle();
						this.addComment(lSession, bUser, bMovie, "Comment " + OffsetDateTime.now(), lLongText);
					}
				}
			}
		}
	}

	public void addComment(Session pSession, User pUser, Movie pMovie, String pCommentTitle, String pLongText)
	{
		UUID lUUID = UUIDs.timeBased();
		BatchStatement lBatchStatement = new BatchStatement();
		lBatchStatement.addAll(this.addComment(pSession, lUUID, pUser, pCommentTitle, pLongText));
		lBatchStatement.addAll(this.addComment(pSession, lUUID, pMovie, pCommentTitle, pLongText));
		pSession.execute(lBatchStatement);
		this.logger.info("Comment of %s %s on %s issued", pUser.getFirstName(), pUser.getLastName(), pMovie.getTitle());
	}

	private Iterable<Statement> addComment(
			Session pSession,
			UUID pCommentId,
			User pUser,
			String pCommentTitle,
			String pLongText)
	{
		List<Statement> lResult = new ArrayList<>();
		String lCQL = String.format(
				"INSERT INTO comments_by_user "
						+ "(userid, comment_id, birthday, first_name, last_name, comment_title, longtext)"
						+ " VALUES (%s, %s, '%s', '%s', '%s', '%s', '%s')",
				pUser.getId(), pCommentId, pUser.getBirthday(), pUser.getFirstName(), pUser.getLastName(),
				pCommentTitle, pLongText);
		Statement lStmt = new SimpleStatement(lCQL);
		lResult.add(lStmt);
		return lResult;
	}

	private Iterable<Statement> addComment(
			Session pSession,
			UUID pCommentUUID,
			Movie pMovie,
			String pCommentTitle,
			String pLongText)
	{
		List<Statement> lResult = new ArrayList<>();
		String lCQL = String.format(
				"INSERT INTO comments_by_movie "
						+ "(movie_id, comment_id, director, movie_title, year_of_publish, comment_title, longtext) "
						+ "VALUES (%s, %s, '%s', '%s', %d, '%s', '%s')",
				pMovie.getId(), pCommentUUID, pMovie.getDirector(), pMovie.getTitle(), pMovie.getYearPublished(),
				pCommentTitle, pLongText);
		Statement lStmt = new SimpleStatement(lCQL);
		lResult.add(lStmt);
		return lResult;
	}
}
