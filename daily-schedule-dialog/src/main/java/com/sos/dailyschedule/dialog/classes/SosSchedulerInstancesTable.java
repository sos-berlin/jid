package com.sos.dailyschedule.dialog.classes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.classes.SOSTable;
import com.sos.dialog.components.SOSTableColumn;
import com.sos.dialog.interfaces.ISOSTable;
import com.sos.localization.Messages;

/** @author Uwe Risse */
public class SosSchedulerInstancesTable extends SOSTable implements ISOSTable {

    public SosSchedulerInstancesTable(Composite composite, int style) {
        super(composite, style);
        createTable();
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    @Override
    public void createTable() {
        Messages messages = new Messages(DashBoardConstants.conPropertiesFileName);
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, DashBoardConstants.NUMBER_OF_COLUMNS_IN_GRID, 2));
        this.setSortDirection(SWT.UP);
        this.setLinesVisible(true);
        this.setHeaderVisible(true);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_SchedulerID), 50);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_HOSTNAME), 100);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_TCP_PORT), 100);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_UDP_PORT), 100);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_DB_NAME), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_DB_HISTORY_TABLENAME), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_DB_ORDERHISTORY_TABLENAME), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_DB_ORDERS_TABLENAME), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_DB_TASKS_TABLENAME), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_DB_VARIABLES_TABLENAME), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_WORKING_DIRECTORY), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_LIVE_DIRECTORY), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_LOGDIR), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_INCLUDEPATH), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_INIPATH), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_IS_SERVICE), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_PARAM), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_SUPERVISOR_HOSTNAME), 100);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_SUPERVISOR_TCP_PORT), 100);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_JETTY_HTTP_PORT), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_JETTY_HTTPS_PORT), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_IS_COMMAND_WEBSERVICE), 90);
        setMoveableColums(true);
    }

}