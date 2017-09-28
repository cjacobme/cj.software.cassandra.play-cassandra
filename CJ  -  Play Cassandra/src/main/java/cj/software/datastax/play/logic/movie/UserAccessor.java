package cj.software.datastax.play.logic.movie;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import cj.software.datastax.play.domain.movie.User;

@Accessor
public interface UserAccessor
{
	@Query("select * from user")
	public Result<User> listUsers();
}
