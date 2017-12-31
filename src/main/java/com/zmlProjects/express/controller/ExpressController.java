package com.zmlProjects.express.controller;

import com.zmlProjects.express.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
