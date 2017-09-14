package cj.software.datastax.play.logic.weather;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import cj.software.datastax.play.domain.weather.TemperatureByDay;

@Accessor
public interface WeatherAccessor
{
	@Query("SELECT * FROM temperature_by_day")
	public abstract Result<TemperatureByDay> listTemperaturesByDay();
}
