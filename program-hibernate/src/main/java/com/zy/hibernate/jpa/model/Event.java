package com.zy.hibernate.jpa.model;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author : ZhangYun
 * @Description :
 @Audited 标记该Entity类或属性支持数据修改记录支持。 Target(value={java.lang.annotation.ElementType.TYPE,java.lang.annotation.ElementType.METHOD,java.lang.annotation.ElementType.FIELD}
 @NotAudited 标记该属性不支持数据修改记录支持 Target(value={java.lang.annotation.ElementType.METHOD,java.lang.annotation.ElementType.FIELD})


 @RevisionEntity 实现为数据修改记录表保存其它自定义内容实现。如修改时间，操作人等。该类必须是一个实体类，会将数据存放一个单独表中。
 @RevisionTimestamp    记录修改时间 必须配合 @RevisionEntity使用
 @RevisionNumber 修改记录表的版本id 通常是配置成主键

 * @Date :  2017/8/9.
 */
@Audited
@Entity
@Table(name = "EVENTS")
public class Event {
    private int id;
    private Date date;
    private String title;

    @Id
    @Column(name = "EVENT_ID")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy = "increment")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotAudited
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EVENT_DATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
