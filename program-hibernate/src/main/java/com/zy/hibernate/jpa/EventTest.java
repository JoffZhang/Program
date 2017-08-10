package com.zy.hibernate.jpa;

import com.zy.hibernate.jpa.model.Event;
import junit.framework.TestCase;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/9.
 */
public class EventTest extends TestCase {

    private EntityManagerFactory entityManagerFactory;

    @Override
    protected void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("com.zy.hibernate.jpa");
    }

    @Override
    protected void tearDown() throws Exception {
        entityManagerFactory.close();
    }


    public void testBasicUsage() {
        Event event = new Event();
        event.setDate(new Date());
        event.setTitle("测试Hibernate jpa");

        Event event2 = new Event();
        event2.setDate(new Date());
        event2.setTitle("测试Hibernate jpa2");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(event);
        entityManager.persist(event2);
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Event> result = entityManager.createQuery("from Event", Event.class).getResultList();
        for (Event e : result) {
            System.out.println("Event (" + e.getDate() + ") : " + e.getTitle());
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        //Tutorial Using Envers 通过此事件可查看任意修改的版本数据
        entityManager = entityManagerFactory.createEntityManager();
        AuditReader reader = AuditReaderFactory.get(entityManager);
        Event firstRevision = reader.find(Event.class, 2, 1);
        System.out.println("firstRevision (" + firstRevision.getDate() + ") : " + firstRevision.getTitle());
        Event secondRevision = reader.find(Event.class, 2, 2);
        System.out.println("secondRevision (" + secondRevision.getDate() + ") : " + secondRevision.getTitle());
    }
}
