package com.zy.io.netty.multi_protocol.http_xml.jibx;

import com.zy.io.netty.multi_protocol.http_xml.jibx.pojo.Address;
import com.zy.io.netty.multi_protocol.http_xml.jibx.pojo.Customer;
import com.zy.io.netty.multi_protocol.http_xml.jibx.pojo.Order;
import com.zy.io.netty.multi_protocol.http_xml.jibx.pojo.Shipping;

import java.util.Arrays;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/31.
 */
public class OrderFactory {

    public static Order create(int i) {
        Order order = new Order();
        order.setOrderNumber(123);
        Customer customer = new Customer();
        customer.setFirstName("ali");
        customer.setMiddleNames(Arrays.asList("baba"));
        customer.setLastName("taobao");
        order.setCustomer(customer);
        Address address = new Address();
        address.setCity("南京市");
        address.setCountry("中国");
        address.setPostCode("123321");
        address.setState("江苏省");
        address.setStreet1("龙眠大道");
        address.setStreet2("INTERNATIONAL_MAIL");
        order.setBillTo(address);
        order.setShipTo(address);
        order.setShipping(Shipping.INTERNATIONAL_MAIL);
        order.setTotal(33f);
        return order;
    }
}
