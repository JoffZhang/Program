package com.zy.hibernate.util;

import com.zy.hibernate.naming_strategies.MyImplicitNamingStrategy;
import com.zy.hibernate.naming_strategies.MyPhysicalNamingStategy;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/8.
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    //相对于3.x.x版本hibernate，我们在4.x.x采用如下方式获取我们的会话工厂：
    //1. 解析我们在hibernate.cfg.xml中的配置
//      Configuration configuration = new Configuration().configure();
    //2. 创建服务注册类,进一步注册初始化我们配置文件中的属性
//      ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
    //3. 创建我们的数据库访问会话工厂
//      SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);


    private static SessionFactory buildSessionFactory() {
        //但在5版本汇总，hibernate则采用如下新方式获取：
        //1. 配置类型安全的准服务注册类，这是当前应用的单例对象，不作修改，所以声明为final
        //在configure("cfg/hibernate.cfg.xml")方法中，如果不指定资源路径，默认在类路径下寻找名为hibernate.cfg.xml的文件
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("com/zy/hibernate/hibernate.cfg.xml").build();
        //2. 根据服务注册类创建一个元数据资源集，同时构建元数据并生成应用一般唯一的的session工厂
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }


    private static final SessionFactory sessionFactoryWithConfig = buildSessionFactory2();

    private static SessionFactory buildSessionFactory2() {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("com/zy/hibernate/hibernate.cfg.xml").build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        MetadataBuilder builder = metadataSources.getMetadataBuilder();
        builder.applyImplicitNamingStrategy(new MyImplicitNamingStrategy());
        builder.applyPhysicalNamingStrategy(new MyPhysicalNamingStategy());
        SessionFactory sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
                //= builder.build().buildSessionFactory();
        return sessionFactory;
    }
    public static SessionFactory getSessionFactoryWithConfig(){
        return sessionFactoryWithConfig;
    }
}
