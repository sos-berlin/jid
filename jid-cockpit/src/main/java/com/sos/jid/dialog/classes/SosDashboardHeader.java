package com.sos.jid.dialog.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;

import com.sos.JSHelper.Basics.JSToolBox;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
<<<<<<< HEAD
 
=======

>>>>>>> origin/release/1.9
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.components.SOSSearchFilter;
import com.sos.dialog.components.SOSTimeZoneSelector;
import com.sos.dialog.interfaces.ITableView;
import com.sos.hibernate.classes.SOSSearchFilterData;
import com.sos.hibernate.classes.UtcTimeHelper;

public class SosDashboardHeader extends JSToolBox {

    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(SosDashboardHeader.class);

    protected Preferences prefs;
    private String prefNode = "SosDashboardHeader";
    private final String conClassName = "SosDashboardHeader";
    private static final String EMPTY_STRING = "";
    private Display display;
    public Timer refreshTimer;
    public Timer inputTimer;
    private DateTime fromDate = null;
    private DateTime toDate = null;
    private CCombo cbSchedulerId = null;
    private String timeZone = "";
    private ITableView main = null;
    private Text searchField = null;
    private int refresh = 0;
    private Composite parent;
    private Text refreshInterval;
    private Button refreshButton;
    private Label lblBis;
    private Label lbSchedulerID;
    private Label lblVon;
    private Integer limit = -1;
    private SOSSearchFilterData sosSearchFilterData;

    public Text getRefreshInterval() {
        return refreshInterval;
    }

    public class RefreshTask extends TimerTask {

        public void run() {
            if (display == null) {
                display = Display.getDefault();
            }
            display.syncExec(new Runnable() {

                public void run() {
                    main.getList();
                };
            });
        }
    }

    public class InputTask extends TimerTask {

        public void run() {
            if (display == null) {
                display = Display.getDefault();
            }
            display.syncExec(new Runnable() {

                public void run() {
                    if (sosSearchFilterData == null) {
                        sosSearchFilterData = new SOSSearchFilterData();
                    }

                    sosSearchFilterData.setRegularExpression(false);
                    sosSearchFilterData.setSearchfield(getSearchField().getText());
                    sosSearchFilterData.setFiltered(true);

                    main.actualizeList();
                    sosSearchFilterData = null;
                    inputTimer.cancel();

                };
            });
        }
    }

    public SosDashboardHeader(Composite parent_, ITableView main_) {
        super(DashBoardConstants.conPropertiesFileName);

        refreshTimer = new Timer();
        inputTimer = new Timer();
        refreshTimer.schedule(new RefreshTask(), 1000, 60000);
        main = main_;
        parent = parent_;
        createHeader();
    }

    public int getIntValue(String s, int d) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException n) {
            return d;
        }
    }

    public void setRefresh(int refresh) {
        if (refresh <= 0) {
            refresh = 60;
        }
        this.refresh = refresh;
    }

    public void setRefresh(String refresh) {
        setRefresh(getIntValue(refresh, 60));
    }

    public void resetRefreshTimer() {

        refreshTimer.cancel();
        refreshTimer = new Timer();
        refreshTimer.schedule(new RefreshTask(), refresh * 1000, refresh * 1000);
    }

    public void resetInputTimer() {
        inputTimer.cancel();
        inputTimer = new Timer();
        inputTimer.schedule(new InputTask(), 1 * 1000, 1 * 1000);
    }

    private void createHeader() {
        refreshButton = new Button(parent, SWT.NONE);
        refreshButton.setLayoutData(new GridData(74, SWT.DEFAULT));
        refreshButton.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Refresh));
        refreshInterval = new Text(parent, SWT.RIGHT | SWT.BORDER);
        refresh = getIntValue(refreshInterval.getText(), 60);
        final GridData gdRefreshInterval = new GridData(35, SWT.DEFAULT);
        gdRefreshInterval.minimumWidth = 50;
        refreshInterval.setLayoutData(gdRefreshInterval);
        lbSchedulerID = new Label(parent, SWT.NONE);
        lbSchedulerID.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_SchedulerID));

        cbSchedulerId = new CCombo(parent, SWT.BORDER);
        GridData gdCombo = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        gdCombo.widthHint = 120;
        gdCombo.minimumWidth = 120;
        cbSchedulerId.setLayoutData(gdCombo);

        lblVon = new Label(parent, SWT.NONE);
        lblVon.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblVon.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_FROM));
        fromDate = new DateTime(parent, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);
        lblBis = new Label(parent, SWT.NONE);
        lblBis.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblBis.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_TO));
        toDate = new DateTime(parent, SWT.BORDER | SWT.DATE | SWT.DROP_DOWN);

        searchField = new Text(parent, SWT.BORDER);
        searchField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        searchField.setVisible(true);

    }

    public String getTimeZone() {
        String t = this.timeZone;
        if (t == null || t.trim().length() == 0) {
            t = UtcTimeHelper.localTimeZoneString();
        }
        return t;
    }

    private Listener getSearchListener() {

        return new Listener() {

            public void handleEvent(Event e) {
                SOSSearchFilter sosSearchFilter = new SOSSearchFilter(parent.getShell());
                sosSearchFilter.setEnableFilterCheckbox(false);
                sosSearchFilterData = sosSearchFilter.execute(EMPTY_STRING);
                if (sosSearchFilter.getSosSearchFilterData() != null) {
                    if (!sosSearchFilter.getSosSearchFilterData().getSearchfield().equals(EMPTY_STRING)) {
                        try {
                            searchField.setText(sosSearchFilter.getSosSearchFilterData().getSearchfield());
                        } catch (Exception ee) {
                            logger.error(ee.getMessage(),ee);
                        }

                    }
                }
            }
        };
    }

    private Listener getTimeZoneListener() {

        return new Listener() {

            public void handleEvent(Event e) {
                SOSTimeZoneSelector sosTimeZoneSelector = new SOSTimeZoneSelector(parent.getShell());
                setTimeZone(sosTimeZoneSelector.execute(getTimeZone()));
                prefs.node(DashBoardConstants.SOS_DASHBOARD_HEADER).put(DashBoardConstants.conSettingTIMEZONE, getTimeZone());
                main.getTableDataProvider().setTimeZone(getTimeZone());
                main.actualizeList();
            }
        };
    }

    public void createMenue() {
        Menu contentMenu = new Menu(refreshButton);
        refreshButton.setMenu(contentMenu);
        parent.setMenu(contentMenu);
        lblBis.setMenu(contentMenu);
        lbSchedulerID.setMenu(contentMenu);
        lblVon.setMenu(contentMenu);
        toDate.setMenu(contentMenu);
        fromDate.setMenu(contentMenu);

        // =============================================================================================

<<<<<<< HEAD
        
  /*      MenuItem itemSearch = new MenuItem(contentMenu, SWT.PUSH);
        itemSearch.addListener(SWT.Selection, getSearchListener());
        itemSearch.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Search));
        */
       
=======
        MenuItem itemSearch = new MenuItem(contentMenu, SWT.PUSH);
        itemSearch.addListener(SWT.Selection, getSearchListener());
        itemSearch.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Search));

>>>>>>> origin/release/1.9
        // =============================================================================================
        MenuItem itemTimeZone = new MenuItem(contentMenu, SWT.PUSH);
        itemTimeZone.addListener(SWT.Selection, getTimeZoneListener());
        itemTimeZone.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_TimeZone));

        // =============================================================================================
        SOSMenuLimitItem setLimitItem = new SOSMenuLimitItem(contentMenu, SWT.PUSH, prefs, prefNode);
        setLimitItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                limit = -1;
                main.actualizeList();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }

    public void initLimit(String prefNode_) {
        prefNode = prefNode_;
        createMenue();

    }

    public DateTime getFromDate() {
        return fromDate;
    }

    public Date getFrom() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, fromDate.getDay());
        cal.set(Calendar.MONTH, fromDate.getMonth());
        cal.set(Calendar.YEAR, fromDate.getYear());
        return cal.getTime();
    }

    public Text getSearchField() {
        return searchField;
    }

    public DateTime getToDate() {
        return toDate;
    }

    public Date getTo() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, toDate.getDay());
        cal.set(Calendar.MONTH, toDate.getMonth());
        cal.set(Calendar.YEAR, toDate.getYear());
        return cal.getTime();
    }

    public CCombo getCbSchedulerId() {
        return cbSchedulerId;
    }

    public void setEnabled(boolean enabled) {
        refreshButton.setEnabled(enabled);
        searchField.setEnabled(false);
        toDate.setEnabled(enabled);
        fromDate.setEnabled(enabled);
        cbSchedulerId.setEnabled(enabled);
        refreshInterval.setEnabled(enabled);
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public void reset() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date()); // heute

        fromDate.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        toDate.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cbSchedulerId.setText("");
        searchField.setText("");
        sosSearchFilterData = new SOSSearchFilterData();
        sosSearchFilterData.setSearchfield("");
    }

    public Timer getRefreshTimer() {
        return refreshTimer;
    }

    public int getLimit() {
        if (limit != -1) {
            return limit;
        } else {
            int defaultLimit = DashBoardConstants.conSettingLIMITDefault;
            try {
                limit = Integer.parseInt(prefs.node(prefNode).get(DashBoardConstants.conSettingLIMIT, String.valueOf(defaultLimit)));
            } catch (NumberFormatException e) {
                limit = defaultLimit;
            }
        }
        return limit;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public SOSSearchFilterData getSosSearchFilterData() {
        return sosSearchFilterData;
    }

    public void setRefreshInterval(Text refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
