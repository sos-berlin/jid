package com.sos.scheduler.db;

import java.io.File;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.hibernate.Query;

import sos.spooler.Spooler;

import com.sos.scheduler.db.SchedulerInstancesFilter;
import com.sos.hibernate.layer.SOSHibernateDBLayer;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;
import com.sos.schedulerinstances.classes.SelectSchedulerInstance;

/** \class SchedulerInstancesDBLayer \brief SchedulerInstancesDBLayer -
 * 
 * \details
 *
 * \section SchedulerInstancesDBLayer.java_intro_sec Introduction
 *
 * \section SchedulerInstancesDBLayer.java_samples Some Samples
 *
 * \code .... code goes here ... \endcode
 *
 * <p style="text-align:center">
 * <br />
 * --------------------------------------------------------------------------- <br />
 * APL/Software GmbH - Berlin <br />
 * ##### generated by ClaviusXPress (http://www.sos-berlin.com) ######### <br />
 * ---------------------------------------------------------------------------
 * </p>
 * \author Uwe Risse \version 13.09.2011 \see reference
 *
 * Created on 13.09.2011 14:40:18 */

public class SchedulerInstancesDBLayer extends SOSHibernateDBLayer {

    @SuppressWarnings("unused")
    private final String conClassName = "SchedulerInstancesDBLayer";
    private SchedulerInstancesFilter filter = null;

    public SchedulerInstancesDBLayer(File configurationFile_) {
        super();
        this.setConfigurationFile(configurationFile_);
        initFilter();
    }

    public void initFilter() {
        this.filter = new SchedulerInstancesFilter();
        this.filter.setDateFormat("yyyy-MM-dd HH:mm:ss");
        this.filter.setOrderCriteria("startTime");

    }

    public int delete() {

        if (session == null) {
            beginTransaction();
        }

        String hql = "delete from SchedulerInstancesDBItem " + getWhere();

        Query query = session.createQuery(hql);

        if (filter.getHostname() != null && !filter.getHostname().equals("")) {
            query.setText("hostName", filter.getHostname());
        }

        if (filter.getDbName() != null && !filter.getDbName().equals("")) {
            query.setText("dbName", filter.getDbName());
        }

        if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
            query.setText("schedulerId", filter.getSchedulerId());
        }

        if (filter.getPort() != null && filter.getPort() > 0) {
            query.setInteger("tcpPort", filter.getPort());
        }

        int row = query.executeUpdate();
        return row;
    }

    private String getWhere() {
        String where = "";
        String and = "";

        if (filter.getHostname() != null && !filter.getHostname().equals("")) {
            where += and + " hostName = :hostName";
            and = " and ";
        }

        if (filter.getDbName() != null && !filter.getDbName().equals("")) {
            where += and + " getDbName <= :dbName ";
            and = " and ";
        }

        if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
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

        if (where.trim().equals("")) {

        } else {
            where = "where " + where;
        }
        return where;

    }

    public List<SchedulerInstancesDBItem> getSchedulerInstancesList() {
        initSession();

        Query query = session.createQuery("from SchedulerInstancesDBItem " + getWhere() + this.filter.getOrderCriteria() + this.filter.getSortMode());

        if (filter.getHostname() != null && !filter.getHostname().equals("")) {
            query.setText("hostName", filter.getHostname());
        }

        if (filter.getDbName() != null && !filter.getDbName().equals("")) {
            query.setText("dbName", filter.getDbName());
        }

        if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
            query.setText("schedulerId", filter.getSchedulerId());
        }

        if (filter.getPort() != null && filter.getPort() > 0) {
            query.setInteger("tcpPort", filter.getPort());
        }

        if (this.getFilter().getLimit() > 0) {
            query.setMaxResults(this.getFilter().getLimit());
        }

        List<SchedulerInstancesDBItem> schedulerInstancesList = query.list();
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

    public SchedulerInstancesDBItem getInstanceById(String schedulerId) {
        initFilter();
        filter.setSchedulerId(schedulerId);

        List<SchedulerInstancesDBItem> schedulerList = getSchedulerInstancesList();
        if (schedulerList.size() > 0) {
            SchedulerInstancesDBItem schedulerInstanceDBItem = selectSchedulerInstance(schedulerList);
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
        schedulerInstancesDbItem.setDbName(objSpooler.db_name());
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

    public void insertScheduler(SchedulerInstancesDBItem newSchedulerInstancesDbItem) {
        SchedulerInstancesDBItem schedulerDbItem = null;
        initFilter();
        this.getFilter().setHostname(newSchedulerInstancesDbItem.getHostname());
        this.getFilter().setPort(newSchedulerInstancesDbItem.getTcpPort());
        this.getFilter().setLimit(1);
        this.getFilter().setSchedulerId(newSchedulerInstancesDbItem.getSchedulerId());

        List<SchedulerInstancesDBItem> schedulerList = getSchedulerInstancesList();
        if (schedulerList.size() > 0) {
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

        this.saveOrUpdate(schedulerDbItem);

    }

}
