package cj.software.datastax.play.domain.movie;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "movie")
public class Movie implements Serializable
{
	private static final long serialVersionUID = 1L;

	@PartitionKey(0)
	private UUID id;

	@Column
	private String title;

	private String director;

	@Column(name = "year_of_publish")
	private int yearPublished;

	public UUID getId()
	{
		return this.id;
	}

	public void setId(UUID pId)
	{
		this.id = pId;
	}

	public String getTitle()
	{
		return this.title;
	}

	public void setTitle(String pTitle)
	{
		this.title = pTitle;
	}

	public String getDirector()
	{
		return this.director;
	}

	public void setDirector(String pDirector)
	{
		this.director = pDirector;
	}

	public int getYearPublished()
	{
		return this.yearPublished;
	}

	public void setYearPublished(int pYearPublished)
	{
		this.yearPublished = pYearPublished;
	}

	@Override
	public int hashCode()
	{
		HashCodeBuilder lBuilder = new HashCodeBuilder().append(this.id);
		int lResult = lBuilder.toHashCode();
		return lResult;
	}

	@Override
	public boolean equals(Object pOther)
	{
		boolean lResult;
		if (pOther instanceof Movie)
		{
			Movie lOther = (Movie) pOther;
			EqualsBuilder lBuilder = new EqualsBuilder().append(this.id, lOther.id);
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
				.append(this.id)
				.append("title", this.title)
				.append("director", this.director)
				.append("published", this.yearPublished)
				;
		//@formatter:on
		String lResult = lBuilder.toString();
		return lResult;
	}
}
