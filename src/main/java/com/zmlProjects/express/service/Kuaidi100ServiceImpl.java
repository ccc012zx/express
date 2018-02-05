package com.zmlProjects.express.service;

import com.alibaba.fastjson.JSON;
import com.zmlProjects.express.bean.KuaidiBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Service("kuaidi100Service")
public class Kuaidi100ServiceImpl implements Kuaidi100Service {

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

//    private static String getState(String state) {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("0", "在途，即货物处于运输过程中");
//        map.put("1", "揽件，货物已由快递公司揽收并且产生了第一条跟踪信息");
//        map.put("2", "疑难，货物寄送过程出了问题");
//        map.put("3", "签收，收件人已签收");
//        map.put("4", "退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收");
//        map.put("5", "派件，即快递正在进行同城派件");
//        map.put("6", "退回，货物正处于退回发件人的途中");
//
//        return map.get(state);
//    }

    public boolean queryOneState(KuaidiBean kuaidiBean) {
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
            return false;//快递还没有签收
        }
        return true;
    }
}
