package com.zy.hibernate.annotation.model.enum_converter;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/14.
 */
public enum Gender2 {

    MALE('M'),
    FEMALE('F');

    private final char code;

    Gender2(char code) {
        this.code = code;
    }

    public static Gender2 fromCode(char code){
        if(code == 'M' || code == 'm'){
            return MALE;
        }
        if(code == 'F' || code == 'f'){
            return FEMALE;
        }
        throw new UnsupportedOperationException(
                "The code "+code +" is not supperted"
        );
    }

    public char getCode(){
        return code;
    }
}
