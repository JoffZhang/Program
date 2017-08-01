package com.zy.io.netty.multi_protocol.http_xml.jibx.pojo;

import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/28.
 */
public class Customer {

    private long customerNumber;
    private String firstName;

    private String lastName;

    private List<String> middleNames;

    public long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(List<String> middleNames) {
        this.middleNames = middleNames;
    }
}
