package com.zmlProjects.express.Quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
public class QuartzExpress {
    private static SchedulerFactory schedulerFactory;
    private static Scheduler sched;

    public static SchedulerFactory getSchedulerFactory() {
        if (schedulerFactory == null) {
            schedulerFactory = new StdSchedulerFactory();
        }
        return schedulerFactory;
    }

    public static Scheduler getScheduler() {
        if (sched == null) {
            try {
                sched = getSchedulerFactory().getScheduler();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return sched;
    }

    /**
     * @param startTime 查询时间
     * @param com       快递公司代码
     * @param number    快递单号
     */
    public static void createQuartz(Date startTime, String com, String number) {
        // First we must get a reference to a scheduler
        try {
            // computer a time that is on the next round minute
            Date runTime = startTime;

            // define the job and tie it to our HelloJob class

            if (!getScheduler().checkExists(new JobKey("job" + number, "group" + number))) {
                JobDetail job = newJob(QuartzJob.class).withIdentity("job" + number, "group" + number).build();
                job.getJobDataMap().put(number, com);
                // Trigger the job to run on the next round minute
                Trigger trigger = newTrigger().withIdentity("trigger" + number, "group" + number).startAt(runTime).build();

                // Tell quartz to schedule the job using our trigger
                ;
                getScheduler().scheduleJob(job, trigger);
            }

            // Start up the scheduler (nothing can actually run until the
            // scheduler has been started)
            getScheduler().start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        // wait long enough so that the scheduler as an opportunity to
        // run the job!
        try {
            Thread.sleep(600);
            // executing...
        } catch (Exception e) {
            //
        }
    }
}
