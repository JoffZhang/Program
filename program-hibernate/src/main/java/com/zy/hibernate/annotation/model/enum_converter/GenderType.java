package com.zy.hibernate.annotation.model.enum_converter;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.CharacterTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.CharTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/14.
 */
public class GenderType  extends AbstractSingleColumnStandardBasicType<Gender2>{
    public static final GenderType INSTANCE = new GenderType();
    private static final long serialVersionUID = -4556856508160407627L;

    public GenderType() {
        super(
                CharTypeDescriptor.INSTANCE,
                GenderJavaTypeDescriptor.INSTANCE
        );
    }

    @Override
    public String getName() {
        return "gender";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

}
