package com.zmlProjects.express.service;

import com.alibaba.fastjson.JSON;
import com.zmlProjects.express.Quartz.QuartzExpress;
import com.zmlProjects.express.bean.DataBean;
import com.zmlProjects.express.bean.DateBeanSort;
import com.zmlProjects.express.bean.KuaidiBean;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("expressService")
public class ExpressService {
    @Autowired
    private ExpressService expressService;

    @Autowired
    private DateBeanSort dateBeanSort;

    public void queryAll(String numbers) {
        String com = "tiantian";//快递公司代码
        String[] numbersStrs = numbers.split(",");
        for (String number : numbersStrs) {
            expressService.queryOne(com, number);
        }

    }

    public void queryOne(String com, String number) {
        KuaidiBean kuaidiBean = expressService.queryOneExpress(com, number);
        StringBuffer sb = new StringBuffer();
        sb.append("快递单号：").append(number).append("，当前快递单状态：").append(ExpressService.getState(kuaidiBean.getState()));
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

    public KuaidiBean queryOneExpress(String com, String number) {
        KuaidiBean kuaidiBean = null;//返回参数
        String key = "29833628d495d7a5";//快递100的密钥
        String urlStr = "http://api.kuaidi100.com/api?id=" + key + "&com=" + com + "&nu=" + number + "&show=0&muti=1&order=desc";
        URL url = null;
        try {
            url = new URL(urlStr);

            URLConnection con = url.openConnection();
            con.setAllowUserInteraction(false);
            InputStream urlStream = url.openStream();
            String type = con.guessContentTypeFromStream(urlStream);
            String charSet = null;
            if (type == null)
                type = con.getContentType();

            if (type == null || type.trim().length() == 0 || type.trim().indexOf("text/json") < 0)
                return null;

            if (type.indexOf("charset=") > 0)
                charSet = type.substring(type.indexOf("charset=") + 8);

            byte b[] = new byte[10000];
            int numRead = urlStream.read(b);
            String content = new String(b, 0, numRead);
            while (numRead != -1) {
                numRead = urlStream.read(b);
                if (numRead != -1) {
//			     String newContent = new String(b, 0, numRead);
                    String newContent = new String(b, 0, numRead, charSet);

                    content += newContent;
                }
            }
            //System.err.println(content);
            kuaidiBean = JSON.parseObject(content, KuaidiBean.class);
            urlStream.close();

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return kuaidiBean;

    }

    public static String getState(String state) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("0", "在途，即货物处于运输过程中");
        map.put("1", "揽件，货物已由快递公司揽收并且产生了第一条跟踪信息");
        map.put("2", "疑难，货物寄送过程出了问题");
        map.put("3", "签收，收件人已签收");
        map.put("4", "退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收");
        map.put("5", "派件，即快递正在进行同城派件");
        map.put("6", "退回，货物正处于退回发件人的途中");

        return map.get(state);
    }
}
