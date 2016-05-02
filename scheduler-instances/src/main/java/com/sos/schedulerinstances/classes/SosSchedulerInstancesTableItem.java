package com.sos.schedulerinstances.classes;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.sos.dialog.classes.SOSTableItem;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.interfaces.ISOSTableItem;
import com.sos.joe.globals.misc.ResourceManager;
import com.sos.scheduler.db.SchedulerInstancesDBItem;

/** @author Uwe Risse */
public class SosSchedulerInstancesTableItem extends SOSTableItem implements ISOSTableItem {

    private static final Logger LOGGER = Logger.getLogger(SosSchedulerInstancesTableItem.class);
    private static final int STATUS_COLUMN_NUMBER = 8;
    private SchedulerInstancesDBItem schedulerInstancesDBItem = null;
    private String[] textBuffer = null;

    public SosSchedulerInstancesTableItem(final Table arg0, final int arg1) {
        super(arg0, arg1);
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    public DbItem getData() {
        return (SchedulerInstancesDBItem) super.getData();
    }

    public void setDBItem(final DbItem d) {
        schedulerInstancesDBItem = (SchedulerInstancesDBItem) d;
        this.setData(d);
    }

    private void setImage(int column, boolean checked) {
        Image checkedImage = ResourceManager.getImageFromResource("/sos/scheduler/editor/icons/config.gif");
        Image uncheckedImage = ResourceManager.getImageFromResource("/sos/scheduler/editor/icons/thin_close_view.gif");
        if (checked) {
            this.setImage(column, checkedImage);
        } else {
            this.setImage(column, uncheckedImage);
        }
    }

    public void setColor() {
        org.eclipse.swt.graphics.Color magenta = Display.getDefault().getSystemColor(SWT.COLOR_DARK_MAGENTA);
        org.eclipse.swt.graphics.Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
        org.eclipse.swt.graphics.Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
        org.eclipse.swt.graphics.Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
        org.eclipse.swt.graphics.Color gray = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
        org.eclipse.swt.graphics.Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
        org.eclipse.swt.graphics.Color yellow = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
        this.setForeground(black);
        if (schedulerInstancesDBItem != null && schedulerInstancesDBItem.getIsPaused()) {
            this.setBackground(0, gray);
            this.setBackground(STATUS_COLUMN_NUMBER, gray);
        } else {
            this.setBackground(white);
        }
        colorSave();
    }

    public void setColumns() {
        SchedulerInstancesDBItem d = schedulerInstancesDBItem;
        LOGGER.debug("...creating tableItem: " + d.getSchedulerId() + ":" + getParent().getItemCount());
        textBuffer = new String[] { d.getSchedulerId(), d.getHostname(), d.getTcpPortValue(),
                d.getUdpPortValue(), d.getDbName(), d.getDbHistoryTableName(), d.getDbOrderHistoryTableName(), d.getDbOrdersTableName(),
                d.getDbTasksTableName(), d.getDbVariablesTableName(), d.getWorkingDirectory(), d.getLiveDirectory(), d.getLogDir(),
                d.getIncludePath(), d.getIniPath(), "", d.getParam(), d.getSupervisorHostName(), d.getSupervisorTcpPortValue(),
                d.getJettyHttpPortValue(), d.getJettyHttpsPortValue(), "" };
        this.setText(textBuffer);
        setImage(15, d.getIsService());
        setImage(21, d.getIsSosCommandWebservice());
    }

    public void setColumnsShort() {
        SchedulerInstancesDBItem d = schedulerInstancesDBItem;
        LOGGER.debug("...creating tableItem: " + d.getSchedulerId() + ":" + getParent().getItemCount());
        textBuffer = new String[] { d.getSchedulerId(), d.getHostname(), d.getTcpPortValue(), d.getUdpPortValue(), };
        this.setText(textBuffer);
    }

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

    @Override
    public Color getBackground() {
        return null;
    }

    @Override
    public Color getForeground() {
        return null;
    }

    @Override
    public void setForeground(final Color c) {
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

}