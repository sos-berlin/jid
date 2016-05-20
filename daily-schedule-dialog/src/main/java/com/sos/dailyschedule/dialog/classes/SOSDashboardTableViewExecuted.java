package com.sos.dailyschedule.dialog.classes;

import java.util.Date;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.components.SOSMenuItem;
import com.sos.dialog.components.SOSSearchFilter;
import com.sos.dialog.interfaces.ITableView;
import com.sos.hibernate.classes.DbItem;
import com.sos.jid.dialog.classes.SOSDashboardTableView;
import com.sos.jid.dialog.classes.SOSMenuLimitItem;
import com.sos.jid.dialog.classes.SosDashboardHeader;
import com.sos.jid.dialog.classes.SosTabLogItem;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class SOSDashboardTableViewExecuted extends SOSDashboardTableView implements ITableView {

    private static final Logger LOGGER = Logger.getLogger(SOSDashboardTableViewExecuted.class);
    private final String className = "SOSDashboardTableViewExecuted";

    public SOSDashboardTableViewExecuted(Composite composite_) {
        super(composite_);
        colPosForSort = 4;
    }

    private void showLog() {
        this.showLog(tableList);
    }

    private Shell getParentShell() {
        return this.getShell();
    }

    @Override
    public void createMenue() {
        Menu contentMenu = new Menu(tableList);
        tableList.setMenu(contentMenu);
        MenuItem showLogs = new MenuItem(contentMenu, SWT.PUSH);
        showLogs.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_show_log_in_new_tab));
        showLogs.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                SosTabLogItem tbtmLog = new SosTabLogItem(DashBoardConstants.conTabLOG, logTabFolder, Messages);
                logTabFolder.setSelection(tbtmLog);
                showLog();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        new MenuItem(contentMenu, SWT.SEPARATOR);
        final SOSMenuItem standAlone = new SOSMenuItem(contentMenu, SWT.CHECK, className, 
                Messages.getLabel(DashBoardConstants.conSOSDashB_show_stand_alone_jobs), true);
        standAlone.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableDataProvider.setShowJobs(standAlone.getSelection());
                getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        final SOSMenuItem orderJobs = new SOSMenuItem(contentMenu, SWT.CHECK, className, 
                Messages.getLabel(DashBoardConstants.conSOSDashB_show_job_chains));
        orderJobs.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableDataProvider.setShowJobChains(orderJobs.getSelection());
                getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        final SOSMenuItem showRunning = new SOSMenuItem(contentMenu, SWT.CHECK, Messages.getLabel(DashBoardConstants.conSOSDashB_show_running));
        showRunning.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableDataProvider.setShowRunning(showRunning.getSelection());
                buildTable();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        final SOSMenuItem showWithError = new SOSMenuItem(contentMenu, SWT.CHECK, Messages.getLabel(DashBoardConstants.conSOSDashB_show_with_error));
        showWithError.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableDataProvider.setShowWithError(showWithError.getSelection());
                buildTable();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        final SOSMenuItem today = new SOSMenuItem(contentMenu, SWT.CHECK, Messages.getLabel(DashBoardConstants.conSOSDashB_Today));
        today.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (today.getSelection()) {
                    tableDataProvider.setFrom(new Date());
                    tableDataProvider.setTo(new Date());
                }
                getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        new MenuItem(contentMenu, SWT.SEPARATOR);
        MenuItem reset = new MenuItem(contentMenu, SWT.PUSH);
        reset.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Reset_Filter));
        reset.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableDataProvider.resetFilter();
                standAlone.setSelection(true);
                orderJobs.setSelection(true);
                showRunning.setSelection(false);
                showWithError.setSelection(false);
                today.setSelection(false);
                tableDataProvider.setIgnoreList(prefs);
                tableDataProvider.setFrom(new Date());
                tableDataProvider.setTo(new Date());
                sosDashboardHeader.reset();
                getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        new MenuItem(contentMenu, SWT.SEPARATOR);
        Menu ignoreSubmenu = new SOSHandleIgnoreSubmenuExecuted(contentMenu, this, Messages, prefs);
        new MenuItem(contentMenu, SWT.SEPARATOR);
        MenuItem search = new MenuItem(contentMenu, SWT.PUSH);
        search.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Search));
        search.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                SOSSearchFilter sosSearchFilter = new SOSSearchFilter(getParentShell());
                sosSearchFilter.setEnableFilterCheckbox(false);
                sosSearchFilter.execute(EMPTY_STRING);
                if (sosSearchFilter.getSosSearchFilterData() != null
                        && !sosSearchFilter.getSosSearchFilterData().getSearchfield().equals(EMPTY_STRING)) {
                    try {
                        tableDataProvider.setSearchField(sosSearchFilter.getSosSearchFilterData());
                        actualizeList();
                    } catch (Exception ee) {
                        LOGGER.error(ee.getMessage(), ee);
                    }
                }
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        MenuItem stepHistory = new MenuItem(contentMenu, SWT.PUSH);
        stepHistory.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_stepHistory));
        stepHistory.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                TableItem[] t = tableList.getSelection();
                if (t.length > 0 && t[0].getData().getClass() == SchedulerOrderHistoryDBItem.class) {
                    SchedulerOrderHistoryDBItem h = (SchedulerOrderHistoryDBItem) t[0].getData();
                    SOSDialogOrderStepHistory s = 
                            new SOSDialogOrderStepHistory(getParentShell(), objOptions, h, logTabFolder, Messages, tableDataProvider.getTimeZone());
                }
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        if (objOptions != null && objOptions.getEnableJobStart().isTrue()) {
            new MenuItem(contentMenu, SWT.SEPARATOR);
            MenuItem startJob = new MenuItem(contentMenu, SWT.PUSH);
            startJob.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_start_now));
            startJob.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

                public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                    TableItem[] t = tableList.getSelection();
                    setAnswer("");
                    showWaitCursor();
                    if (t.length > 0) {
                        for (int i = 0; i < t.length; i++) {
                            DbItem h = null;
                            if (t[i].getData().getClass() == SchedulerOrderHistoryDBItem.class) {
                                h = (SchedulerOrderHistoryDBItem) t[i].getData();
                            } else {
                                h = (SchedulerTaskHistoryDBItem) t[i].getData();
                            }
                            start(h);
                        }
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
                        messageBox.setMessage(getAnswer());
                        messageBox.open();
                        restoreCursor();
                        getList();
                    }
                }

                public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                }
            });
        }

        MenuItem excel = new MenuItem(contentMenu, SWT.PUSH);
        excel.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Export_To_Excel));
        excel.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableList.createExcelFile();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        if (tableDataProvider != null) {
            tableDataProvider.setShowJobs(standAlone.getSelection());
            tableDataProvider.setShowJobChains(orderJobs.getSelection());
            tableDataProvider.setShowRunning(showRunning.getSelection());
            tableDataProvider.setShowWithError(showWithError.getSelection());
            if (today.getSelection()) {
                tableDataProvider.setFrom(new Date());
                tableDataProvider.setTo(new Date());
            }
        }
        final String prefNode = DashBoardConstants.SOS_DASHBOARD_HEADER + "_" + className;
        SOSMenuLimitItem setLimitItem = new SOSMenuLimitItem(contentMenu, SWT.PUSH, prefs, prefNode);
        setLimitItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                Integer limit = Integer.parseInt(prefs.node(prefNode).get(DashBoardConstants.conSettingLIMIT, 
                        String.valueOf(DashBoardConstants.conSettingLIMITDefault)));
                sosDashboardHeader.setLimit(limit);
                actualizeList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }

    public void createTable() {
        mainViewComposite = new Composite(leftTabFolder, SWT.NONE);
        GridLayout layout = new GridLayout(DashBoardConstants.NUMBER_OF_COLUMNS_IN_GRID, false);
        mainViewComposite.setLayout(layout);
        sosDashboardHeader = new SosDashboardHeader(mainViewComposite, this);
        sosDashboardHeader.setPrefs(prefs);
        sosDashboardHeader.initLimit(DashBoardConstants.SOS_DASHBOARD_HEADER + "_" + className);
        tableList = new SosExecutedTable(mainViewComposite, SWT.FULL_SELECTION | SWT.MULTI);
        super.createTable();
    }

}