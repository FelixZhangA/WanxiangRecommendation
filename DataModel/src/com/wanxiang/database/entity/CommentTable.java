package com.wanxiang.database.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.wanxiang.datamodel.User;

public class CommentTable extends EntityBase
{
    @Column(column = "recommendation_id")
    public long r_id;
    
    @Column(column = "user")
    public String user;
    
    @Column(column = "content")
    public String content;
    
    @Column(column = "date")
    public long date;
    
    @Override
    public String toString()
    {
        return "Parent{" + "id=" + getId() + ", user='" + user + '\''
                + ", content='" + content + '\''
                + ", recommendation_id='" + r_id + '\''
                + ", date=" + date + '}';
    }
}
