package com.zmlProjects.express.controller;

import com.zmlProjects.express.Quartz.QuartzExpress;
import com.zmlProjects.express.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/exp")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @RequestMapping(value="/query",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,String> queryExpress(String expNumbers){
        expressService.queryAll(expNumbers);
        Map<String,String> map = new HashMap<String,String>();
        map.put("message","ok");
        map.put("url","http://www.ttkdex.com/Track.action?no="+expNumbers+"&verificationCode=9788");
        return map;
    }

    /**
     * 前端传递"快递单号、快递单发件人、快递单何时查询"，后端对快递单号查询，如果还没有到货，则数据入库，并在第三四五天继续查询，并短信提醒。
     *
     * @param expNumbers 以，拼接的多个快递单号
     * @param username
     * @param timeDay
     * @return
     */
    @RequestMapping(value = "/queryAndInsert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> queryAndInsertExpress(String expNumbers, String username, int timeDay) throws ParseException {
        // 创建定时任务
//        QuartzExpress.createFixedExpressQuartz("tiantian");
        expressService.queryAndInsertExpress(expNumbers, username, timeDay);
//        expressService.getExpressByNumber(expNumbers);
        Map<String, String> map = new HashMap<String, String>();
        map.put("message", "ok");
        map.put("url", "http://www.ttkdex.com/Track.action?no=" + expNumbers + "&verificationCode=9788");
        return map;
    }
}
