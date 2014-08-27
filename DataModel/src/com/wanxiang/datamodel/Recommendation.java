package com.wanxiang.datamodel;

import java.io.Serializable;
import java.util.ArrayList;

public class Recommendation implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Entity entity;
    private Category category;
    private User user;
    private long date;
    private String content;
    private ArrayList<Comment> comments;
    private long id;
    private int phraise_num = 0;
    private int comment_num = 0;

    public Recommendation()
    {
    	// For cursor transfer use;
    }
    
    public Recommendation(String entityName, String categoryName, String userName, long date, String content, int phraise, int comment)
    {
        Entity entity = new Entity(entityName);
        Category category = new Category(categoryName);
        User user = new User(userName);
        init(entity, category, user, date, content, phraise, comment);
    }
    private void init(Entity entity, Category category, User user, long date, String content, int phraise, int comment)
    {
        this.setCategory(category);
        this.entity = entity;
        this.user = user;
        this.date = date;
        this.content = content;
        this.comment_num = comment;
        this.phraise_num = phraise;
    }
    
    public void setID(long value)
    {
        id = value;
    }
    public long getID()
    {
        return id;
    }
        
    public Entity getEntity()
    {
        return entity;
    }
    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }
    public User getUser()
    {
        return user;
    }
    public void setUser(User user)
    {
        this.user = user;
    }
    public long getDate()
    {
        return date;
    }
    public void setDate(long date)
    {
        this.date = date;
    }
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }
    public Category getCategory()
    {
        return category;
    }
    public void setCategory(Category category)
    {
        this.category = category;
    }
	public ArrayList<Comment> getComments() 
	{
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) 
	{
		this.comments = comments;
	}
	
	public int getPhraiseNum()
	{
		return phraise_num;
	}
    
    public void setPhraiseNum(int num)
    {
    	this.phraise_num = num;
    }
    
	public int getCommentNum()
	{
		return comment_num;
	}
    
    public void setCommentNum(int num)
    {
    	this.comment_num = num;
    }
}
