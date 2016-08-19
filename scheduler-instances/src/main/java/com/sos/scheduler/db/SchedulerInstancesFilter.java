package com.sos.scheduler.db;

import com.sos.hibernate.SOSHibernateConstants;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSHibernateFilter;
import com.sos.hibernate.interfaces.ISOSHibernateFilter;

/** @author Uwe Risse */
public class SchedulerInstancesFilter extends SOSHibernateFilter implements ISOSHibernateFilter {

    private String dateFormat = SOSHibernateConstants.conDateFormat;
    private String schedulerId;
    private String hostname;
    private String dbName;
    private Integer port;
    private boolean isRunnig = false;
    private boolean isPaused = false;
    private boolean isAgent = false;
    private boolean isSosCommandWebservice = false;

    public SchedulerInstancesFilter() {
        super(SOSHibernateConstants.conPropertiesFileName);
    }

    public boolean isFiltered(DbItem dbitem) {
        return false;
    }

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isRunnig() {
        return isRunnig;
    }

    public void setRunnig(boolean isRunnig) {
        this.isRunnig = isRunnig;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean isAgent) {
        this.isAgent = isAgent;
    }

    public boolean isSosCommandWebservice() {
        return isSosCommandWebservice;
    }

    public void setSosCommandWebservice(boolean isSosCommandWebservice) {
        this.isSosCommandWebservice = isSosCommandWebservice;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String getTitle() {
        if (this.getSchedulerId() == null) {
            return null;
        } else {
            return this.getSchedulerId() + ":" + this.getHostname() + String.valueOf(this.getPort());
        }
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
