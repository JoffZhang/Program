package com.zy.hibernate.annotation.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/9.
 */
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
