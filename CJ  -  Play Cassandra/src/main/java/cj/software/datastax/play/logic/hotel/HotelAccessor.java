package cj.software.datastax.play.logic.hotel;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import cj.software.datastax.play.domain.hotel.Hotel;

@Accessor
public interface HotelAccessor
{
	@Query("SELECT * FROM hotels")
	public Result<Hotel> getAll();
}
