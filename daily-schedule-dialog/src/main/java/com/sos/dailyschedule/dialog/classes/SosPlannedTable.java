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
public class SosPlannedTable extends SOSTable implements ISOSTable {

    public SosPlannedTable(Composite composite, int style) {
        super(composite, style);
        createTable();
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    @Override
    public void createTable() {
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, DashBoardConstants.NUMBER_OF_COLUMNS_IN_GRID, 2));
        this.setSortDirection(SWT.UP);
        this.setLinesVisible(true);
        this.setHeaderVisible(true);
        Messages messages = new Messages(DashBoardConstants.conPropertiesFileName);
        new SOSTableColumn(this, " ", 30, messages);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_SchedulerID, 50, messages);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_JOB, 100, messages);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_Planned, 100, messages, SOSTableColumn.ColumnType.DATE);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_Executed, 100, messages, SOSTableColumn.ColumnType.DATE);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_END, 100, messages, SOSTableColumn.ColumnType.DATE);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_DURATION, 90, messages, SOSTableColumn.ColumnType.DATE);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_EXIT, 100, messages);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_Status, 100, messages);
        new SOSTableColumn(this, DashBoardConstants.conSOSDashB_LATE, 53, messages);
        setMoveableColums(true);
    }

}