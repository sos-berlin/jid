package com.sos.jid.dialog.classes;
import java.util.List;
import java.util.prefs.Preferences;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.apache.log4j.Logger;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.dialog.classes.FormBase;
import com.sos.hibernate.classes.DbItem;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.history.SchedulerHistoryDataProvider;

public class SOSDashboardMainView extends FormBase {
    protected Group                         right;
    protected Group                         left;
    protected CTabFolder                    leftTabFolder                       = null;
    protected CTabFolder                    logTabFolder                        = null;
    protected Preferences                   prefs;
    private static Logger                   logger                              = Logger.getLogger(SOSDashboardMainView.class);
    protected SchedulerHistoryDataProvider  detailHistoryDataProvider           = null;
    protected SosDashboardHeader            sosDashboardHeader                  = null;
    public Composite                        mainViewComposite                       = null;
    protected Table                         tableHistoryDetail                  = null;
    protected SOSDashboardOptions           objOptions                          = null;

    public SOSDashboardMainView(Composite composite_) {
        super(composite_, DashBoardConstants.conPropertiesFileName);
    }
 
  

    protected void showLog(Table table) { 
        this.showWaitCursor();
        if (table.getSelectionIndex() >= 0 && table.getSelectionIndex() >= 0) {
            SosTabLogItem logItem = (SosTabLogItem) logTabFolder.getSelection();
            if (logItem == null) {
                logTabFolder.setSelection(0);
                logItem = (SosTabLogItem) logTabFolder.getSelection();
            }
            TableItem t = table.getItem(table.getSelectionIndex());
            DbItem d = (DbItem) t.getData();
            logItem.addLog(table, d.getTitle(), detailHistoryDataProvider.getLogAsString(d));
        }
        this.RestoreCursor();
    }

      
    public void setLeftTabFolder(CTabFolder leftTabFolder) {
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

    
}
