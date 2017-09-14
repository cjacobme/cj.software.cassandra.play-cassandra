package cj.software.datastax.play.domain.hotel;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;

@UDT(name = "address")
public class Address
{
	// CREATE TYPE hotel.address (
	// street text,
	// city text,
	// state_or_province text,
	// postal_code text,
	// country text
	// );

	private String street;

	private String city;

	@Field(name = "state_or_province")
	private String stateOrProvince;

	private String country;

	public String getStreet()
	{
		return this.street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getCity()
	{
		return this.city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getStateOrProvince()
	{
		return this.stateOrProvince;
	}

	public void setStateOrProvince(String stateOrProvince)
	{
		this.stateOrProvince = stateOrProvince;
	}

	public String getCountry()
	{
		return this.country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	@Override
	public String toString()
	{
		StringBuilder lSB = new StringBuilder();
		if (this.street != null)
		{
			lSB.append(" street:").append(this.street);
		}
		if (this.city != null)
		{
			lSB.append(" city:").append(this.city);
		}
		if (this.stateOrProvince != null)
		{
			lSB.append(" State/Province:").append(this.stateOrProvince);
		}
		if (this.country != null)
		{
			lSB.append(" country:").append(this.country);
		}
		return lSB.toString();
	}
}
