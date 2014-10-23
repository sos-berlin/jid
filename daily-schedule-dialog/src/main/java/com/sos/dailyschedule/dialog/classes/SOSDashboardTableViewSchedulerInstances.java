package com.sos.dailyschedule.dialog.classes;


import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.classes.SOSUrl;
import com.sos.dialog.interfaces.ITableView;
import com.sos.jid.dialog.classes.SOSDashboardTableView;
import com.sos.jid.dialog.classes.SosDashboardHeader;


import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;

import org.eclipse.swt.SWT;
 
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class SOSDashboardTableViewSchedulerInstances extends SOSDashboardTableView implements ITableView {
    
    private final String className = "SOSDashboardTableViewSchedulerInstances";
    private static final String conTabLOG = "Log";
    private SOSBrowserTabFolder reportsBrowserTabFolder;
    
    public SOSDashboardTableViewSchedulerInstances(Composite composite_) {
        super(composite_);
        colPosForSort = 4;

    }

    private   Shell getParentShell() {
        return this.getShell();
    }
  
    @Override
    public void createMenue() {
        Menu contentMenu = new Menu(tableList);
        tableList.setMenu(contentMenu);
    
        MenuItem reportServer = new MenuItem(contentMenu, SWT.PUSH);
        reportServer.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Report_Server));
        reportServer.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
              public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                  TableItem[] t = tableList.getSelection();
                  if (t.length > 0) {
                      if (t[0].getData().getClass() == SchedulerInstancesDBItem.class){
                          SchedulerInstancesDBItem h = (SchedulerInstancesDBItem) t[0].getData();
                          prefs.node(DashBoardConstants.SOS_DASHBOARD).put(DashBoardConstants.conSOSDashB_Report_Server, String.format("%s:%s", h.getHostname(),h.getJettyHttpPort()));
                          String webServicAddress = prefs.node(DashBoardConstants.SOS_DASHBOARD).get(DashBoardConstants.conSOSDashB_Report_Server,"");

                          reportsBrowserTabFolder.closeAllBrowserTabs();
                          reportsBrowserTabFolder.addUrl(new SOSUrl("Report Overview:",webServicAddress+"/jobscheduler/operations_gui/scheduler_data/config/reports/report1.html"));
                          reportsBrowserTabFolder.addUrl(new SOSUrl("Report Top 10 Longest Running Process:",webServicAddress+"/jobscheduler/operations_gui/scheduler_data/config/reports/report2.html"));
                          reportsBrowserTabFolder.openUrls();
              
                      }
                  }
               }

              public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
              }
          });
        MenuItem excel = new MenuItem(contentMenu, SWT.PUSH);
        excel.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Export_To_Excel));
        excel.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
              public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                  tableList.createExcelFile();
              }

              public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
              }
          });
    }
 
    public void createTable () {

        mainViewComposite = new Composite(leftTabFolder, SWT.NONE);
        GridLayout layout = new GridLayout(DashBoardConstants.NUMBER_OF_COLUMNS_IN_GRID, false);
        mainViewComposite.setLayout(layout);
        sosDashboardHeader = new SosDashboardHeader(mainViewComposite, this);
        sosDashboardHeader.setPrefs(prefs);
        sosDashboardHeader.initLimit(DashBoardConstants.SOS_DASHBOARD_HEADER + "_" + className);

        tableList = new SosSchedulerInstancesTable(mainViewComposite, SWT.FULL_SELECTION | SWT.MULTI);
        
        super.createTable();
    }

    public void setReportsBrowserTabFolder(SOSBrowserTabFolder reportsBrowserTabFolder) {
        this.reportsBrowserTabFolder = reportsBrowserTabFolder;
    }
    
   

 

}
