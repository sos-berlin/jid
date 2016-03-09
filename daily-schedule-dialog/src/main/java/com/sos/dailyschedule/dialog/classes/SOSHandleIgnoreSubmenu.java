package com.sos.dailyschedule.dialog.classes;

import java.util.prefs.Preferences;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.hibernate.classes.DbItem;
import com.sos.jid.dialog.classes.SOSDashboardTableView;
import com.sos.jid.dialog.classes.SOSDialogHandleIgnoreList;
import com.sos.localization.Messages;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public abstract class SOSHandleIgnoreSubmenu extends Menu {

    private final String className = "SOSHandleIgnoreSubmenu";
    protected Messages messages = null;
    protected Preferences prefs;
    protected SOSDashboardTableView sosDashboardTableView;
    private boolean ignoreListEnabled = true;
    private MenuItem disableIgnore;

    abstract String getContext();

    abstract void addSpecialContextMenu();

    public SOSHandleIgnoreSubmenu(Menu parent, SOSDashboardTableView sosDashboardTableView_, Messages messages_, Preferences prefs_) {
        super(parent);
        messages = messages_;
        prefs = prefs_;
        sosDashboardTableView = sosDashboardTableView_;
        createMenue(parent);
    }

    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    private Shell getParentShell() {
        return this.getShell();
    }

    public void createMenue(final Menu parent) {

        Menu ignoreSubmenu = new Menu(parent);
        MenuItem ignoreSubmenuItem = new MenuItem(parent, SWT.CASCADE);
        ignoreSubmenuItem.setText("Ignore");
        ignoreSubmenuItem.setMenu(ignoreSubmenu);

        // this is an option to add different sub menu items. Not used in JID
        addSpecialContextMenu();

        MenuItem handleIgnore = new MenuItem(ignoreSubmenu, SWT.PUSH);
        handleIgnore.setText(messages.getLabel(DashBoardConstants.conSOSDashB_handleIgnore));
        handleIgnore.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

                SOSDialogHandleIgnoreList s = new SOSDialogHandleIgnoreList(getParentShell(), prefs, getContext());
                sosDashboardTableView.getTableDataProvider().disableIgnoreList(prefs);
                sosDashboardTableView.getTableDataProvider().setIgnoreList(prefs);
                sosDashboardTableView.getList();

            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });

        // =============================================================================================

        MenuItem addIgnore = new MenuItem(ignoreSubmenu, SWT.PUSH);
        addIgnore.setText(messages.getLabel(DashBoardConstants.conSOSDashB_addIgnore));
        addIgnore.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                TableItem[] t = sosDashboardTableView.getTableList().getSelection();
                if (t.length > 0) {
                    for (int i = 0; i < t.length; i++) {
                        DbItem h = (DbItem) t[i].getData();
                        sosDashboardTableView.getTableDataProvider().addToIgnorelist(prefs, h);
                    }
                }
                sosDashboardTableView.getTableDataProvider().setIgnoreList(prefs);
                sosDashboardTableView.getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });

        // =============================================================================================

        MenuItem addIgnoreJobChainOrder = new MenuItem(ignoreSubmenu, SWT.PUSH);
        addIgnoreJobChainOrder.setText(messages.getLabel(DashBoardConstants.conSOSDashB_addIgnoreJobChainOrder));
        addIgnoreJobChainOrder.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                TableItem[] t = sosDashboardTableView.getTableList().getSelection();
                if (t.length > 0) {
                    // if (t[0].getData().getClass() ==
                    // SchedulerOrderHistoryDBItem.class){
                    DbItem dbItem = (DbItem) t[0].getData();
                    if (dbItem.isOrderJob()) {
                        dbItem.setOrderId(null);
                        sosDashboardTableView.getTableDataProvider().addToIgnorelist(prefs, dbItem);
                    }
                    sosDashboardTableView.getList();
                }
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });

        disableIgnore = new MenuItem(ignoreSubmenu, SWT.PUSH);
        disableIgnore.setText(messages.getLabel(DashBoardConstants.conSOSDashB_disableIgnore));
        disableIgnore.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                ignoreListEnabled = !ignoreListEnabled;
                if (ignoreListEnabled) {
                    sosDashboardTableView.getTableDataProvider().setIgnoreList(prefs);
                    disableIgnore.setText(messages.getLabel(DashBoardConstants.conSOSDashB_disableIgnore));
                } else {
                    sosDashboardTableView.getTableDataProvider().disableIgnoreList(prefs);
                    disableIgnore.setText(messages.getLabel(DashBoardConstants.conSOSDashB_enableIgnore));
                }
                sosDashboardTableView.getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });

        // =============================================================================================
        MenuItem resetIgnore = new MenuItem(ignoreSubmenu, SWT.PUSH);
        resetIgnore.setText(messages.getLabel(DashBoardConstants.conSOSDashB_resetIgnore));
        resetIgnore.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {

                sosDashboardTableView.getTableDataProvider().resetIgnoreList(prefs);
                sosDashboardTableView.getList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });

    }

}
