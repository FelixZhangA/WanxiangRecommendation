package com.wanxiang.database.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.sqlite.FinderLazyLoader;

@Table(name = "recommandation", execAfterTableCreated = "CREATE UNIQUE INDEX index_name ON recommandation(entity,category,content,date,phraise_num,comment_num)")
public class RecommandationTable extends EntityBase
{
    @Column(column = "entity")
    public String  entity;

    @Column(column = "category")
    public String category;

    @Column(column = "content")
    public String  content;

    @Column(column = "date")
    public long   date;

    @Column(column = "phraise_num")
    public int     phraise_num;

    @Column(column = "comment_num")
    public int    comment_num;

    @Column(column = "user")
    public String user;
    @Override
    public String toString()
    {
        return "Parent{" + "id=" + getId() + ", entity='" + entity + '\''
                + ", category='" + category + '\'' + ", content=" + content
                + ", user='" + user
                + ", phraise_num=" + phraise_num + ", comment_num="
                + comment_num + ", date=" + date + '}';
    }
}
