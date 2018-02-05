package com.zmlProjects.express.service;

import com.alibaba.fastjson.JSON;
import com.zmlProjects.express.DAO.ExpressDAO;
import com.zmlProjects.express.Quartz.QuartzExpress;
import com.zmlProjects.express.bean.DataBean;
import com.zmlProjects.express.bean.DateBeanSort;
import com.zmlProjects.express.bean.ExpressBean;
import com.zmlProjects.express.bean.KuaidiBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("expressService")
public class ExpressServiceImpl implements ExpressService {

    private static String TIANTIANCOM = "tiantian";//天天快递编码

    @Autowired
    private DateBeanSort dateBeanSort;

    @Autowired
    private Kuaidi100Service kuaidi100Service;



    public void queryAll(String numbers) {
        String com = "tiantian";//快递公司代码
        String[] numbersStrs = numbers.split(",");
        for (String number : numbersStrs) {
            this.queryOne(com, number);
        }

    }

    public void queryOne(String com, String number) {
        KuaidiBean kuaidiBean = kuaidi100Service.queryOneExpress(com, number);
        StringBuffer sb = new StringBuffer();
//        sb.append("快递单号：").append(number).append("，当前快递单状态：").append(this.getState(kuaidiBean.getState()));
//			快递单当前的状态 ：　
//			0：在途，即货物处于运输过程中；
//			1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
//			2：疑难，货物寄送过程出了问题；
//			3：签收，收件人已签收；
//			4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
//			5：派件，即快递正在进行同城派件；
//			6：退回，货物正处于退回发件人的途中；

        if (null != kuaidiBean && !"3".equals(kuaidiBean.getState())) {
            //当前快递单号还没有签收，查询揽件日期
            //(此处要判断list是不是空的)
            List<DataBean> list = kuaidiBean.getData();
            Collections.sort(list, dateBeanSort);
            //比较第一个时间和当前时间，是否是第三天
            String nowDateStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            Date nowDate = null;//当前时间
            String firstDateStr = DateFormatUtils.format(list.get(0).getTime(), "yyyy-MM-dd");
            Date firstDate = null;//发出快递的第一天日期
            try {

                nowDate = DateUtils.parseDate(nowDateStr, "yyyy-MM-dd");
                firstDate = DateUtils.parseDate(firstDateStr, "yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sb.append(",快递寄出日期：").append(firstDate);
            if (nowDate.getTime() - firstDate.getTime() > 2 * 24 * 60 * 60 * 1000) {
                //已经超过3天了，还没有到

                sb.append(",【快递已逾期】发出的第一天是：" + firstDateStr + "；当前日期：" + nowDateStr);
                sb.append(",最新一条信息:" + DateFormatUtils.format(list.get(list.size() - 1).getTime(), "yyyy-MM-dd HH:mm:ss") + "：" + list.get(list.size() - 1).getContext());
            } else {
                //还没有过第三天，需要在第三天23：59：59秒启动第二次查询
                sb.append(";快递单号还没有过第三天，需要在第三天23：59：59秒启动第二次查询");
                // 注，此处要使用定时器，来定时执行第二次查询kuaidi100.query();
                Date startTime = new Date(firstDate.getTime() + 3 * 24 * 60 * 60 * 1000 - 1);//第二次查询的时间为快递第三天的晚上23:59:59
                /**
                 * 定时器测试，设置job启动时间

                 Date startTime = null;
                 try {
                 startTime = DateUtils.parseDate("2017-12-3 22:56:00", "yyyy-MM-dd HH:mm:ss");
                 } catch (ParseException e) {
                 e.printStackTrace();
                 }
                 */
                // 创建定时任务
                QuartzExpress.createQuartz(startTime, com, number);
            }
        }
        if (kuaidiBean != null) {
            sb.append(JSON.toJSON(kuaidiBean));
        }
        System.out.println(sb);
    }

    @Autowired
    private ExpressDAO expressDAO;

    @Override
    public ExpressBean getExpressByNumber(String expNumbers) {
        return expressDAO.selectByNumber(expNumbers);
    }

    /**
     * 前端传递"快递单号、快递单发件人、快递单何时查询"，后端对快递单号查询，如果还没有到货，则数据入库，并在第三四五天继续查询，并短信提醒。
     * 1、查询快递单号状态
     * 2、没有到货的快递单入库
     * 3、第三天job执行查询
     *
     * @param expNumbers
     * @param username
     * @param timeDay
     */
    @Override
    public void queryAndInsertExpress(String expNumbers, String username, int timeDay) throws ParseException {
        //拆分expNumbers
        String[] strs = expNumbers.split(",");
        for (int i = 0; i < strs.length; i++) {
            String expNumber = strs[i];
            if (StringUtils.isNotEmpty(expNumber)) {
                //查询快递单号，是否送达
                KuaidiBean kuaidiBean = kuaidi100Service.queryOneExpress(TIANTIANCOM, expNumber);

                List<DataBean> list = kuaidiBean.getData();
                Collections.sort(list, dateBeanSort);
                String firstDateStr = DateFormatUtils.format(list.get(0).getTime(), "yyyy-MM-dd");
                Date firstDay = DateUtils.parseDate(firstDateStr, "yyyy-MM-dd");//快递发出的第一天日期
                if (!kuaidi100Service.queryOneState(kuaidiBean)) {
                    //快递还没有签收，将上述快递单号入库（快递单号已经入库的放弃入库），并异步扫描（异步扫描还没有做）
                    expressDAO.insert(new ExpressBean(expNumber, firstDay, TIANTIANCOM, username, timeDay));
                }
            }
        }

    }

    @Override
    public List<ExpressBean> queryAllExpress() {
        return expressDAO.queryAllExpress();
    }

    @Override
    public void deleteExpress(ExpressBean expressBean) {
        expressDAO.deleteExpress(expressBean);
    }

}
