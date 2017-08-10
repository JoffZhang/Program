package com.zy.hibernate.hbm_xml.model;

import java.util.Date;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/8.
 */
public class Event {
    private int id;
    private Date date;
    private String title;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
