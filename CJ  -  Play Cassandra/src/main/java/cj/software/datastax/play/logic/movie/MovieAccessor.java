package cj.software.datastax.play.logic.movie;

import java.util.UUID;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

import cj.software.datastax.play.domain.movie.Movie;

@Accessor
public interface MovieAccessor
{
	@Query("SELECT * FROM movie")
	public abstract Result<Movie> listMovies();

	@Query("INSERT INTO movie (id, director, title, year_of_publish) VALUES (:id, :director, :title, :yearOfPublish)")
	public abstract void addMovie(
			@Param("id") UUID pUUID,
			@Param("director") String pDirector,
			@Param("title") String pTitle,
			@Param("yearOfPublish") int pPublishedYear);
}
