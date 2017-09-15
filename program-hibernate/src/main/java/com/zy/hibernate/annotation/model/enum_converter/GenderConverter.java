package com.zy.hibernate.annotation.model.enum_converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Author : ZhangYun
 * @Description : 定制一个转换器，可将给定的JDBC类型映射到一个实体的基本类型
 * @Date :  2017/8/14.
 */
@Converter
public class GenderConverter implements AttributeConverter<Gender,Character>{

    //转换成数据库字段
    @Override
    public Character convertToDatabaseColumn(Gender gender) {
        if(gender == null) return null;

        return gender.getCode();
    }
    //转换成属性
    @Override
    public Gender convertToEntityAttribute(Character character) {
        if(character == null) return null;

        return Gender.fromCode(character);
    }
}
