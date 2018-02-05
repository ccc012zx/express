package com.zmlProjects.express.bean;


import java.util.Date;

/**
 * 供quartz扫描的快递单类
 */
public class ExpressBean {
    //number, firstday, company, user, timeday
    private String number;//快递单号
    private Date firstday;//快递单发货日
    private String company;//快递公司
    private String user;//快递单寄件人/通知所有人
    private Integer timeday;//查询时效，武汉合肥第三天查询，北京第四天查询

    public ExpressBean(String number, Date firstday, String company, String user, Integer timeday) {
        this.number = number;
        this.firstday = firstday;
        this.company = company;
        this.user = user;
        this.timeday = timeday;
    }

    public ExpressBean(String number, java.sql.Date firstday, String company, String user, Integer timeday) {
        this.number = number;
        this.firstday = firstday;
        this.company = company;
        this.user = user;
        this.timeday = timeday;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getFirstday() {
        return firstday;
    }

    public void setFirstday(Date firstday) {
        this.firstday = firstday;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getTimeday() {
        return timeday;
    }

    public void setTimeday(Integer timeday) {
        this.timeday = timeday;
    }
}
