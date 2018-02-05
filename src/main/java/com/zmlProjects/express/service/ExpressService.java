package com.zmlProjects.express.service;

import com.zmlProjects.express.bean.ExpressBean;

import java.text.ParseException;
import java.util.List;

public interface ExpressService {
    void queryAll(String expNumbers);

    void queryOne(String s, String key);

    ExpressBean getExpressByNumber(String expNumbers);

    void queryAndInsertExpress(String expNumbers, String username, int timeDay) throws ParseException;

    public List<ExpressBean> queryAllExpress();

    public void deleteExpress(ExpressBean expressBean);
}
