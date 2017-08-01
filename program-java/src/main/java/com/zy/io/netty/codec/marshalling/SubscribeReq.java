package com.zy.io.netty.codec.marshalling;

import java.io.Serializable;
import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/26.
 */
public class SubscribeReq implements Serializable{
    private static final long serialVersionUID = 4522435368062921846L;
    private int subReqID;
    private String userName;
    private String productName;
    private List<String> address;

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SubscribeReq{" +
                "subReqID=" + subReqID +
                ", userName='" + userName + '\'' +
                ", productName='" + productName + '\'' +
                ", address=" + address +
                '}';
    }
}
