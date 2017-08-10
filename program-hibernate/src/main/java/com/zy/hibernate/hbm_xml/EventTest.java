package com.zy.hibernate.hbm_xml;

import com.zy.hibernate.hbm_xml.model.Event;
import com.zy.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/8.
 */
public class EventTest {

    public static void main(String[] args) {
        saves();
        search();
    }

    private static void search() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List result = session.createQuery("from Event").list();
        for(Event event : (List<Event>)result){
            System.out.println("Event ("+event.getDate()+"):"+event.getTitle());
        }
        session.getTransaction().commit();
        session.close();
    }

    private static void saves() {
        Event event = new Event();
        event.setId(1);
        event.setDate(new Date());
        event.setTitle("测试Hibernate");

        Event event2 = new Event();
        event2.setId(2);
        event2.setDate(new Date());
        event2.setTitle("测试Hibernate2");

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(event);
        session.save(event2);
        session.getTransaction().commit();
        session.close();
        System.out.println("insert into table is ok !");
    }
}
