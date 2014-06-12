package com.sos.dailyschedule.dialog;

import com.sos.JSHelper.Basics.JSVersionInfo;
import com.sos.dailyschedule.DailyScheduleDataProvider;
import com.sos.dailyschedule.dialog.classes.SOSDashboardTableViewExecuted;
import com.sos.dailyschedule.dialog.classes.SOSDashboardTableViewPlanned;
import com.sos.dailyschedule.dialog.classes.SosHistoryTable;
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.dialog.classes.FormBase;
import com.sos.dialog.classes.SOSUrl;
import com.sos.eventing.dialog.classes.SOSTabEVENTS;
import com.sos.hibernate.classes.DbItem;
import com.sos.jid.dialog.classes.*;
import com.sos.jobnet.dialog.classes.SOSTabJOBNET;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;
import com.sos.scheduler.history.SchedulerHistoryDataProvider;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.*;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

/*
 *
 */
public class DashboardShowDialog extends FormBase {
    private static final int RIGHT_MOUSE_BUTTON = 3;
    private static final String LIST_OF_SCHEDULERS = "list_of_schedulers";
	private static final String SOS_DASHBOARD = "SOS_DASHBOARD";
	private static final String TABNAME_DASHBOARD = "Dashboard";
	private static final String TABNAME_SCHEDULER_OPERATIONS_CENTER = "JOC";
	private static final String TABNAME_SCHEDULER_JOE = "JOE";
	private static final String TABNAME_SCHEDULER_EVENTS = "Events";
    private static final String TABNAME_SCHEDULER_JOBNET = "Jobnet";
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

    
	private Table tableHistoryDetail = null;
	public Group left;
	private Group right = null;
	private Group bottom = null;
	private Composite parent = null;
	private CTabFolder mainTabFolder = null;
	private CTabFolder browserTabFolder = null;
	private CTabFolder leftTabFolder = null;
	private CTabFolder logTabFolder = null;
	private Composite jocComposite;
	private Composite joeComposite;
	private Composite eventsComposite;
	private Composite dashboardComposite;

    private SOSTabEVENTS tbtmEvents;
    private SOSTabJOBNET tbtmJobnet;
    // private SOSTabJade tbtmJade;

	private SOSDashboardTableViewExecuted tableViewExecuted;
	private SOSDashboardTableViewPlanned tableViewPlanned;

	private SchedulerHistoryDataProvider detailHistoryDataProvider = null;
	private SchedulerHistoryDataProvider executedHistoryDataProvider = null;
	private DailyScheduleDataProvider dailyScheduleDataProvider = null;

 	
	private SchedulerInstancesDBLayer schedulerInstancesDBLayer;
	private SOSDashboardOptions objOptions = null;

	private boolean haveDb;

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

	private void showBrowser() {
	    SOSUrl url=null;
	    
		if (objOptions != null && objOptions.getEnableJOC().isTrue()) {
			String listOfScheduler = prefs.node(SOS_DASHBOARD).get(LIST_OF_SCHEDULERS, "");
			if (haveDb && listOfScheduler.equals("")) { // Den ersten aus den
														// Instances �ffnen.
				schedulerInstancesDBLayer.initFilter();
				List<SchedulerInstancesDBItem> instances = schedulerInstancesDBLayer.getSchedulerInstancesList();
				if (instances.size() > 0) {
					SchedulerInstancesDBItem schedulerInstancesDBItem = instances.get(0);
					url = new SOSUrl (schedulerInstancesDBItem.getHostName() + ":" + schedulerInstancesDBItem.getTcpPort()); 
				}
			} else {// Den ersten aus den Preferences �ffnen
				String hostPort = listOfScheduler.split(",")[0];
                url = new SOSUrl(hostPort);
			}

			jocComposite = new Composite(mainTabFolder, SWT.NONE);
			jocComposite.setLayout(new GridLayout());

			CTabItem tbtmJOCs = new CTabItem(mainTabFolder, SWT.NONE);
			tbtmJOCs.setText(TABNAME_SCHEDULER_OPERATIONS_CENTER);
			tbtmJOCs.setControl(jocComposite);

			browserTabFolder = new CTabFolder(jocComposite, SWT.NONE);
			createContextMenuBrowserTabfolder(browserTabFolder);
			createContextMenuBrowserTabfolder(mainTabFolder);

			browserTabFolder.setTabPosition(SWT.BOTTOM);
			browserTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

			new SosTabJOC( browserTabFolder, url);
			if (!listOfScheduler.equals("")) { // Die weiteren �ffnen
				String[] hostPorts = listOfScheduler.split(",");
				for (int i = 1; i < hostPorts.length; i++) {
					this.openScheduler(hostPorts[i]);
				}
			}
			browserTabFolder.setSelection(0);
		}

	}

	private void createMainWindow() {
		if (objOptions != null && (objOptions.getEnableJOC().isTrue() || objOptions.getEnableJOE().isTrue() || objOptions.getEnableEvents().isTrue() ||  objOptions.getEnableJobnet().isTrue())) {
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
	
    private void showJobnet() {
        if (objOptions != null && objOptions.getEnableJobnet().isTrue()) {
            tbtmJobnet = new SOSTabJOBNET(objOptions, TABNAME_SCHEDULER_JOBNET, mainTabFolder);
        }
    }

    private void showJade() {
       // if (objOptions != null && objOptions.getEnableJade().isTrue()) {
       // tbtmJade = new SOSTabJade(objOptions, TABNAME_SCHEDULER_JADE, mainTabFolder);
       // }
    }

	
	private void createFormParts() {
		createMainWindow();
		showJoe();
		showBrowser();
		showEvents();
		showJade();
		showJobnet();

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
		tablesSashForm.setWeights(new int[] { 730, 210 });
		dashboardComposite.layout();
        composite.getDisplay().addFilter(SWT.KeyDown, new Listener() {
            public void handleEvent(Event event) {

                if (event.keyCode == SWT.F5) {

                    if (mainTabFolder.getSelection() == null || mainTabFolder.getSelection().getText().equals(TABNAME_DASHBOARD)) {
                        if (leftTabFolder.getSelection() != null && leftTabFolder.getSelectionIndex() == 0) {
                            tableViewPlanned.actualizeList();
                        }
                        else {
                            tableViewExecuted.actualizeList();
                        }
                    }
                    if (mainTabFolder.getSelection() == null || mainTabFolder.getSelection().getText().equals(TABNAME_SCHEDULER_JOBNET)) {
                        if (tbtmJobnet != null) {
                            tbtmJobnet.refresh();
                        }
                    }
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
		dashboardShell.setText(conJOB_SCHEDULER_DASHBOARD + " (" + JSVersionInfo.getVersionString() + ")");
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

		 
		CTabItem tbtmDailyPlan = new CTabItem(leftTabFolder, SWT.NONE);
		tbtmDailyPlan.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_NAME_TAB_PLANNED));
		tbtmDailyPlan.setControl(tableViewPlanned.getTablePlannedComposite());

		CTabItem tbtmHistory = new CTabItem(leftTabFolder, SWT.NONE);
		tbtmHistory.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_NAME_TAB_HISTORY));
		tbtmHistory.setControl(tableViewExecuted.getTableComposite());
 
		leftTabFolder.setSelection(0);
		
		

	}

	private void createRight() {
		tableHistoryDetail = new SosHistoryTable(right, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableHistoryDetail.setEnabled(haveDb);
		createMenueTableHistoryDetail();
		tableHistoryDetail.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (event.button == RIGHT_MOUSE_BUTTON) // rechte maustaste
				{
					setRightMausclick(true);
				} else {
					setRightMausclick(false);
				}
			}
		});
		tableHistoryDetail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (!isRightMouseclick()) {
					showLog(tableHistoryDetail);
				}
			}
		});
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

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		prefs = Preferences.userNodeForPackage(this.getClass());
		if (haveDb) {
			executedHistoryDataProvider.setIgnoreList(prefs);
			String s = executedHistoryDataProvider.getFilter().getTitle();
		}
		dashboardShell = new Shell();
		dashboardShell.setLayout(new GridLayout());
		this.showWaitCursor();
		createFormParts();
		createLeft();
		createRight();
		createBottom();
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

	private void openScheduler(final String hostport_) {
	    
	    SOSUrl url = new SOSUrl(hostport_);
		SosTabJOC tbtmJoc = new SosTabJOC( browserTabFolder, url);
		browserTabFolder.setSelection(tbtmJoc);
		saveSchedulerTabs();
	}

 

	private void saveSchedulerTabs() {
		String listOfScheduler = "";
		CTabItem[] tabs = browserTabFolder.getItems();
		for (CTabItem tab : tabs) {
            SOSUrl url = (SOSUrl)tab.getData();
            if (url != null) {
    			if (listOfScheduler.equals("")) {
    				listOfScheduler = url.getUrlValue();
    			} else {
    				listOfScheduler = listOfScheduler + "," + url.getUrlValue();
    			}
    		}
		}
		prefs.node(SOS_DASHBOARD).put(LIST_OF_SCHEDULERS, listOfScheduler);
	}

	private void createMenueTableHistoryDetail() {
		Menu contentMenuHistory = new Menu(tableHistoryDetail);
		tableHistoryDetail.setMenu(contentMenuHistory);
		// =============================================================================================
		MenuItem showLogHistory = new MenuItem(contentMenuHistory, SWT.PUSH);
		showLogHistory.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_show_log_in_new_tab));
		showLogHistory.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
				SosTabLogItem tbtmLog = new SosTabLogItem(conTabLOG, logTabFolder, Messages);
				logTabFolder.setSelection(tbtmLog);
				showLog(tableHistoryDetail);
			}

			@Override
			public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		final String prefNode = SOS_DASHBOARD + "_TableHistoryDetail";
		SOSMenuLimitItem setLimitItem = new SOSMenuLimitItem(contentMenuHistory, SWT.PUSH,prefs,prefNode);
		setLimitItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
	         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
	           Integer limit = Integer.parseInt(prefs.node(prefNode).get(DashBoardConstants.conSettingLIMIT,String.valueOf(DashBoardConstants.conSettingLIMITDefault)));
	           tableViewExecuted.setHistoryLimit(limit);
               tableViewPlanned.setHistoryLimit(limit);
	         }

	         public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
	         }
	     });
	}
	
	
	   

	private void createContextMenuBrowserTabfolder(final CTabFolder cParent) {
		Menu contentMenu = new Menu(cParent);
		cParent.setMenu(contentMenu);
		MenuItem addItem = new MenuItem(contentMenu, SWT.PUSH);
		addItem.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_open_scheduler));
		addItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			@Override
			public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
			  
				SosDialogGetHostPort s = new SosDialogGetHostPort(dashboardShell);
				if (!s.cancel()) {
					openScheduler(s.getUrl());
					for (int i = 0; i < mainTabFolder.getTabList().length; i++) {
						try {
							if (mainTabFolder.getItem(i).getText().equals(TABNAME_SCHEDULER_OPERATIONS_CENTER)) {
								mainTabFolder.setSelection(i);
							}

						} catch (Exception ee) {
						}
					}
				}
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
				SosTabJOC tbtmJoc = (SosTabJOC) browserTabFolder.getSelection();
				if (tbtmJoc != null) {
					if (browserTabFolder.getItemCount() > 1) {
						tbtmJoc.dispose();
						saveSchedulerTabs();
					}
				}
			}

			@Override
			public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		// =============================================================================================
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
		schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(dataProvider_.getConfigurationFile());
		detailHistoryDataProvider = new SchedulerHistoryDataProvider(dataProvider_.getConfigurationFile());
		dailyScheduleDataProvider = dataProvider_;

		dailyScheduleDataProvider.getFilter().setPlannedFrom(new Date());
		dailyScheduleDataProvider.getFilter().setPlannedTo(new Date());
	//	dailyScheduleDataProvider.getData();

		executedHistoryDataProvider = new SchedulerHistoryDataProvider(dataProvider_.getConfigurationFile());
		executedHistoryDataProvider.setFrom(new Date());
		executedHistoryDataProvider.setTo(new Date());
	//	executedHistoryDataProvider.getData();
		

		haveDb = true;

	}

	
	 
	
	private void showLog(final Table table) {
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

	public void setObjOptions(final SOSDashboardOptions objOptions) {
		this.objOptions = objOptions;
	}

}