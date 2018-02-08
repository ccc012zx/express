package com.zmlProjects.express.Quartz;

import com.zmlProjects.express.bean.ExpressBean;
import com.zmlProjects.express.bean.KuaidiBean;
import com.zmlProjects.express.service.ExpressService;
import com.zmlProjects.express.service.Kuaidi100Service;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service("quartzJobFixedExpressService")

public class QuartzJobFixedExpressService {
    @Autowired
    private ExpressService expressService;

    @Autowired
    private Kuaidi100Service kuaidi100Service;

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobFixedExpressService.class);

    public void scanExpress(String abc) {
       logger.info("log4J打印：進入express job");
        //1、查询express表，返回所有数据的list
        List<ExpressBean> list = expressService.queryAllExpress();
        //2、遍历list，firstday+timeday<=today,查询快递状态：状态为"未到货"则调用短信提醒功能；状态为"已经签收"则调用Express数据转移功能（转移数据到history表中）
        // for (int i = 0; i < list.size(); i++) {
            ExpressBean expressBean = list.get(i);

            if (DateUtils.addDays(expressBean.getFirstday(), expressBean.getTimeday()-1).getTime() <= new Date().getTime()) {
                //expressBean.getTimeday()为3，表示正常是第三天到货，
                String expressNumber = expressBean.getNumber();
                KuaidiBean kuaidiBean = kuaidi100Service.queryOneExpress("tiantian", expressNumber);//此处拆分到另外一个service中去
                if ("3".equals(kuaidiBean.getState())) {
                    //转移到hisory表中
                    //此处未完成：expressHisService.add(expressBean);//此处建议建一个表，存储一个快递单的所有数据
                    //express表中删除数据
                    expressService.deleteExpress(expressBean);
                } else {
                    if (DateUtils.addDays(expressBean.getFirstday(), expressBean.getTimeday()-1).getDay() < new Date().getDay()) {
                        //注此处是'='，临时改成<."等于"的时候才调用短信提醒功能（表示应当收货的那一天，當天提醒一次，後續就不在提醒了），"大于"不管，默认寄件人要重点处理
                        System.out.println("print打印：" + expressNumber + "还没收货，请及时关注");
                        logger.info("log4J打印：" + expressNumber + "还没收货，请及时关注");
                    }
                }
            }
        }


    }
}
