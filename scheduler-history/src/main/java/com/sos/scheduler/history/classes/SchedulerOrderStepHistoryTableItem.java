package com.sos.scheduler.history.classes;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.classes.SOSTableItem;
import com.sos.dialog.components.SOSTableColumn;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.interfaces.ISOSTableItem;
import com.sos.scheduler.history.db.SchedulerOrderStepHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;

/** @\author Uwe Risse */
public class SchedulerOrderStepHistoryTableItem extends SOSTableItem implements ISOSTableItem {

    private static final int ERROR_COLUMN_NUMBER = 6;
    private DbItem dbItem = null;
    private String[] textBuffer = null;

    public SchedulerOrderStepHistoryTableItem(final Table arg0, final int arg1) {
        super(arg0, arg1);
    }

    @Override
    public DbItem getData() {
        return (DbItem) super.getData();
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    @Override
    public void setDBItem(final DbItem d) {
        dbItem = d;
        this.setData(d);
    }

    @Override
    public void setColor() {
        org.eclipse.swt.graphics.Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
        org.eclipse.swt.graphics.Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
        org.eclipse.swt.graphics.Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        org.eclipse.swt.graphics.Color gray = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
        org.eclipse.swt.graphics.Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
        org.eclipse.swt.graphics.Color green = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
        this.setForeground(black);
        if (dbItem.getEndTime() == null && !dbItem.haveError()) {
            this.setBackground(0, green);
            this.setBackground(ERROR_COLUMN_NUMBER, green);
        } else {
            if (dbItem.haveError()) {
                this.setBackground(0, red);
                this.setForeground(0, white);
                this.setBackground(ERROR_COLUMN_NUMBER, red);
                this.setForeground(ERROR_COLUMN_NUMBER, white);
            } else {
                this.setBackground(0, white);
                this.setBackground(ERROR_COLUMN_NUMBER, white);
            }
        }
        colorSave();
    }

    @Override
    public void setColumns() {
        SchedulerOrderStepHistoryDBItem d = (SchedulerOrderStepHistoryDBItem) dbItem;
        String jobname = "";
        if (d.getSchedulerTaskHistoryDBItem() != null) {
            jobname = d.getSchedulerTaskHistoryDBItem().getJob();
        }
        textBuffer = new String[] { d.getId().getStepValue(), d.getState(), jobname, d.getStartTimeFormated(), d.getEndTimeFormated(),
                d.getDurationFormated(), d.getExecResult(), d.getErrorCode(), d.getErrorText() };
        this.setText(textBuffer);
    }

    @Override
    public String[] getTextBuffer() {
        return textBuffer;
    }

    @Override
    public Color[] getBackgroundColumn() {
        return colorsBackground;
    }

    @Override
    public Color[] getForegroundColumn() {
        return colorsForeground;
    }

}