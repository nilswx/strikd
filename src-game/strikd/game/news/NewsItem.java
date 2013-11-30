package strikd.game.news;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name="news")
public class NewsItem
{
	@Id
	private int id;
	private String headline;
	private String body;
	private String imageUrl;
	private Date timestamp;

	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
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
	
	public String getImageUrl()
	{
		return this.imageUrl;
	}
	
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}
	
	public Date getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}
}
