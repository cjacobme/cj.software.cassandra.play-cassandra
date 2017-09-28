package cj.software.datastax.play.logic.movie;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import cj.software.datastax.play.domain.movie.Movie;

@Accessor
public interface MovieAccessor
{
	@Query("SELECT * FROM movie")
	public abstract Result<Movie> listMovies();
}
