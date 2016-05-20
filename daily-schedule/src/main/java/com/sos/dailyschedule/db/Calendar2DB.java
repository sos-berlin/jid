package com.sos.dailyschedule.db;

import com.sos.dailyschedule.job.CreateDailyScheduleOptions;
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.SchedulerObjectFactory.enu4What;
import com.sos.scheduler.model.answers.*;
import com.sos.scheduler.model.commands.JSCmdShowCalendar;
import com.sos.scheduler.model.commands.JSCmdShowOrder;
import com.sos.scheduler.model.commands.JSCmdShowState;
import com.sos.scheduler.model.objects.Spooler;
import org.apache.log4j.Logger;

import java.io.File;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Calendar2DB {

    private static final Logger LOGGER = Logger.getLogger(Calendar2DB.class);
    private static SchedulerObjectFactory objFactory = null;
    private Date from;
    private Date to;
    private int dayOffset;
    private String schedulerId = "";
    private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private DailyScheduleDBLayer dailySchedulerDBLayer;
    private CreateDailyScheduleOptions options = null;

    public Calendar2DB(File configurationFile) {
        dailySchedulerDBLayer = new DailyScheduleDBLayer(configurationFile);
    }

    private void initSchedulerConnection() {
        if ("".equals(schedulerId)) {
            LOGGER.debug("Calender2DB");
            objFactory = new SchedulerObjectFactory(options.getSchedulerHostName().getValue(), options.getscheduler_port().value());
            objFactory.initMarshaller(Spooler.class);
            dayOffset = options.getdayOffset().value();
            schedulerId = this.getSchedulerId();
        }
    }

    private Calendar getCalender() {
        initSchedulerConnection();
        JSCmdShowCalendar objSC = objFactory.createShowCalendar();
        objSC.setWhat("orders");
        objSC.setLimit(9999);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
        objSC.setFrom(sdf.format(from));
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'23:59:59");
        objSC.setBefore(sdf.format(to));
        objSC.run();
        return objSC.getCalendar();
    }

    public void delete() {
        initSchedulerConnection();
        dailySchedulerDBLayer.setWhereFrom(from);
        dailySchedulerDBLayer.setWhereTo(to);
        dailySchedulerDBLayer.setWhereSchedulerId(schedulerId);
        dailySchedulerDBLayer.delete();
    }

    private String getSchedulerId() {
        JSCmdShowState objCmdShowState = objFactory.createShowState(new enu4What[] { enu4What.folders, enu4What.no_subfolders });
        objCmdShowState.setPath("notexist_sos");
        objCmdShowState.setSubsystems("folder");
        objCmdShowState.setMaxTaskHistory(BigInteger.valueOf(1));
        objCmdShowState.run();
        State objState = objCmdShowState.getState();
        return objState.getSpoolerId();
    }

    private boolean isSetback(Order order) {
        return order.getSetback() != null;
    }

    private Order getOrder(String jobChain, String orderId) {
        if (orderId == null) {
            return null;
        } else {
            JSCmdShowOrder objSO = objFactory.createShowOrder();
            objSO.setJobChain(jobChain);
            objSO.setOrder(orderId);
            objSO.run();
            Order order = objSO.getAnswer().getOrder();
            return order;
        }
    }

    public void store() throws ParseException {
        dailySchedulerDBLayer.beginTransaction();
        this.delete();
        Calendar objCalendar = getCalender();
        Order order = null;
        for (Object objCalendarObject : objCalendar.getAtOrPeriod()) {
            DailyScheduleDBItem dailySchedulerDBItem = new DailyScheduleDBItem(this.dateFormat);
            dailySchedulerDBItem.setSchedulerId(schedulerId);
            if (objCalendarObject instanceof At) {
                At objAt = (At) objCalendarObject;
                String orderId = objAt.getOrder();
                String jobChain = objAt.getJobChain();
                String job = objAt.getJob();
                order = getOrder(jobChain, orderId);
                dailySchedulerDBItem.setJob(job);
                dailySchedulerDBItem.setJobChain(jobChain);
                dailySchedulerDBItem.setOrderId(orderId);
                if (orderId == null || !isSetback(order)) {
                    dailySchedulerDBItem.setSchedulePlanned(objAt.getAt());
                    LOGGER.debug("Start at :" + objAt.getAt());
                    LOGGER.debug("Job Name :" + job);
                    LOGGER.debug("Job-Chain Name :" + jobChain);
                    LOGGER.debug("Order Name :" + orderId);
                } else {
                    LOGGER.debug("Job-Chain Name :" + jobChain + "/" + orderId + " ignored because order is in setback state");
                }
            } else {
                if (objCalendarObject instanceof Period) {
                    Period objPeriod = (Period) objCalendarObject;
                    String orderId = objPeriod.getOrder();
                    String jobChain = objPeriod.getJobChain();
                    String job = objPeriod.getJob();
                    order = getOrder(jobChain, orderId);
                    dailySchedulerDBItem.setJob(job);
                    dailySchedulerDBItem.setJobChain(jobChain);
                    dailySchedulerDBItem.setOrderId(orderId);
                    dailySchedulerDBItem.setPeriodBegin(objPeriod.getBegin());
                    dailySchedulerDBItem.setPeriodEnd(objPeriod.getEnd());
                    dailySchedulerDBItem.setRepeat(objPeriod.getAbsoluteRepeat(), objPeriod.getRepeat());
                    LOGGER.debug("Absolute Repeat Interval :" + objPeriod.getAbsoluteRepeat());
                    LOGGER.debug("Timerange start :" + objPeriod.getBegin());
                    LOGGER.debug("Timerange end :" + objPeriod.getEnd());
                    LOGGER.debug("Job-Name :" + objPeriod.getJob());
                }
            }
            dailySchedulerDBItem.setResult(0);
            dailySchedulerDBItem.setStatus(DashBoardConstants.STATUS_NOT_ASSIGNED);
            dailySchedulerDBItem.setModified(new Date());
            dailySchedulerDBItem.setCreated(new Date());
            if (dailySchedulerDBItem.getSchedulePlanned() != null && (dailySchedulerDBItem.getJob() == null 
                    || !"(Spooler)".equals(dailySchedulerDBItem.getJob()))) {
                dailySchedulerDBLayer.save(dailySchedulerDBItem);
            }
        }
        dailySchedulerDBLayer.commit();
    }

    private void setFrom() throws ParseException {
        Date now = new Date();
        if (dayOffset < 0) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(now);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, dayOffset);
            now = calendar.getTime();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String froms = formatter.format(now);
        froms = froms + "T00:00:00";
        formatter = new SimpleDateFormat(dateFormat);
        this.from = formatter.parse(froms);
    }

    private void setTo() throws ParseException {
        Date now = new Date();
        if (dayOffset > 0) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(now);
            calendar.add(GregorianCalendar.DAY_OF_MONTH, dayOffset);
            now = calendar.getTime();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String tos = formatter.format(now);
        tos = tos + "T23:59:59";
        formatter = new SimpleDateFormat(dateFormat);
        this.to = formatter.parse(tos);
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public void setOptions(CreateDailyScheduleOptions options) throws ParseException {
        this.options = options;
        dayOffset = options.getdayOffset().value();
        setFrom();
        setTo();
    }

}