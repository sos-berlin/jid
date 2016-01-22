package com.sos.dailyschedule.dialog;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.sos.JSHelper.Basics.VersionInfo;
import com.sos.auth.SOSJaxbSubject;
import com.sos.dailyschedule.DailyScheduleDataProvider;
import com.sos.dailyschedule.classes.SOSDatabaseConfigurationFileMatcher;
import com.sos.dailyschedule.classes.SOSDatabaseConfigurationFileMatcherEntry;
import com.sos.dailyschedule.dialog.classes.SOSBrowserTabFolder;
import com.sos.dailyschedule.dialog.classes.SOSDashboardTableViewExecuted;
import com.sos.dailyschedule.dialog.classes.SOSDashboardTableViewPlanned;
import com.sos.dailyschedule.dialog.classes.SOSDashboardTableViewSchedulerInstances;
import com.sos.dailyschedule.dialog.classes.SosHistoryTable;
import com.sos.dailyschedule.dialog.classes.SosSchedulerOrderStepHistoryTable;
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.dialog.classes.FormBase;
import com.sos.dialog.classes.SOSUrl;
import com.sos.eventing.dialog.classes.SOSTabEVENTS;
import com.sos.jid.dialog.classes.SOSTabJOE;
import com.sos.jid.dialog.classes.SosTabLogItem;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;
import com.sos.scheduler.history.SchedulerHistoryDataProvider;
import com.sos.schedulerinstances.SchedulerInstancesDataProvider;

// uncomment to reactive JobNet import
// com.sos.jobnet.dialog.classes.SOSTabJOBNET;

public class DashboardShowDialog extends FormBase {

    private static final String LEFT_SASH = "LEFT_SASH";
    private static final String RIGHT_SASH = "RIGHT_SASH";
    private static final String LIST_OF_SCHEDULERS = "list_of_schedulers";
    private static final String LIST_OF_REPORTS = "list_of_reports";
	private static final String TABNAME_DASHBOARD = "Dashboard";
    private static final String TABNAME_SCHEDULER_OPERATIONS_CENTER = "JOC";
    private static final String TABNAME_REPORTS = "Reports";
    private static final String TABNAME_SCHEDULER_JOE = "JOE";
	private static final String TABNAME_SCHEDULER_EVENTS = "Events";
    // uncomment to reactive JobNet private static final String
    // TABNAME_SCHEDULER_JOBNET = "Jobnet";
    private static final String TABNAME_SCHEDULER_JADE = "Jade";
	private static final String conJOB_SCHEDULER_DASHBOARD = "JobScheduler Information Dashboard";
	private static final String conTabLOG = "Log";
	private SashForm logSashForm;
	private SashForm tablesSashForm;
	protected Composite composite = null;
	private Shell dashboardShell = null;
	private Display display;
	private Preferences prefs;
    private Label lbShowTime;
    private Timer showTimer;
    private String lastDbname="";
    private SosHistoryTable tableHistoryDetail = null;
    private SosSchedulerOrderStepHistoryTable tableStepHistoryDetail = null;
	public Group left;
	private Group right = null;
	private Group bottom = null;
	private Composite parent = null;
	private CTabFolder mainTabFolder = null;
    private SOSBrowserTabFolder sosJocTabFolder;
    private SOSBrowserTabFolder sosReportsTabFolder;
    private CTabFolder leftTabFolder = null;
    private CTabFolder rightTabFolder = null;
	private CTabFolder logTabFolder = null;
	private Composite joeComposite;
	private Composite dashboardComposite;
	private CCombo cbDatabaseConnections;
    private SOSTabEVENTS tbtmEvents;
    //uncomment to reactive JobNet private SOSTabJOBNET tbtmJobnet;
    // private SOSTabJade tbtmJade;
	private SOSDashboardTableViewExecuted tableViewExecuted;
    private SOSDashboardTableViewPlanned tableViewPlanned;
    private SOSDashboardTableViewSchedulerInstances tableViewSchedulerInstances;
	private SchedulerHistoryDataProvider detailHistoryDataProvider = null;
	private SchedulerHistoryDataProvider executedHistoryDataProvider = null;
    private DailyScheduleDataProvider dailyScheduleDataProvider = null;
    private SchedulerInstancesDataProvider schedulerInstancesDataProvider = null;
	private SchedulerInstancesDBLayer schedulerInstancesDBLayer;
	private SOSDashboardOptions objOptions = null;
	private boolean haveDb;
    private SOSJaxbSubject currentUser=null;
    SOSDatabaseConfigurationFileMatcher sosDatabaseConfigurationFileMatcher;

    public class ShowTimeTask extends TimerTask {

        private static final String EMPTYSTRING = "";
        @Override
          public void run() {
              if (display == null) {
                  display = Display.getDefault();
              }
              display.syncExec(new Runnable() {

                  @Override
                  public void run() {
                      final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                      lbShowTime.setText( dateFormat.format(new Date()));
                  };
              });
          }
      }

	public DashboardShowDialog(final Composite composite_) throws Exception {
		super(composite_, DashBoardConstants.conPropertiesFileName);
		composite = composite_;
	}

    private void showJoc() {
        SOSUrl defaultUrl = null;
        if (objOptions != null && objOptions.getEnableJOC().isTrue()) {
            sosJocTabFolder = new SOSBrowserTabFolder(mainTabFolder,TABNAME_SCHEDULER_OPERATIONS_CENTER, Messages);
            String listOfScheduler = prefs.node(DashBoardConstants.SOS_DASHBOARD).get(LIST_OF_SCHEDULERS, "");
            if (haveDb && listOfScheduler.equals("")) { // Den ersten aus den
                schedulerInstancesDBLayer.initFilter();
                List<SchedulerInstancesDBItem> instances = schedulerInstancesDBLayer.getSchedulerInstancesList();
                if (instances.size() > 0) {
                    SchedulerInstancesDBItem schedulerInstancesDBItem = instances.get(0);
                    defaultUrl = new SOSUrl (schedulerInstancesDBItem.getHostName() + ":" + schedulerInstancesDBItem.getTcpPort()); 
                }
            }
            sosJocTabFolder.setOpenMenueItem(Messages.getLabel(DashBoardConstants.conSOSDashB_open_scheduler));
            sosJocTabFolder.setDefaultUrl(defaultUrl);
            sosJocTabFolder.setPrefKey(LIST_OF_SCHEDULERS);
            sosJocTabFolder.setPrefs(prefs);
            sosJocTabFolder.openUrls();
        }
    }

    private void showReports() {
         if (objOptions != null && objOptions.getEnableReports().isTrue()) {
             String webServicAddress = objOptions.securityServer.Value();
             webServicAddress = prefs.node(DashBoardConstants.SOS_DASHBOARD).get(DashBoardConstants.conSOSDashB_Report_Server,webServicAddress);
             sosReportsTabFolder = new SOSBrowserTabFolder(mainTabFolder,TABNAME_REPORTS, Messages);

             sosReportsTabFolder.setPrefKey(LIST_OF_REPORTS);
             sosReportsTabFolder.setPrefs(prefs);
            sosReportsTabFolder.addUrl(new SOSUrl("Report Overview:", webServicAddress
                    + "/jobscheduler/operations_gui/scheduler_data/config/reports/report1.html"));
            sosReportsTabFolder.addUrl(new SOSUrl("Report Top 10 Longest Running Process:", webServicAddress
                    + "/jobscheduler/operations_gui/scheduler_data/config/reports/report2.html"));
             sosReportsTabFolder.openUrls();
        }
    }

    private void createMainWindow() {
        if (objOptions != null
                && (objOptions.getEnableJOC().isTrue() || objOptions.getEnableJOE().isTrue() || objOptions.getEnableEvents().isTrue() || objOptions.getEnableJobnet().isTrue())) {
			mainTabFolder = new CTabFolder(dashboardShell, SWT.NONE);
			mainTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			mainTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
			mainTabFolder.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					setRefreshState();
				}
			});
			dashboardComposite = new Composite(mainTabFolder, SWT.NONE);
			dashboardComposite.setLayout(new GridLayout());
			CTabItem tbtmDashboard = new CTabItem(mainTabFolder, SWT.NONE);
			tbtmDashboard.setText(TABNAME_DASHBOARD);
			tbtmDashboard.setControl(dashboardComposite);
		} else {
			dashboardComposite = dashboardShell;
		}
	}

	private void setRefreshState() {
		if (haveDb) {
			tableViewPlanned.getSosDashboardHeader().getRefreshTimer().cancel();
			tableViewExecuted.getSosDashboardHeader().getRefreshTimer().cancel();
			if (tbtmEvents != null) {
				tbtmEvents.disableRefresh();
			}
			if (mainTabFolder != null) {
				if (mainTabFolder.getSelection() == null || mainTabFolder.getSelection().getText().equals(TABNAME_DASHBOARD)) {
					tableViewPlanned.getSosDashboardHeader().resetRefreshTimer();
					tableViewExecuted.getSosDashboardHeader().resetRefreshTimer();
				} else {
					if (mainTabFolder.getSelection().getText().equals(TABNAME_SCHEDULER_EVENTS)) {
						if (tbtmEvents != null) {
							tbtmEvents.enableRefresh();
						}
					}
				}
			} else {
				tableViewPlanned.getSosDashboardHeader().getRefreshTimer().cancel();
				tableViewExecuted.getSosDashboardHeader().getRefreshTimer().cancel();
				if (tbtmEvents != null) {
					tbtmEvents.disableRefresh();
				}
			}
		}
	}

	private void showJoe() {
		if (objOptions != null && objOptions.getEnableJOE().isTrue()) {
			joeComposite = new Composite(mainTabFolder, SWT.NONE);
			joeComposite.setLayout(new GridLayout());
			CTabItem tbtmJoe = new SOSTabJOE(TABNAME_SCHEDULER_JOE, mainTabFolder, joeComposite);
			tbtmJoe.setControl(joeComposite);
		}
	}

	private void showEvents() {
		if (objOptions != null && objOptions.getEnableEvents().isTrue()) {
			tbtmEvents = new SOSTabEVENTS(objOptions, TABNAME_SCHEDULER_EVENTS, mainTabFolder);
		}
	}

    //uncomment to reactive JobNet private void showJobnet() {
    // uncomment to reactive JobNet if (objOptions != null &&
    // objOptions.getEnableJobnet().isTrue()) {
    // uncomment to reactive JobNet tbtmJobnet = new SOSTabJOBNET(objOptions,
    // TABNAME_SCHEDULER_JOBNET, mainTabFolder);
    //uncomment to reactive JobNet }
    //uncomment to reactive JobNet }

    private void showJade() {
        // uncomment to reactive Jade if (objOptions != null &&
        // objOptions.getEnableJade().isTrue()) {
        // uncomment to reactive Jade tbtmJade = new SOSTabJade(objOptions,
        // TABNAME_SCHEDULER_JADE, mainTabFolder);
    	//uncomment to reactive Jade         }
    }

	private void createFormParts() {
		createMainWindow();
		showJoe();
		showJoc();
		showReports();
		showEvents();
		showJade();
        //uncomment to reactive JobNet showJobnet();
        showTimer = new Timer();
        showTimer.schedule(new ShowTimeTask(), 1 * 1000, 1 * 1000);
		logSashForm = new SashForm(dashboardComposite, SWT.SMOOTH | SWT.VERTICAL);
		logSashForm.setSashWidth(10);
		GridData gd_sashForm = new GridData(GridData.FILL_BOTH);
		gd_sashForm.heightHint = 600;
		logSashForm.setLayoutData(gd_sashForm);
		tablesSashForm = new SashForm(logSashForm, SWT.HORIZONTAL);
		tablesSashForm.setSashWidth(10);
		left = new Group(tablesSashForm, SWT.NONE);
		right = new Group(tablesSashForm, SWT.NONE);
		bottom = new Group(logSashForm, SWT.NONE);
		final FillLayout fl_bottom = new FillLayout();
		bottom.setLayout(fl_bottom);
		final FillLayout fl_right = new FillLayout();
		right.setLayout(fl_right);
        int leftSash =  prefs.node(DashBoardConstants.SOS_DASHBOARD).getInt(LEFT_SASH, 730);
        int rigthSash =  prefs.node(DashBoardConstants.SOS_DASHBOARD).getInt(RIGHT_SASH, 210);
		tablesSashForm.setWeights(new int[] { leftSash, rigthSash });
		dashboardComposite.layout();
        composite.getDisplay().addFilter(SWT.KeyDown, new Listener() {

            public void handleEvent(Event event) {
                if (event.keyCode == SWT.F5) {
                    if (mainTabFolder.getSelection() == null || mainTabFolder.getSelection().getText().equals(TABNAME_DASHBOARD)) {
                        if (leftTabFolder.getSelection() != null && leftTabFolder.getSelectionIndex() == 0) {
                            tableViewPlanned.actualizeList();
                        } else {
                            tableViewExecuted.actualizeList();
                        }
                    }
                    // uncomment to reactive JobNet if
                    // (mainTabFolder.getSelection() == null ||
                    // mainTabFolder.getSelection().getText().equals(TABNAME_SCHEDULER_JOBNET))
                    // {
                    	//uncomment to reactive JobNet if (tbtmJobnet != null) {
                        //uncomment to reactive JobNet  tbtmJobnet.refresh();
                    	//uncomment to reactive JobNet  }
                    //uncomment to reactive JobNet }
                }
	           //     event.doit = false;
            }
        });
	}

	private void createLeft() {
		parent = left;
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 9;
		parent.setLayout(gridLayout);

		dashboardShell.setText(conJOB_SCHEDULER_DASHBOARD + " (" + VersionInfo.VERSION_STRING + ")");
		dashboardShell.setSize(1000, 550);
		dashboardShell.addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(final MouseEvent arg0) {
				if (haveDb && mainTabFolder != null && mainTabFolder.getSelection() != null) {
					if (mainTabFolder.getSelection().getText().equals(TABNAME_DASHBOARD)) {
						tableViewPlanned.getSosDashboardHeader().resetRefreshTimer();
						tableViewExecuted.getSosDashboardHeader().resetRefreshTimer();
					}
				}
			}
		});
		leftTabFolder = new CTabFolder(parent, SWT.NONE);
		leftTabFolder.addListener (SWT.Resize,  new Listener () {

		     public void handleEvent (Event e) {
		         int[] weights = tablesSashForm.getWeights();
                 prefs.node(DashBoardConstants.SOS_DASHBOARD).putInt(LEFT_SASH, weights[0]);
                 prefs.node(DashBoardConstants.SOS_DASHBOARD).putInt(RIGHT_SASH, weights[1]);
 		     }
        });
	    leftTabFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (leftTabFolder.getSelectionIndex() == 0) {
                    if (haveDb) {
                        tableViewPlanned.buildTable();
                    }
                } else {
                    if (haveDb) {
                        tableViewExecuted.buildTable();
                    }
                }
            }
        });
		leftTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 9, 3));
		leftTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tableViewPlanned = new SOSDashboardTableViewPlanned(composite);
		tableViewPlanned.setObjOptions(objOptions);
		tableViewPlanned.setLeftTabFolder(leftTabFolder);
		tableViewPlanned.setPrefs(prefs);
		tableViewPlanned.setRight(right);
		tableViewPlanned.setLeft(left);
		tableViewPlanned.setTableDataProvider(dailyScheduleDataProvider);
		tableViewPlanned.setDetailHistoryDataProvider(detailHistoryDataProvider);
		tableViewPlanned.setDBLayer(schedulerInstancesDBLayer.getConfigurationFile());
		tableViewPlanned.createTable();
		tableViewPlanned.createMenue();
		if (haveDb) {
		 	tableViewPlanned.actualizeList();
			tableViewPlanned.getSchedulerIds();
		} else {
			tableViewPlanned.getSosDashboardHeader().getRefreshTimer().cancel();
			tableViewPlanned.getSosDashboardHeader().setEnabled(false);
			tableViewPlanned.getTableList().setEnabled(false);
		}

		tableViewExecuted = new SOSDashboardTableViewExecuted(composite);
		tableViewExecuted.setObjOptions(objOptions);
		tableViewExecuted.setDBLayer(schedulerInstancesDBLayer.getConfigurationFile());
		tableViewExecuted.setLeftTabFolder(leftTabFolder);
		tableViewExecuted.setPrefs(prefs);
		tableViewExecuted.setRight(right);
		tableViewExecuted.setLeft(left);
		tableViewExecuted.setTableDataProvider(executedHistoryDataProvider);
		tableViewExecuted.setDetailHistoryDataProvider(detailHistoryDataProvider);
		tableViewExecuted.createTable();
		tableViewExecuted.createMenue();
		if (haveDb) {
		    tableViewExecuted.actualizeList();
			tableViewExecuted.getSchedulerIds();
		} else {
			tableViewExecuted.getSosDashboardHeader().getRefreshTimer().cancel();
			tableViewExecuted.getSosDashboardHeader().setEnabled(false);
			tableViewExecuted.getTableList().setEnabled(false);
		}

		tableViewSchedulerInstances = new SOSDashboardTableViewSchedulerInstances(composite);
		tableViewSchedulerInstances.setObjOptions(objOptions);
		tableViewSchedulerInstances.setDBLayer(schedulerInstancesDBLayer.getConfigurationFile());
		tableViewSchedulerInstances.setReportsBrowserTabFolder(sosReportsTabFolder);
		tableViewSchedulerInstances.setLeftTabFolder(leftTabFolder);
		tableViewSchedulerInstances.setPrefs(prefs);
		tableViewSchedulerInstances.setLeft(left);
		tableViewSchedulerInstances.setTableDataProvider(schedulerInstancesDataProvider);
		tableViewSchedulerInstances.setDetailHistoryDataProvider(detailHistoryDataProvider);
		tableViewSchedulerInstances.createTable();
		tableViewSchedulerInstances.createMenue();
        if (haveDb) {
            tableViewSchedulerInstances.actualizeList();
            tableViewSchedulerInstances.getSchedulerIds();
        } else {
            tableViewSchedulerInstances.getSosDashboardHeader().getRefreshTimer().cancel();
            tableViewSchedulerInstances.getSosDashboardHeader().setEnabled(false);
            tableViewSchedulerInstances.getTableList().setEnabled(false);
        }

		CTabItem tbtmDailyPlan = new CTabItem(leftTabFolder, SWT.NONE);
		tbtmDailyPlan.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_NAME_TAB_PLANNED));
		tbtmDailyPlan.setControl(tableViewPlanned.getTablePlannedComposite());
        CTabItem tbtmHistory = new CTabItem(leftTabFolder, SWT.NONE);
        tbtmHistory.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_NAME_TAB_HISTORY));
        tbtmHistory.setControl(tableViewExecuted.getTableComposite());

        if (objOptions != null && objOptions.getEnableSchedulerInstances().isTrue()) {
            CTabItem tbtmSchedulerInstances = new CTabItem(leftTabFolder, SWT.NONE);
            tbtmSchedulerInstances.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_NAME_TAB_SCHEDULER_INSTANCES));
            tbtmSchedulerInstances.setControl(tableViewSchedulerInstances.getTableComposite());            
        }
		leftTabFolder.setSelection(0);
		showDatabaseSelection();
	}

	private void showDatabaseSelection(){
        File f = this.dailyScheduleDataProvider.getConfigurationFile();

	    sosDatabaseConfigurationFileMatcher = new SOSDatabaseConfigurationFileMatcher(f);
        ArrayList<SOSDatabaseConfigurationFileMatcherEntry> hibernateConfigurationFiles = sosDatabaseConfigurationFileMatcher.scanForHibernateConfigurationFiles();
        if (hibernateConfigurationFiles.size() > 1){
    	    cbDatabaseConnections = new CCombo(parent, SWT.BORDER);
            for (int i = 0; i < hibernateConfigurationFiles.size(); i++) {
                SOSDatabaseConfigurationFileMatcherEntry sosDatabaseConfigurationFileMatcherEntry = hibernateConfigurationFiles.get(i);
                cbDatabaseConnections.add(sosDatabaseConfigurationFileMatcherEntry.getDbName());
            }
    	    GridData gdCombo = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    	    gdCombo.widthHint = 120;
    	    gdCombo.minimumWidth = 120;
    	    cbDatabaseConnections.setLayoutData(gdCombo);
    	    cbDatabaseConnections.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent event) {
                    try{
                        String dbName = cbDatabaseConnections.getText();
                        switchDatabaseConnection(dbName);
                        lastDbname = dbName;
                    }catch (Exception e){
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION |SWT.OK);
                        messageBox.setMessage(e.getMessage());
                        messageBox.open();
                        switchDatabaseConnection(lastDbname);
                        cbDatabaseConnections.setText(lastDbname);
                    }
                }
            });
        }
	}
	
	private void switchDatabaseConnection(String dbName){
	    if (dbName.equals("")){
	        dbName = "default";
	    }
	    File hibernateConfigurationFile = sosDatabaseConfigurationFileMatcher.getFile(dbName);
        DailyScheduleDataProvider sav = dailyScheduleDataProvider;  
        DailyScheduleDataProvider dataProvider = new DailyScheduleDataProvider(new File(hibernateConfigurationFile.getAbsolutePath()));
        setDataProvider(dataProvider);
        tableViewPlanned.setTableDataProvider(dailyScheduleDataProvider);
        tableViewSchedulerInstances.setTableDataProvider(schedulerInstancesDataProvider);
        tableViewExecuted.setTableDataProvider(executedHistoryDataProvider);
        tableViewPlanned.actualizeList();
        tableViewExecuted.actualizeList();
	}
	
	private void createRight() {
	    final GridLayout gridLayout = new GridLayout();
        right.setLayout(gridLayout);
	    rightTabFolder = new CTabFolder(right, SWT.NONE);
	    rightTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 9, 3));
	    rightTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

        CTabItem detailHistory  = new CTabItem(rightTabFolder, SWT.NONE);
        CTabItem stepHistory    = new CTabItem(rightTabFolder, SWT.NONE);
        detailHistory.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_NAME_TAB_HISTORY));
        stepHistory.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_NAME_TAB_STEP_HISTORY));

        Composite historyViewComposite = new Composite(rightTabFolder, SWT.NONE);
        Composite stepHistoryViewComposite = new Composite(rightTabFolder, SWT.NONE);

		tableHistoryDetail = new SosHistoryTable(historyViewComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, Messages);
		tableHistoryDetail.setEnabled(haveDb);
		tableHistoryDetail.setLogTabFolder(logTabFolder);
		tableHistoryDetail.setDetailHistoryDataProvider(detailHistoryDataProvider);

		tableStepHistoryDetail = new SosSchedulerOrderStepHistoryTable(stepHistoryViewComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, Messages);	
 		tableStepHistoryDetail.setEnabled(haveDb);
 		tableStepHistoryDetail.setLogTabFolder(logTabFolder);
 		tableStepHistoryDetail.setDetailHistoryDataProvider(detailHistoryDataProvider);
	    tableViewExecuted.setTableStepHistory(tableStepHistoryDetail);
        tableViewPlanned.setTableStepHistory(tableStepHistoryDetail);
	    stepHistory.setControl(stepHistoryViewComposite);
		detailHistory.setControl(historyViewComposite);
	    rightTabFolder.setSelection(0);
	}

	private void createBottom() {
		logTabFolder = new CTabFolder(bottom, SWT.NONE);
		createContextMenuLogTabfolder();
		logTabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				SosTabLogItem tItem = (SosTabLogItem) logTabFolder.getSelection();
				tItem.setSelection();
			}
		});
		logTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		SosTabLogItem tbtmLog = new SosTabLogItem(conTabLOG, logTabFolder, Messages);

		lbShowTime = new Label(dashboardShell, SWT.NONE);
 		lbShowTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
 		lbShowTime.setText("                    ");
	}

    /** Create contents of the window */
	protected void createContents() {
        prefs = Preferences.userNodeForPackage(this.getClass());
		if (haveDb) {
			executedHistoryDataProvider.setIgnoreList(prefs);
			dailyScheduleDataProvider.setIgnoreList(prefs);
		}
		dashboardShell = new Shell();
		dashboardShell.setLayout(new GridLayout());
		this.showWaitCursor();
		createFormParts();
		createLeft();
        createBottom();
		createRight();
		tableViewExecuted.setLogTabFolder(logTabFolder);
		tableViewExecuted.setTableHistoryDetail(tableHistoryDetail);
		tableViewPlanned.setLogTabFolder(logTabFolder);
		tableViewPlanned.setTableHistoryDetail(tableHistoryDetail);
		/*
		 * tableViewEventList.setLogTabFolder(logTabFolder);
		 * tableViewEventList.setTableHistoryDetail(tableHistoryDetail);
		 */
		this.RestoreCursor();
		parent = left;
		logSashForm.setWeights(new int[] { 309, 170 });
		dashboardComposite.layout();
		setRefreshState();
	}

	public void show() {
		createContents();
		InputStream imgInputStream = DashboardShowDialog.class.getResourceAsStream("/com/sos/dialog/dashboard.png");
		if (imgInputStream != null) {
			dashboardShell.setImage(new Image(shell.getDisplay(), imgInputStream));
		}
		dashboardShell.open();
		dashboardShell.layout();
	}
 
	private void createContextMenuLogTabfolder() {
		Menu contentMenu = new Menu(logTabFolder);
		logTabFolder.setMenu(contentMenu);
		MenuItem addItem = new MenuItem(contentMenu, SWT.PUSH);
		addItem.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_new_log));
		addItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				SosTabLogItem tbtmLog = new SosTabLogItem(conTabLOG, logTabFolder, Messages);
				logTabFolder.setSelection(tbtmLog);
			}
			@Override
			public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		// =============================================================================================
		MenuItem closeItem = new MenuItem(contentMenu, SWT.PUSH);
		closeItem.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_close));
		closeItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				SosTabLogItem tbtmLog = (SosTabLogItem) logTabFolder.getSelection();
				if (tbtmLog != null) {
					if (logTabFolder.getItemCount() > 1) {
						tbtmLog.dispose();
					} else {
						tbtmLog.clearLog();
					}
				}
			}

			@Override
			public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		// =============================================================================================
	}

	public void open() {
		if (composite != null) {
			dashboardShell = composite.getShell();
		}
		display = Display.getDefault();
		show();
		while (!dashboardShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		System.exit(0);

	}

	public void setDataProvider(final DailyScheduleDataProvider dataProvider_) {
		haveDb = false;
		schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(dataProvider_.getConfigurationFileName());
		detailHistoryDataProvider = new SchedulerHistoryDataProvider(dataProvider_.getConfigurationFileName());
		dailyScheduleDataProvider = dataProvider_;
		dailyScheduleDataProvider.getFilter().setPlannedFrom(new Date());
		dailyScheduleDataProvider.getFilter().setPlannedTo(new Date());
		executedHistoryDataProvider = new SchedulerHistoryDataProvider(dataProvider_.getConfigurationFileName());
		executedHistoryDataProvider.setFrom(new Date());
		executedHistoryDataProvider.setTo(new Date());
		schedulerInstancesDataProvider = new SchedulerInstancesDataProvider(dataProvider_.getConfigurationFileName());
		haveDb = true;
	}
	 
	public void setObjOptions(final SOSDashboardOptions objOptions) {
		this.objOptions = objOptions;
	}

    public void setCurrentUser(SOSJaxbSubject currentUser) {
        this.currentUser = currentUser;
    }

}