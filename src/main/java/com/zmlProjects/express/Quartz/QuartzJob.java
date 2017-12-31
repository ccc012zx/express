package com.zmlProjects.express.Quartz;

import com.zmlProjects.express.service.ExpressService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

public class QuartzJob implements Job {

    @Autowired
    private ExpressService expressService;

    @Override
    //该方法实现需要执行的任务
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        JobDataMap map = arg0.getJobDetail().getJobDataMap();
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();//快递单号，唯一
            Object value = entry.getValue();//快递公司编码
            expressService.queryOne(value.toString(), key);
        }
    }
}
