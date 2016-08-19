package com.sos.scheduler.db;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.hibernate.Query;

import sos.spooler.Spooler;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.hibernate.layer.SOSHibernateDBLayer;
import com.sos.schedulerinstances.classes.SelectSchedulerInstance;

public class SchedulerInstancesDBLayer extends SOSHibernateDBLayer {

    private static final Logger LOGGER = Logger.getLogger(SchedulerInstancesDBLayer.class);
    private SchedulerInstancesFilter filter = null;

    public SchedulerInstancesDBLayer(String configurationFileName) {
        super();
        this.setConfigurationFileName(configurationFileName);
        this.initConnection(this.getConfigurationFileName());
        initFilter();
    }

    public SchedulerInstancesDBLayer(SOSHibernateConnection connection) {
        super();
        this.initConnection(connection);
        initFilter();
    }

    public SchedulerInstancesDBLayer(File configurationFile) {
        super();
        try {
            this.setConfigurationFileName(configurationFile.getCanonicalPath());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            this.setConfigurationFileName("");
        }
        this.initConnection(this.getConfigurationFileName());
        initFilter();
    }

    public void initFilter() {
        this.filter = new SchedulerInstancesFilter();
        this.filter.setDateFormat("yyyy-MM-dd HH:mm:ss");
        this.filter.setOrderCriteria("startTime");
    }

    public int delete() throws Exception {
        int row = 0;
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        connection.beginTransaction();
        String hql = "delete from SchedulerInstancesDBItem " + getWhere();
        Query query = connection.createQuery(hql);
        if (filter.getHostname() != null && !"".equals(filter.getHostname())) {
            query.setText("hostName", filter.getHostname());
        }
        if (filter.getDbName() != null && !"".equals(filter.getDbName())) {
            query.setText("dbName", filter.getDbName());
        }
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            query.setText("schedulerId", filter.getSchedulerId());
        }
        if (filter.getPort() != null && filter.getPort() > 0) {
            query.setInteger("tcpPort", filter.getPort());
        }
        row = query.executeUpdate();
        return row;
    }

    private String getWhere() {
        String where = "";
        String and = "";
        if (filter.getHostname() != null && !"".equals(filter.getHostname())) {
            where += and + " hostName = :hostName";
            and = " and ";
        }
        if (filter.getDbName() != null && !"".equals(filter.getDbName())) {
            where += and + " getDbName <= :dbName ";
            and = " and ";
        }
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            where += and + " schedulerId = :schedulerId";
            and = " and ";
        }
        if (filter.getPort() != null && filter.getPort() > 0) {
            where += and + " tcpPort = :tcpPort";
            and = " and ";
        }
        if (filter.isSosCommandWebservice()) {
            where += and + " isSosCommandWebservice is true";
            and = " and ";
        }
        if (!"".equals(where.trim())) {
            where = "where " + where;
        }
        return where;
    }

    public List<SchedulerInstancesDBItem> getSchedulerInstancesList() throws Exception {
        if(connection == null) {
            initConnection(getConfigurationFileName());
        }
        List<SchedulerInstancesDBItem> schedulerInstancesList = null;
        connection.beginTransaction();
        Query query = connection.createQuery("from SchedulerInstancesDBItem " + getWhere() + this.filter.getOrderCriteria() 
                + this.filter.getSortMode());
        LOGGER.debug("getWhere(): " + getWhere().toString());
        if (filter.getHostname() != null && !"".equals(filter.getHostname())) {
            query.setText("hostName", filter.getHostname());
        }
        LOGGER.debug("filter.getHostname(): " + filter.getHostname());
        if (filter.getDbName() != null && !"".equals(filter.getDbName())) {
            query.setText("dbName", filter.getDbName());
        }
        LOGGER.debug("filter.getDbName(): " + filter.getDbName());
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            query.setText("schedulerId", filter.getSchedulerId());
        }
        LOGGER.debug("filter.getSchedulerId(): " + filter.getSchedulerId());
        if (filter.getPort() != null && filter.getPort() > 0) {
            query.setInteger("tcpPort", filter.getPort());
        }
        LOGGER.debug("filter.getPort(): " + filter.getPort());
        if (this.getFilter().getLimit() > 0) {
            query.setMaxResults(this.getFilter().getLimit());
        }
        LOGGER.debug("filter.getLimit(): " + filter.getLimit());
        schedulerInstancesList = query.list();
        return schedulerInstancesList;
    }

    private SchedulerInstancesDBItem selectSchedulerInstance(List<SchedulerInstancesDBItem> l) {
        if (l.size() == 1) {
            return (SchedulerInstancesDBItem) l.get(0);
        } else {
            Shell shell = new Shell();
            SelectSchedulerInstance s = new SelectSchedulerInstance(shell, l);
            return (SchedulerInstancesDBItem) s.getSelectedSchedulerInstancesDBItem();
        }
    }

    public SchedulerInstancesDBItem getInstanceById(String schedulerId) throws Exception {
        initFilter();
        filter.setSchedulerId(schedulerId);
        List<SchedulerInstancesDBItem> schedulerList = getSchedulerInstancesList();
        if (!schedulerList.isEmpty()) {
            SchedulerInstancesDBItem schedulerInstanceDBItem = selectSchedulerInstance(schedulerList);
            return schedulerInstanceDBItem;
        } else {
            return null;
        }
    }

    public SchedulerInstancesDBItem getInstance(String schedulerId, String host, Integer port) throws Exception {
        initFilter();
        filter.setSchedulerId(schedulerId);
        filter.setHostname(host);
        filter.setPort(port);
        List<SchedulerInstancesDBItem> schedulerList = getSchedulerInstancesList();
        if (!schedulerList.isEmpty()) {
            SchedulerInstancesDBItem schedulerInstanceDBItem = schedulerList.get(0);
            return schedulerInstanceDBItem;
        } else {
            return null;
        }
    }

    public SchedulerInstancesFilter getFilter() {
        return filter;
    }

    public void setFilter(SchedulerInstancesFilter filter) {
        this.filter = filter;
    }

    public SchedulerInstancesDBItem setInstancesDbItemValues(String host, int port, Spooler objSpooler) {
        SchedulerInstancesDBItem schedulerInstancesDbItem = new SchedulerInstancesDBItem();
        schedulerInstancesDbItem.setHostName(host);
        schedulerInstancesDbItem.setTcpPort(port);
        schedulerInstancesDbItem.setSchedulerId(objSpooler.id());
        schedulerInstancesDbItem.setDbName("");
        schedulerInstancesDbItem.setDbHistoryTableName(objSpooler.db_history_table_name());
        schedulerInstancesDbItem.setDbOrdersTableName(objSpooler.db_orders_table_name());
        schedulerInstancesDbItem.setDbTasksTableName(objSpooler.db_tasks_table_name());
        schedulerInstancesDbItem.setDbVariablesTableName(objSpooler.db_variables_table_name());
        schedulerInstancesDbItem.setIncludePath(objSpooler.include_path());
        schedulerInstancesDbItem.setIniPath(objSpooler.ini_path());
        schedulerInstancesDbItem.setLogDir(objSpooler.log_dir());
        schedulerInstancesDbItem.setParam(objSpooler.param());
        schedulerInstancesDbItem.setIsService(objSpooler.is_service());
        schedulerInstancesDbItem.setLiveDirectory(objSpooler.configuration_directory());
        if (objSpooler.supervisor_client() != null) {
            schedulerInstancesDbItem.setSupervisorHostName(objSpooler.supervisor_client().hostname());
            schedulerInstancesDbItem.setSupervisorTcpPort(objSpooler.supervisor_client().tcp_port());
        }
        return schedulerInstancesDbItem;
    }

    public void insertScheduler(SchedulerInstancesDBItem newSchedulerInstancesDbItem) throws Exception {
        SchedulerInstancesDBItem schedulerDbItem = null;
        initFilter();
        this.getFilter().setHostname(newSchedulerInstancesDbItem.getHostname());
        this.getFilter().setPort(newSchedulerInstancesDbItem.getTcpPort());
        this.getFilter().setLimit(1);
        this.getFilter().setSchedulerId(newSchedulerInstancesDbItem.getSchedulerId());
        List<SchedulerInstancesDBItem> schedulerList = getSchedulerInstancesList();
        if (!schedulerList.isEmpty()) {
            schedulerDbItem = schedulerList.get(0);
        } else {
            schedulerDbItem = new SchedulerInstancesDBItem();
        }
        schedulerDbItem.setHostName(newSchedulerInstancesDbItem.getHostname());
        schedulerDbItem.setTcpPort(newSchedulerInstancesDbItem.getTcpPort());
        schedulerDbItem.setSchedulerId(newSchedulerInstancesDbItem.getSchedulerId());
        schedulerDbItem.setDbName(newSchedulerInstancesDbItem.getDbName());
        schedulerDbItem.setDbHistoryTableName(newSchedulerInstancesDbItem.getDbHistoryTableName());
        schedulerDbItem.setDbOrdersTableName(newSchedulerInstancesDbItem.getDbOrdersTableName());
        schedulerDbItem.setDbTasksTableName(newSchedulerInstancesDbItem.getDbTasksTableName());
        schedulerDbItem.setDbVariablesTableName(newSchedulerInstancesDbItem.getDbVariablesTableName());
        schedulerDbItem.setIncludePath(newSchedulerInstancesDbItem.getIncludePath());
        schedulerDbItem.setIniPath(newSchedulerInstancesDbItem.getIniPath());
        schedulerDbItem.setLogDir(newSchedulerInstancesDbItem.getLogDir());
        schedulerDbItem.setParam(newSchedulerInstancesDbItem.getParam());
        schedulerDbItem.setIsService(newSchedulerInstancesDbItem.getIsService());
        schedulerDbItem.setLiveDirectory(newSchedulerInstancesDbItem.getLiveDirectory());
        schedulerDbItem.setSupervisorHostName(newSchedulerInstancesDbItem.getSupervisorHostName());
        schedulerDbItem.setSupervisorTcpPort(newSchedulerInstancesDbItem.getSupervisorTcpPort());
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        connection.beginTransaction();
        connection.saveOrUpdate(schedulerDbItem);
    }

}