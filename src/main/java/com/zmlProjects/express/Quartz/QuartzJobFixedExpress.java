package com.zmlProjects.express.Quartz;

import com.zmlProjects.express.bean.ExpressBean;
import com.zmlProjects.express.bean.KuaidiBean;
import com.zmlProjects.express.service.ExpressService;
import com.zmlProjects.express.service.Kuaidi100Service;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 每晚23点固定执行
 * 1、扫描Express表
 * 2、今天=timeday（例如第三天）的，查询快递单号状态
 * 2.1、已经到货的，转移该条数据到history表中
 * 2.2、没有到货的，短信提醒（短信提醒只会提醒一次，后续没有到货或者到货了都不短信提醒）
 * 3、今天>timeday的，查询快递单号状态
 * 3.1、已经到货的，转移该条数据到history表中
 */
public class QuartzJobFixedExpress implements Job {


    @Autowired
    private ExpressService expressService;

    @Autowired
    private Kuaidi100Service kuaidi100Service;

    private static final Logger logger = LoggerFactory.getLogger(QuartzJobFixedExpress.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
//        JobDataMap map = arg0.getJobDetail().getJobDataMap();
//        Set<Map.Entry<String, Object>> entries = map.entrySet();
//        for (Map.Entry<String, Object> entry : entries) {
//            String key = entry.getKey();//快递单号，唯一
//            Object value = entry.getValue();//快递公司编码
//            expressService.queryOne(value.toString(), key);
//        }

        logger.info("log4J打印：進入express job");
        //1、查询express表，返回所有数据的list
        List<ExpressBean> list = expressService.queryAllExpress();
        //2、遍历list，firstday+timeday<=today,查询快递状态：状态为"未到货"则调用短信提醒功能；状态为"已经签收"则调用Express数据转移功能（转移数据到history表中）
        for (int i = 0; i < list.size(); i++) {
            ExpressBean expressBean = list.get(i);

            if (DateUtils.addDays(expressBean.getFirstday(), expressBean.getTimeday()).getTime() <= new Date().getTime()) {
                String expressNumber = expressBean.getNumber();
                KuaidiBean kuaidiBean = kuaidi100Service.queryOneExpress("tiantian", expressNumber);//此处拆分到另外一个service中去
                if ("3".equals(kuaidiBean.getState())) {
                    //转移到hisory表中
                    //此处未完成：expressHisService.add(expressBean);
                    //express表中删除数据
                    expressService.deleteExpress(expressBean);
                } else {
                    if (DateUtils.addDays(expressBean.getFirstday(), expressBean.getTimeday()).getDay() == new Date().getDay()) {
                        //"等于"的时候才调用短信提醒功能，"大于"不管，默认寄件人要重点处理
                        System.out.println("print打印：" + expressNumber + "还没收货，请及时关注");
                        logger.info("log4J打印：" + expressNumber + "还没收货，请及时关注");
                    }
                }
            }
        }


    }
}
