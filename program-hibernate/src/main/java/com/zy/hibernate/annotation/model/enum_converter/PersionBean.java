package com.zy.hibernate.annotation.model.enum_converter;



import org.hibernate.annotations.Type;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/14.
 */
@Entity(name="Persion")
public class PersionBean {

    @Id
    private Long id;

    private String name;
    /**
     * JPA不允许适用AttributeConverter属性标记为@Enumerated   如果使用AttributeConverter方法，一定不要标记属性@Enumerated
     */
    @Convert(converter = GenderConverter.class)
    public Gender gender;

    @Type(type = "com.zy.hibernate.annotation.model.enum_converter.GenderConverter")
    public Gender2 gender2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
