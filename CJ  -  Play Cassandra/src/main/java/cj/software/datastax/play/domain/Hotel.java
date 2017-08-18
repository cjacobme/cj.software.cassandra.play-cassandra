package cj.software.datastax.play.domain;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "hotel", name = "hotels")
public class Hotel
{
	@PartitionKey
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String phone;

	private Address address;

	@Column(name = "pois")
	private Set<String> pointsOfInterests;

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPhone()
	{
		return this.phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Address getAddress()
	{
		return this.address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public Set<String> getPointsOfInterests()
	{
		return Collections.unmodifiableSet(this.pointsOfInterests);
	}

	public void setPointsOfInterests(Set<String> pointsOfInterests)
	{
		this.pointsOfInterests = pointsOfInterests;
	}

	@Override
	public String toString()
	{
		//@formatter:off
		ToStringBuilder lBuilder = new ToStringBuilder(this)
				.append("id", this.id)
				.append("name", this.name)
				.append("address", this.address)
				.append("phone", this.phone);
		//@formatter:on
		String lResult = lBuilder.toString();
		return lResult;
	}

	@Override
	public boolean equals(Object pOther)
	{
		boolean lResult;
		if (pOther instanceof Hotel)
		{
			Hotel lOther = (Hotel) pOther;
			EqualsBuilder lBuilder = new EqualsBuilder().append(this.id, lOther.id);
			lResult = lBuilder.isEquals();
		} else
		{
			lResult = false;
		}
		return lResult;
	}

	@Override
	public int hashCode()
	{
		HashCodeBuilder lBuilder = new HashCodeBuilder().append(this.id);
		int lResult = lBuilder.toHashCode();
		return lResult;
	}
}
