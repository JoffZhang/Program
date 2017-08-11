package com.zy.hibernate.naming_strategies.model;

import javax.persistence.*;

/**
 * @Author : ZhangYun
 * @Description :显式命名表明
 * @Date :  2017/8/10.
 */
@Entity
//显式命名表明
@Table(name="TestTable2Bean")
public class TestTable2Bean {
    @Id
    @Column()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;
    @Column(length = 20)
    private String testName;

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
