package com.zy.hibernate.annotation.model.generated;

import com.sun.xml.internal.bind.v2.TODO;
import com.zy.hibernate.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.persistence.EntityManager;
/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/14.
 */
public class GeneratedTest {
    private static SessionFactory sessionFactory;
    @Before
    public void before(){
        sessionFactory = HibernateUtil.getSessionFactory();
    }
    @Test
    public void testGenerated(){
        /**
         * @TODO    待研究 查询不成功，插入不成功？
         */
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person();
        person.setId( 1L );
        person.setFirstName( "John" );
        person.setMiddleName1( "Flávio" );
        person.setMiddleName2( "André" );
        person.setMiddleName3( "Frederico" );
        person.setMiddleName4( "Rúben" );
        person.setMiddleName5( "Artur" );
        person.setLastName( "Doe" );

        entityManager.persist( person );
        entityManager.flush();
        entityManager.getTransaction().commit();

        assertEquals("Joff Zhang",person.getFullName());
    }
}
