package com.zy.hibernate.naming_strategies.model;

import javax.persistence.*;

/**
 * @Author : ZhangYun
 * @Description :隐式命名
 * @Date :  2017/8/10.
 */
@Entity
//隐式命名
@Table
public class TestTable1Bean {

    @Id
    @Column()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @Column(length = 20)
    private String testName;

    @ManyToOne
    private TestTable2Bean testForeign;

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

    public TestTable2Bean getTestForeign() {
        return testForeign;
    }

    public void setTestForeign(TestTable2Bean testForeign) {
        this.testForeign = testForeign;
    }
}
