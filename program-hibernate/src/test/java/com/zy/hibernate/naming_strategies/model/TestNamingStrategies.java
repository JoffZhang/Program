package com.zy.hibernate.naming_strategies.model;

import com.zy.hibernate.util.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/10.
 */
public class TestNamingStrategies {

    private static SessionFactory sessionFactory;

    @Before
    public void before(){
        sessionFactory = HibernateUtil.getSessionFactoryWithConfig();
    }

    @After
    public void after(){
        sessionFactory.close();
    }

    @Test
    public void test(){
        String[] testTable1Been = StringUtils.splitByCharacterTypeCamelCase("TestTable1Bean");
        System.out.println("commons-long : StringUtils.splitByCharacterTypeCamelCase(string):驼峰式拆分(驼峰，类型)");
        System.out.println("StringUtils.splitByCharacterTypeCamelCase:  "+Arrays.asList(testTable1Been));


        String[] testTable1Been1 = StringUtils.splitByCharacterType("TestTable1Bean");
        System.out.println("commons-long : StringUtils.splitByCharacterType(string):根据类型拆分（大小写，类型）");
        System.out.println("StringUtils.splitByCharacterType: "+ Arrays.asList(testTable1Been1).toString());
    }

}
