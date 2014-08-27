package com.wanxiang.datamodel;

import java.io.Serializable;

public class Entity implements Serializable
{
    private String name;
    
    public Entity(String name)
    {
        this.setEntityName(name);
    }

    public String getEntityName()
    {
        return name;
    }

    public void setEntityName(String name)
    {
        this.name = name;
    }
}

