package cn.damonto.ssmTest.entity;

import cn.damonto.ssmTest.common.BaseEntity;

public class User extends BaseEntity{

    private String name;
    private String pwd;

    @Override
    public String toString() {
        return "User [name=" + name + ", pwd=" + pwd + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
