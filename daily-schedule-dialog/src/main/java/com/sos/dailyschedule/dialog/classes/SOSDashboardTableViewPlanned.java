package com.sos.dailyschedule.dialog.classes;

import java.util.Date;

import com.sos.scheduler.model.objects.Spooler;
import com.sos.dailyschedule.db.DailyScheduleDBItem;
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.components.SOSMenuItem;
import com.sos.dialog.components.SOSSearchFilter;
import com.sos.dialog.interfaces.ITableView;
import com.sos.hibernate.classes.UtcTimeHelper;
import com.sos.jid.dialog.classes.SOSDashboardTableView;
import com.sos.jid.dialog.classes.SOSMenuLimitItem;
import com.sos.jid.dialog.classes.SosDashboardHeader;
import com.sos.jid.dialog.classes.SosTabLogItem;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;
import com.sos.scheduler.model.SchedulerObjectFactory;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class SOSDashboardTableViewPlanned extends SOSDashboardTableView implements ITableView {

    private static final String CLASSNAME = "SOSDashboardTableViewPlanned";
    private static final Logger LOGGER = Logger.getLogger(SOSDashboardTableViewExecuted.class);

    public SOSDashboardTableViewPlanned(Composite composite_) {
        super(composite_);
        colPosForSort = 4;
    }

    private Shell getParentShell() {
        return this.getShell();
    }

    @Override
    public void createMenue() {
        Menu contentMenu = new Menu(tableList);
        tableList.setMenu(contentMenu);
        MenuItem showLog = new MenuItem(contentMenu, SWT.PUSH);
        showLog.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_show_log_in_new_tab));
        showLog.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                SosTabLogItem tbtmLog = new SosTabLogItem(DashBoardConstants.conTabLOG, logTabFolder, Messages);
                logTabFolder.setSelection(tbtmLog);
                showLog(tableList);
            }
            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        new MenuItem(contentMenu, SWT.SEPARATOR);
        final SOSMenuItem standAlone = new SOSMenuItem(contentMenu, SWT.CHECK, CLASSNAME, 
                Messages.getLabel(DashBoardConstants.conSOSDashB_show_stand_alone_jobs), true);
        standAlone.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableDataProvider.setShowJobs(standAlone.getSelection());
                buildTable();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        final SOSMenuItem orderJobs = new SOSMenuItem(contentMenu, SWT.CHECK, CLASSNAME, 
                Messages.getLabel(DashBoardConstants.conSOSDashB_show_job_chains), true);
        orderJobs.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableDataProvider.setShowJobChains(orderJobs.getSelection());
                buildTable();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        final SOSMenuItem late = new SOSMenuItem(contentMenu, SWT.CHECK, Messages.getLabel(DashBoardConstants.conSOSDashB_show_late));
        late.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableDataProvider.setLate(late.getSelection());
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
                    detailHistoryDataProvider.setTo(new Date());
                }
                getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        final SOSMenuItem executed = new SOSMenuItem(contentMenu, SWT.CHECK, Messages.getLabel(DashBoardConstants.conSOSDashB_only_executed));
        executed.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (executed.getSelection()) {
                    tableDataProvider.setStatus(DashBoardConstants.STATUS_EXECUTED);
                } else {
                    tableDataProvider.setStatus("");
                }
                buildTable();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        final SOSMenuItem waiting = new SOSMenuItem(contentMenu, SWT.CHECK, Messages.getLabel(DashBoardConstants.conSOSDashB_show_waiting));
        waiting.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (waiting.getSelection()) {
                    tableDataProvider.setStatus(DashBoardConstants.STATUS_WAITING);
                } else {
                    tableDataProvider.setStatus("");
                }
                buildTable();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        new MenuItem(contentMenu, SWT.SEPARATOR);
        MenuItem reset = new MenuItem(contentMenu, SWT.PUSH);
        reset.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Reset_Filter));
        reset.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                sosDashboardHeader.reset();
                tableDataProvider.resetFilter();
                tableDataProvider.setFrom(new Date());
                tableDataProvider.setTo(new Date());
                detailHistoryDataProvider.setTo(new Date());
                standAlone.setSelection(true);
                orderJobs.setSelection(true);
                late.setSelection(false);
                today.setSelection(false);
                executed.setSelection(false);
                waiting.setSelection(false);
                getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
        new MenuItem(contentMenu, SWT.SEPARATOR);
        Menu ignoreSubmenu = new SOSHandleIgnoreSubmenuPlanned(contentMenu, this, Messages, prefs);
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
                    SOSDialogOrderStepHistory s = new SOSDialogOrderStepHistory(getParentShell(), objOptions, h, logTabFolder, Messages, 
                            tableDataProvider.getTimeZone());
                }
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });

        new MenuItem(contentMenu, SWT.SEPARATOR);
        if (objOptions != null && objOptions.getEnableJobStart().isTrue()) {
            MenuItem startJob = new MenuItem(contentMenu, SWT.PUSH);
            startJob.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_start_now));
            startJob.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

                public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                    TableItem[] t = tableList.getSelection();
                    setAnswer("");
                    showWaitCursor();
                    if (t.length > 0) {
                        for (int i = 0; i < t.length; i++) {
                            DailyScheduleDBItem h = (DailyScheduleDBItem) t[i].getData();
                            SchedulerInstancesDBItem schedulerInstancesDBItem = start(h);
                            actualizePlan(h, schedulerInstancesDBItem);
                        }
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
                        messageBox.setMessage(getAnswer());
                        messageBox.open();
                        getList();
                        RestoreCursor();
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
        final String prefNode = DashBoardConstants.SOS_DASHBOARD_HEADER + "_" + CLASSNAME;
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
        tableDataProvider.setShowJobs(standAlone.getSelection());
        tableDataProvider.setShowJobChains(orderJobs.getSelection());
        tableDataProvider.setLate(late.getSelection());
        if (today.getSelection()) {
            tableDataProvider.setFrom(new Date());
            tableDataProvider.setTo(new Date());
            detailHistoryDataProvider.setTo(new Date());
        }
        if (executed.getSelection()) {
            tableDataProvider.setStatus(DashBoardConstants.STATUS_EXECUTED);
        } else {
            tableDataProvider.setStatus("");
        }
        if (waiting.getSelection()) {
            tableDataProvider.setStatus(DashBoardConstants.STATUS_WAITING);
        } else {
            tableDataProvider.setStatus("");
        }
    }

    protected void actualizePlan(DailyScheduleDBItem dailyScheduleDBItem, SchedulerInstancesDBItem schedulerInstancesDBItem) {
        UtcTimeHelper utcTimehelper = new UtcTimeHelper();
        Date utcNow = utcTimehelper.getNowUtc();
        if (schedulerInstancesDBItem != null) {
            SchedulerObjectFactory objSchedulerObjectFactory = new SchedulerObjectFactory(schedulerInstancesDBItem.getHostName(), 
                    schedulerInstancesDBItem.getTcpPort());
            objSchedulerObjectFactory.initMarshaller(Spooler.class);
            if (dailyScheduleDBItem.isOrderJob()) {
                try {
                    if (dailyScheduleDBItem.getScheduleExecuted() == null) {
                        schedulerOrderHistoryDBLayer.getFilter().setOrderid(dailyScheduleDBItem.getOrderId());
                        schedulerOrderHistoryDBLayer.getFilter().setJobchain(dailyScheduleDBItem.getJobChain());
                        schedulerOrderHistoryDBLayer.getFilter().setSchedulerId(dailyScheduleDBItem.getSchedulerId());
                        schedulerOrderHistoryDBLayer.getFilter().setStartTime(utcNow);
                        schedulerOrderHistoryDBLayer.getFilter().setOrderCriteria(DashBoardConstants.ORDER_CRITERIA);
                        schedulerOrderHistoryDBLayer.getFilter().setSortMode(DashBoardConstants.SORT_MODE);
                        SchedulerOrderHistoryDBItem orderHistory = schedulerOrderHistoryDBLayer.getOrderHistoryItem();
                        tableDataProvider.beginTransaction();
                        DailyScheduleDBItem dailyScheduleDBItem2 =
                                (DailyScheduleDBItem) tableDataProvider.getSession().load(DailyScheduleDBItem.class, dailyScheduleDBItem.getId());
                        dailyScheduleDBItem2.setScheduleExecuted(utcNow);
                        if (orderHistory != null) {
                            dailyScheduleDBItem2.setSchedulerOrderHistoryId(orderHistory.getHistoryId());
                        }
                        tableDataProvider.update(dailyScheduleDBItem2);
                        tableDataProvider.commit();
                    }
                } catch (Exception ee) {
                    LOGGER.error(ee.getMessage(), ee);
                }
            } else {
                try {
                    if (dailyScheduleDBItem.getScheduleExecuted() == null) {
                        schedulerTaskHistoryDBLayer.getFilter().setJobname(dailyScheduleDBItem.getJob());
                        schedulerTaskHistoryDBLayer.getFilter().setSchedulerId(dailyScheduleDBItem.getSchedulerId());
                        schedulerTaskHistoryDBLayer.getFilter().setStartTime(utcNow);
                        schedulerTaskHistoryDBLayer.getFilter().setOrderCriteria(DashBoardConstants.ORDER_CRITERIA);
                        schedulerTaskHistoryDBLayer.getFilter().setSortMode(DashBoardConstants.SORT_MODE);
                        SchedulerTaskHistoryDBItem taskHistory = schedulerTaskHistoryDBLayer.getHistoryItem();
                        tableDataProvider.beginTransaction();
                        DailyScheduleDBItem dailyScheduleDBItem2 = 
                                (DailyScheduleDBItem) tableDataProvider.getSession().load(DailyScheduleDBItem.class, dailyScheduleDBItem.getId());
                        dailyScheduleDBItem2.setScheduleExecuted(utcNow);
                        if (taskHistory != null) {
                            dailyScheduleDBItem2.setSchedulerHistoryId(taskHistory.getLogId());
                        }
                        tableDataProvider.update(dailyScheduleDBItem2);
                        tableDataProvider.commit();
                    }
                } catch (Exception ee) {
                    LOGGER.error(ee.getMessage(), ee);
                }
            }
        }
    }

    public void createTable() {
        mainViewComposite = new Composite(leftTabFolder, SWT.NONE);
        GridLayout layout = new GridLayout(DashBoardConstants.NUMBER_OF_COLUMNS_IN_GRID, false);
        mainViewComposite.setLayout(layout);
        sosDashboardHeader = new SosDashboardHeader(mainViewComposite, this);
        sosDashboardHeader.setPrefs(prefs);
        sosDashboardHeader.initLimit(DashBoardConstants.SOS_DASHBOARD_HEADER + "_" + CLASSNAME);
        tableList = new SosPlannedTable(mainViewComposite, SWT.FULL_SELECTION | SWT.MULTI);
        super.createTable();
    }

}