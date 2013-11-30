package strikd.game.stream;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity @Table(name="news")
public class NewsItem
{
	private Date timestamp;
	private String imageUrl;
	private String headline;
	private String body;
	
	public Date getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public String getImageUrl()
	{
		return this.imageUrl;
	}
	
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}
	
	public String getHeadline()
	{
		return this.headline;
	}
	
	public void setHeadline(String headline)
	{
		this.headline = headline;
	}
	
	public String getBody()
	{
		return this.body;
	}
	
	public void setBody(String body)
	{
		this.body = body;
	}
}
