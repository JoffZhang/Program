package com.zy.hibernate.annotation.model;

import com.zy.hibernate.annotation.model.enum_converter.PhoneType;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/11.
 */

// @Entity 注解将一个类声明为实体 Bean
@Entity
//@Table 为实体Bean指定对应数据库表，目录和schema的名字。@Table 注解包含一个schema和一个catelog 属性，使用@UniqueConstraints 可以定义表的唯一约束。
@Table(name = "tbl_test")
public class AnnotationBean {
    @Id
    private long id;
    /**
     * @Basic ： 不写的时候默认注解 ，字段与属性名相同
     */
    @Basic
    private String declaredName;//显式的声明
    private String  implicitlyName;//隐式的声明
    /**
     * @Column ：明确列命名
     */
    @Column(name = "tel_phone")
    private String telPhone;
    /**
     * 时间映射
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    /**
     * @Transient   ：   不需要持久化到数据库
     */
    @Transient
    private String yourName;
    /**
     * @Enumerated ：枚举映射
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(name="phone_type")
    private PhoneType type;

}
