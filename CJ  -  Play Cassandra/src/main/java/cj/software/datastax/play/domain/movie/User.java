package cj.software.datastax.play.domain.movie;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "user")
public class User implements Serializable
{
	private static final long serialVersionUID = 1L;

	@PartitionKey(0)
	private UUID id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "birthday", codec = LocalDateCodec.class)
	private LocalDate birthday;

	public UUID getId()
	{
		return this.id;
	}

	public void setId(UUID pId)
	{
		this.id = pId;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(String pFirstName)
	{
		this.firstName = pFirstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String pLastName)
	{
		this.lastName = pLastName;
	}

	public LocalDate getBirthday()
	{
		return this.birthday;
	}

	public void setBirthday(LocalDate pBirthday)
	{
		this.birthday = pBirthday;
	}

	@Override
	public boolean equals(Object pOther)
	{
		boolean lResult;

		if (pOther instanceof User)
		{
			User lOther = (User) pOther;
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
	public int hashCode()
	{
		HashCodeBuilder lBuilder = new HashCodeBuilder().append(this.id);
		int lResult = lBuilder.toHashCode();
		return lResult;
	}

	@Override
	public String toString()
	{
		//@formatter:off
		ToStringBuilder lBuilder = new ToStringBuilder(this)
				.append(this.id)
				.append("first Name", this.firstName)
				.append("last Name", this.lastName)
				.append("birthday", this.birthday);
		//@formatter:on
		String lResult = lBuilder.toString();
		return lResult;
	}
}
