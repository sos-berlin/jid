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
public class SosExecutedTable extends SOSTable implements ISOSTable {

    public SosExecutedTable(Composite composite, int style) {
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
        new SOSTableColumn(this, " ", 30, messages);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_SchedulerID), 50);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_JOB), 100);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_START), 100, SOSTableColumn.ColumnType.DATE);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_END), 100, SOSTableColumn.ColumnType.DATE);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_DURATION), 90);
        new SOSTableColumn(this, SWT.NONE, messages.getLabel(DashBoardConstants.conSOSDashB_EXIT), 100);
        setMoveableColums(true);
    }

}