package cj.software.datastax.play.domain.weather;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.datastax.driver.core.LocalDate;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "temperature_by_day")
public class TemperatureByDay implements Serializable
{
	private static final long serialVersionUID = 1L;

	@PartitionKey(0)
	@Column(name = "weatherstation_id")
	private String weatherstationId;

	@PartitionKey(1)
	private LocalDate date;

	@ClusteringColumn
	@Column(name = "event_time")
	private Date eventTime;

	private String temperature;

	public String getWeatherstationId()
	{
		return this.weatherstationId;
	}

	public void setWeatherstationId(String pWeatherstationId)
	{
		this.weatherstationId = pWeatherstationId;
	}

	public LocalDate getDate()
	{
		return this.date;
	}

	public void setDate(LocalDate pDate)
	{
		this.date = pDate;
	}

	public Date getEventTime()
	{
		return this.eventTime;
	}

	public void setEventTime(Date pEventTime)
	{
		this.eventTime = pEventTime;
	}

	public String getTemperature()
	{
		return this.temperature;
	}

	public void setTemperature(String pTemperature)
	{
		this.temperature = pTemperature;
	}

	@Override
	public int hashCode()
	{
		//@formatter:off
		HashCodeBuilder lBuilder = new HashCodeBuilder()
				.append(this.weatherstationId)
				.append(this.eventTime);
		//@formatter:on
		int lResult = lBuilder.toHashCode();
		return lResult;
	}

	@Override
	public boolean equals(Object pOther)
	{
		boolean lResult;
		if (pOther instanceof TemperatureByDay)
		{
			TemperatureByDay lOther = (TemperatureByDay) pOther;
			//@formatter:off
			EqualsBuilder lBuilder = new EqualsBuilder()
					.append(this.weatherstationId, lOther.weatherstationId)
					.append(this.eventTime, lOther.eventTime);
			//@formatter:on
			lResult = lBuilder.isEquals();
		}
		else
		{
			lResult = false;
		}
		return lResult;
	}

	@Override
	public String toString()
	{
		//@formatter:off
		ToStringBuilder lBuilder = new ToStringBuilder(this)
				.append("station-id", this.weatherstationId)
				.append("event-time", this.eventTime)
				.append("temp", this.temperature);
		//@formatter.on
		String lResult = lBuilder.toString();
		return lResult;
	}
	
}
