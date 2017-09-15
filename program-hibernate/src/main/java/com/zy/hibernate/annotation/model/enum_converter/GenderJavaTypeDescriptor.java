package com.zy.hibernate.annotation.model.enum_converter;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.CharacterTypeDescriptor;

public class GenderJavaTypeDescriptor extends AbstractTypeDescriptor<Gender2> {
    private static final long serialVersionUID = -3399546684797674071L;

    public static final GenderJavaTypeDescriptor INSTANCE = new GenderJavaTypeDescriptor();

    protected GenderJavaTypeDescriptor() {
        super(Gender2.class);
    }

    @Override
    public String toString(Gender2 gender) {
        return gender == null ? null : gender.name();
    }

    @Override
    public Gender2 fromString(String s) {
        return s == null ? null : Gender2.valueOf(s);
    }

    @Override
    public <X> X unwrap(Gender2 gender, Class<X> type, WrapperOptions wrapperOptions) {
        return CharacterTypeDescriptor.INSTANCE.unwrap(
                gender == null ? null : gender.getCode(),
                type,
                wrapperOptions
        );
    }

    @Override
    public <X> Gender2 wrap(X value, WrapperOptions wrapperOptions) {
        return Gender2.fromCode(
                CharacterTypeDescriptor.INSTANCE.wrap(value,wrapperOptions)
        );
    }
}
