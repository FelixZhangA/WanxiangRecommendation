package com.wanxiang.datamodel;

import java.io.Serializable;

import android.graphics.Color;

public class Category implements Serializable
{

    private int color;
    private String name;
    private long   id;

    public Category()
    {
    	
    }
    public Category(String name)
    {
        this.name = name;
        this.color = Color.WHITE;
        this.id = System.currentTimeMillis();
    }

    public Category(String name, int color)
    {
        this.name = name;
        this.color = color;
        this.id = System.currentTimeMillis();
    }
    
    public String getCategoryName()
    {
        return name;
    }

    public void setCategoryName(String name)
    {
        this.name = name;
    }

	public int getColor()
	{
		return color;
	}

	public void setColor( int color )
	{
		this.color = color;
	}

	public long getCagetoryId()
	{
		return id;
	}

	public void setCategoryId( long id )
	{
		this.id = id;
	}
}
