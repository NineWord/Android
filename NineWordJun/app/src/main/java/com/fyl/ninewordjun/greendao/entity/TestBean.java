package com.fyl.ninewordjun.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by laofuzi on 2017/10/28.
 */
@Entity
public class TestBean {
    @Id
    private Long id;
    private String name;
    @Transient
    private int tempUsageCount; // not persisted

    @Generated(hash = 1979658847)
    public TestBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 2087637710)
    public TestBean() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
