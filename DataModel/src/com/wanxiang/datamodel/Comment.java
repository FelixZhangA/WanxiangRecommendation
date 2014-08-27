package com.wanxiang.datamodel;


public class Comment
{
    private User user;
    private String content;
    private long time;
    private long rId;
    
	public Comment(long recommendationId, String user, String content, long currentTimeMillis) 
	{
		this.rId = recommendationId;
		this.user = new User(user);
		this.content = content;
		this.time = currentTimeMillis;
	}
	public User getCommentUser() 
	{
		return user;
	}
	public void setCommentUser(User user) 
	{
		this.user = user;
	}
	public String getCommentContent()
	{
		return content;
	}
	public void setCommentContent(String content) 
	{
		this.content = content;
	}
	public long getCommentDate() 
	{
		return time;
	}
	public void setCommentDate(long time)
	{
		this.time = time;
	}
	public long getRecommendationId()
	{
		return rId;
	}
	public void setRecommendationId( long rId )
	{
		this.rId = rId;
	}
}
