package com.sos.jid.dialog.classes;

import java.util.prefs.Preferences;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.dialog.classes.FormBase;
import com.sos.scheduler.history.SchedulerHistoryDataProvider;
import com.sos.scheduler.history.SchedulerOrderStepHistoryDataProvider;

public class SOSDashboardMainView extends FormBase {

    protected Group right;
    protected Group left;
    protected Composite leftTabFolder = null;
    protected CTabFolder logTabFolder = null;
    protected Preferences prefs;
    protected SchedulerHistoryDataProvider detailHistoryDataProvider = null;
    protected SchedulerOrderStepHistoryDataProvider schedulerOrderStepHistoryDataProvider = null;
    protected SosDashboardHeader sosDashboardHeader = null;
    protected Table tableHistoryDetail = null;
    protected Table tableStepHistory = null;
    protected SOSDashboardOptions objOptions = null;
    public Composite mainViewComposite = null;

    public SOSDashboardMainView(Composite composite_) {
        super(composite_, DashBoardConstants.conPropertiesFileName);
    }

    public void setLeftTabFolder(Composite leftTabFolder) {
        this.leftTabFolder = leftTabFolder;
    }

    public void setDetailHistoryDataProvider(SchedulerHistoryDataProvider detailHistoryDataProvider) {
        this.detailHistoryDataProvider = detailHistoryDataProvider;
    }

    public void setSosDashboardHeaderplanned(SosDashboardHeader sosDashboardHeader) {
        this.sosDashboardHeader = sosDashboardHeader;
    }

    public void setLogTabFolder(CTabFolder logTabFolder) {
        this.logTabFolder = logTabFolder;
    }

    public void setTableHistoryDetail(Table tableHistoryDetail) {
        this.tableHistoryDetail = tableHistoryDetail;
    }

    public Composite getTablePlannedComposite() {
        return mainViewComposite;
    }

    public SosDashboardHeader getSosDashboardHeader() {
        return sosDashboardHeader;
    }

    public void setObjOptions(SOSDashboardOptions objOptions) {
        this.objOptions = objOptions;
    }

    public void setRight(Group right) {
        this.right = right;
    }

    public void setLeft(Group left) {
        this.left = left;
    }

    public Composite getTableComposite() {
        return mainViewComposite;
    }

    public void setTableStepHistory(Table tableStepHistory) {
        this.tableStepHistory = tableStepHistory;
    }

}