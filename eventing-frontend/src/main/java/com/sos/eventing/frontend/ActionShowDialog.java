package com.sos.eventing.frontend;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import sos.ftp.profiles.FTPProfile;
import sos.ftp.profiles.FTPProfileJadeClient;
import sos.ftp.profiles.FTPProfilePicker;
import sos.scheduler.consoleviews.events.SOSActions;
import sos.scheduler.consoleviews.events.SOSEvaluateEvents;
import sos.scheduler.consoleviews.events.SOSEventCommand;
import sos.scheduler.consoleviews.events.SOSEventCommandElement;
import sos.scheduler.consoleviews.events.SOSEventGroups;
import sos.scheduler.consoleviews.events.SchedulerEvent;
import sos.settings.SOSProfileSettings;
import sos.settings.SOSSettings;
import sos.util.SOSFile;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.VirtualFileSystem.common.SOSFileEntry;
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dialog.swtdesigner.SWTResourceManager;

public class ActionShowDialog extends JSToolBox {

    private static final Logger LOGGER = Logger.getLogger(ActionShowDialog.class);
    private static final String SECTION_PLUGIN_ACTION_SHOW_DIALOG = "plugin_action_show_dialog";
    protected Composite composite = null;
    private Tree actionTree = null;
    private Scheduler scheduler = null;
    private Shell eventsInSchedulerShell = null;
    private Display display;
    private Timer timer;
    private int refresh;
    private String configurationDirectory;
    private Preferences prefs;
    private Label msg;
    private Table tableEvents = null;
    private Label logic = null;
    private File configuration_file = null;
    private SOSEvaluateEvents evaluateEvents = null;
    private Combo cboListOfFiles = null;
    private FTPProfilePicker ftpProfilePicker = null;
    private SOSLogger sosLogger = null;

    private class RefreshTask extends TimerTask {

        public void run() {
            if (display == null) {
                display = Display.getDefault();
            }
            display.syncExec(new Runnable() {
                public void run() {
                    try {
                        refresh();
                    } catch (DOMException e) {
                        LOGGER.error(e.getMessage(), e);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            });
        }
    }

    public class Xml_parser_handler extends org.xml.sax.helpers.DefaultHandler {

        public void warning(SAXParseException e) throws SAXException {
            throw e;
        }

        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        public void fatalError(SAXParseException e) throws SAXException {
            throw e;
        }
    }

    public ActionShowDialog(Composite composite_, Scheduler scheduler_, String configurationDirectory_, SOSLogger sosLogger_) throws Exception {
        super(DashBoardConstants.conPropertiesFileName);
        this.sosLogger = sosLogger_;
        this.configurationDirectory = "";
        this.scheduler = scheduler_;
        this.configurationDirectory = configurationDirectory_;
        this.composite = composite_;
        this.evaluateEvents = new SOSEvaluateEvents(scheduler.getHostName(), Integer.parseInt(scheduler.getTcpPort()));
    }

    private void setBackgroundChild(TreeItem item, int a, int b, int c) {
        TreeItem[] t = item.getItems();
        for (int i = 0; i < t.length; i++) {
            TreeItem child = t[i];
            if (a >= 0) {
                child.setBackground(SWTResourceManager.getColor(a, b, c));
            } else {
                child.setBackground(SWTResourceManager.getColor(255, 255, 255));
            }
        }
    }

    private void refreshItem(TreeItem actItem) {
        TreeItem[] t = actItem.getItems();
        for (int i = 0; i < t.length; i++) {
            TreeItem item = t[i];
            SOSActions a = (SOSActions) item.getData();
            if (a.isActive(evaluateEvents.getListOfActiveEvents())) {
                item.setBackground(SWTResourceManager.getColor(255, 0, 0));
                setBackgroundChild(item, 255, 0, 0);
            } else {
                item.setBackground(null);
                setBackgroundChild(item, 255, 255, 255);
            }
        }
    }

    protected void refresh() throws DOMException, Exception {
        try {
            evaluateEvents.buildEventsFromXMl();
            TreeItem[] t = actionTree.getItems();
            if (actionTree.getSelectionCount() > 0) {
                SOSActions a = (SOSActions) actionTree.getSelection()[0].getData();
                fillEventTables(a);
            }
            for (int i = 0; i < t.length; i++) {
                TreeItem item = t[i];
                SOSActions a = (SOSActions) item.getData();
                // Tree aktualisieren
                if (a.isActive(evaluateEvents.getListOfActiveEvents())) {
                    item.setBackground(SWTResourceManager.getColor(255, 0, 0));
                } else {
                    item.setBackground(null);
                }
                refreshItem(item);
            }
        } catch (SAXException e1) {
            //
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage(), e1);
        }
    }

    private void fillTreeItem(SOSActions a, TreeItem item) {
        Iterator<SOSEventCommand> iCommands = a.getListOfCommands().iterator();
        while (iCommands.hasNext()) {
            SOSEventCommand ec = iCommands.next();
            TreeItem command_item = new TreeItem(item, SWT.NONE);
            if ("".equals(ec.getAttribute("name"))) {
                command_item.setFont(SWTResourceManager.getFont("", 8, SWT.NORMAL));
                command_item.setText(ec.getCommand().getNodeName());
            } else {
                command_item.setFont(SWTResourceManager.getFont("", 8, SWT.ITALIC));
                command_item.setText(ec.getAttribute("name"));
            }
            command_item.setData(a);
            Iterator<SOSEventCommandElement> iCommandElements = ec.getListOfCommandElements().iterator();
            while (iCommandElements.hasNext()) {
                SOSEventCommandElement ece = iCommandElements.next();
                TreeItem command_item_action = new TreeItem(command_item, SWT.NONE);
                command_item_action.setText(ece.node2String());
                command_item_action.setData(a);
            }
        }
    }

    private void getConfigurationFiles() throws Exception {
        String fileSpec = "(\\..*)?\\.actions\\.xml$";
        this.cboListOfFiles.removeAll();
        Vector specialFiles = SOSFile.getFilelist(this.configurationDirectory, fileSpec, 0);
        Iterator<File> iter = specialFiles.iterator();
        while (iter.hasNext()) {
            File actionEventHandler = iter.next();
            if (actionEventHandler.exists() && actionEventHandler.canRead()) {
                this.cboListOfFiles.add(actionEventHandler.getName());
            }
        }
    }

    private void fillTree() throws DOMException, Exception {
        actionTree.removeAll();
        tableEvents.removeAll();
        if (configuration_file != null) {
            evaluateEvents.readConfigurationFile(configuration_file);
            Iterator<SOSActions> iActions = evaluateEvents.getListOfActions().iterator();
            while (iActions.hasNext()) {
                SOSActions a = iActions.next();
                TreeItem item = new TreeItem(actionTree, SWT.NONE);
                item.setFont(SWTResourceManager.getFont("", 8, SWT.BOLD));
                item.setText(a.getName());
                fillTreeItem(a, item);
                item.setData(a);
            }
        }
    }

    private void fillEventTables(SOSActions a) {
        if (a != null) {
            tableEvents.clearAll();
            int l = tableEvents.getItemCount();
            for (int i = 0; i < l; i++) {
                TableItem t = tableEvents.getItem(tableEvents.getItemCount() - 1);
                t.dispose();
            }
            logic.setText(a.getLogic());
            Iterator<SOSEventGroups> i = a.getListOfEventGroups().iterator();
            while (i.hasNext()) {
                SOSEventGroups evg = i.next();
                String group = evg.getGroup();
                Iterator<SchedulerEvent> iEvents = evg.getListOfEvents().iterator();
                while (iEvents.hasNext()) {
                    final TableItem newItemTableItem = new TableItem(tableEvents, SWT.BORDER);
                    if ("and".equals(evg.getLogic())) {
                        newItemTableItem.setBackground(SWTResourceManager.getColor(255, 255, 128));
                    }
                    if ("or".equals(evg.getLogic())) {
                        newItemTableItem.setBackground(SWTResourceManager.getColor(206, 231, 255));
                    }
                    newItemTableItem.setText(group);
                    newItemTableItem.setFont(0, SWTResourceManager.getFont("", 8, SWT.BOLD));
                    SchedulerEvent event = iEvents.next();
                    event.setLogic(evg.getLogic());
                    newItemTableItem.setData(event);
                    newItemTableItem.setText(1, event.getEventTitle());
                    newItemTableItem.setText(2, event.getEventName());
                    newItemTableItem.setText(3, event.getEventId());
                    newItemTableItem.setText(4, event.getEventClass());
                    if (!"".equals(event.getJobName())) {
                        newItemTableItem.setText(5, event.getJobName());
                    } else {
                        newItemTableItem.setText(5, event.getJobChain());
                    }
                    newItemTableItem.setText(6, evaluateEvents.getEventStatus(event));
                    newItemTableItem.setText(7, event.getCreated());
                    newItemTableItem.setText(8, event.getExpires());
                    newItemTableItem.setText(9, event.getComment());
                    if (evg.isActiv(evaluateEvents.getListOfActiveEvents())) {
                        newItemTableItem.setBackground(0, SWTResourceManager.getColor(0, 255, 64));
                    }
                    if ("active".equalsIgnoreCase(newItemTableItem.getText(6))) {
                        newItemTableItem.setFont(6, SWTResourceManager.getFont("", 8, SWT.BOLD));
                        newItemTableItem.setBackground(6, SWTResourceManager.getColor(0, 255, 64));
                    } else {
                        newItemTableItem.setBackground(6, SWTResourceManager.getColor(255, 0, 0));
                        newItemTableItem.setFont(6, SWTResourceManager.getFont("", 8, SWT.ITALIC));
                    }
                    group = "";
                }
            }
        }
    }

    private void createContents(Composite parent) throws Exception {
        prefs = Preferences.userNodeForPackage(this.getClass());
        final GridLayout gridLayout = new GridLayout(6, false);
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
        parent.setLayout(gridLayout);
        eventsInSchedulerShell.addShellListener(new ShellAdapter() {

            public void shellActivated(final ShellEvent e) {

            }
        });
        timer = new Timer();
        timer.schedule(new RefreshTask(), 1000, 5000);
        final Button refreshButton = new Button(parent, SWT.NONE);
        refreshButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                try {
                    refresh();
                } catch (DOMException e1) {
                    LOGGER.error(e1.getMessage(), e1);
                } catch (Exception e1) {
                    LOGGER.error(e1.getMessage(), e1);
                }
            }
        });
        refreshButton.setLayoutData(new GridData(91, GridData.HORIZONTAL_ALIGN_BEGINNING));
        refreshButton.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Refresh));
        final Text refreshInterval = new Text(parent, SWT.RIGHT | SWT.BORDER);
        refreshInterval.setText("300");
        refresh = getIntValue(refreshInterval.getText(), 300);
        refreshInterval.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent arg0) {
                refresh = getIntValue(refreshInterval.getText(), 300);
                timer.cancel();
                timer = new Timer();
                timer.schedule(new RefreshTask(), refresh * 1000, refresh * 1000);
                prefs.put("refresh", refreshInterval.getText());

            }
        });
        final GridData gd_refreshInterval = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_refreshInterval.minimumWidth = 50;
        refreshInterval.setLayoutData(gd_refreshInterval);
        logic = new Label(parent, SWT.NONE);
        logic.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        logic.setFont(SWTResourceManager.getFont("", 12, SWT.BOLD));
        logic.setText("                                ");
        cboListOfFiles = new Combo(parent, SWT.READ_ONLY);
        cboListOfFiles.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent arg0) {

            }
        });
        cboListOfFiles.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {

                try {
                    if (cboListOfFiles.getText().startsWith("ftp:")) {
                        FTPProfile profile = ftpProfilePicker.getSelectedFTPProfile();
                        FTPProfileJadeClient fptProfileJadeClient = new FTPProfileJadeClient(profile);
                        SOSFileEntry sosFileEntry = new SOSFileEntry();
                        sosFileEntry.setDirectory(false);
                        sosFileEntry.setFilename(cboListOfFiles.getText().substring(4));
                        sosFileEntry.setParentPath(profile.getRoot());
                        fptProfileJadeClient.copyRemoteFileToLocal(sosFileEntry);
                        configuration_file = new File(profile.getLocaldirectory(), sosFileEntry.getFilename());
                        fillTree();
                        configuration_file.delete();
                    } else {
                        configuration_file = new File(getConfigurationDirectory() + "/" + cboListOfFiles.getText());
                        fillTree();
                    }
                } catch (DOMException e1) {
                    LOGGER.error(e1.getMessage(), e1);
                } catch (Exception e1) {
                    LOGGER.error(e1.getMessage(), e1);
                }
            }

            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });
        final GridData gd_listOfFiles = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_listOfFiles.widthHint = 105;
        cboListOfFiles.setLayoutData(gd_listOfFiles);
        getConfigurationFiles();
        final Label ftpLabel = new Label(parent, SWT.NONE);
        ftpLabel.setAlignment(SWT.RIGHT);
        ftpLabel.setText("FTP");
        File f = new File(configurationDirectory, "ftp_profiles.ini");
        ftpProfilePicker = new FTPProfilePicker(parent, SWT.NONE, f);
        try {
            ftpProfilePicker.addEmptyItem();
        } catch (Exception e) {
        }
        ftpProfilePicker.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent arg0) {
            }
        });
        ftpProfilePicker.setLogger(sosLogger);
        ftpProfilePicker.setButtonText("Profile");
        ftpProfilePicker.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                msg.setText("try to connect..");
                try {
                    FTPProfile profile = ftpProfilePicker.getSelectedFTPProfile();
                    FTPProfileJadeClient ftpProfileJadeClient = new FTPProfileJadeClient(profile);
                    if (profile != null) {
                        sosLogger.info("user: " + profile.getUser());
                        sosLogger.info("root: " + profile.getRoot());
                        Iterator<String> iter = null;
                        sosLogger.debug6("get list of files");
                        Vector<String> v = ftpProfileJadeClient.getList(profile.getRoot());
                        iter = v.iterator();
                        cboListOfFiles.removeAll();
                        sosLogger.debug6("reading files");
                        while (iter != null && iter.hasNext()) {
                            File file = new File(iter.next());
                            sosLogger.debug6("... reading " + file.getName());
                            if (file.getName().toLowerCase().endsWith(".actions.xml")) {
                                String entry = "ftp:" + file.getName();
                                if (cboListOfFiles.indexOf(entry) < 0) {
                                    cboListOfFiles.add(entry);
                                }
                            }
                        }
                    } else {
                        getConfigurationFiles();
                    }
                } catch (Exception ex) {
                    try {
                        sosLogger.warn("Error while connecting " + ex.getMessage());
                    } catch (Exception e1) {
                        LOGGER.error(e1.getMessage(), e1);
                    }
                }

            }
        });
        final GridData gd_ftpProfile = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_ftpProfile.minimumWidth = 100;
        ftpProfilePicker.setLayoutData(gd_ftpProfile);
        ftpProfilePicker.setBounds(0, 0, 242, 24);
        actionTree = new Tree(parent, SWT.FULL_SELECTION | SWT.BORDER);
        actionTree.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (actionTree.getSelectionCount() > 0) {
                    SOSActions a = (SOSActions) actionTree.getSelection()[0].getData();
                    logic.setText(a.getLogic());
                    fillEventTables(a);
                }
            }
        });
        actionTree.setLinesVisible(true);
        final GridData gd_actionTree = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_actionTree.widthHint = 191;
        actionTree.setLayoutData(gd_actionTree);
        tableEvents = new Table(parent, SWT.BORDER);
        tableEvents.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {

            }
        });
        tableEvents.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SchedulerEvent event = (SchedulerEvent) e.item.getData();
                logic.setText(event.getLogic());
            }
        });
        tableEvents.setLinesVisible(true);
        tableEvents.setHeaderVisible(true);
        final GridData gd_tableEvents = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
        gd_tableEvents.verticalIndent = -1;
        gd_tableEvents.heightHint = 295;
        gd_tableEvents.widthHint = 339;
        tableEvents.setLayoutData(gd_tableEvents);
        final TableColumn tbColumnGroup = new TableColumn(tableEvents, SWT.NONE);
        tbColumnGroup.setWidth(50);
        tbColumnGroup.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + tbColumnGroup.getText()).put("width", String.valueOf(tbColumnGroup.getWidth()));
            }
        });
        tbColumnGroup.setText("Event Group");
        tbColumnGroup.setWidth(this.getIntValue(prefs.node("table/col/" + tbColumnGroup.getText()).get("width", "100"), 100));
        final TableColumn tbColumnTitle = new TableColumn(tableEvents, SWT.NONE);
        tbColumnTitle.setWidth(50);
        tbColumnTitle.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + tbColumnTitle.getText()).put("width", String.valueOf(tbColumnTitle.getWidth()));

            }
        });
        tbColumnTitle.setText("Title");
        tbColumnTitle.setWidth(this.getIntValue(prefs.node("table/col/" + tbColumnTitle.getText()).get("width", "100"), 100));
        final TableColumn tbColumnName = new TableColumn(tableEvents, SWT.NONE);
        tbColumnName.setWidth(50);
        tbColumnName.setText("Name");
        final TableColumn tbColumnEventId = new TableColumn(tableEvents, SWT.NONE);
        tbColumnEventId.setWidth(50);
        tbColumnEventId.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + tbColumnEventId.getText()).put("width", String.valueOf(tbColumnEventId.getWidth()));

            }
        });
        tbColumnEventId.setText("Event");
        tbColumnEventId.setWidth(this.getIntValue(prefs.node("table/col/" + tbColumnEventId.getText()).get("width", "100"), 100));
        final TableColumn tbColumnClass = new TableColumn(tableEvents, SWT.NONE);
        tbColumnClass.setWidth(50);
        tbColumnClass.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + tbColumnClass.getText()).put("width", String.valueOf(tbColumnClass.getWidth()));

            }
        });
        tbColumnClass.setText("Class");
        tbColumnClass.setWidth(this.getIntValue(prefs.node("table/col/" + tbColumnClass.getText()).get("width", "80"), 80));
        final TableColumn tbColumnJob = new TableColumn(tableEvents, SWT.NONE);
        tbColumnJob.setWidth(50);
        tbColumnJob.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + tbColumnJob.getText()).put("width", String.valueOf(tbColumnJob.getWidth()));

            }
        });
        tbColumnJob.setText("Job / Job Chain");
        tbColumnJob.setWidth(this.getIntValue(prefs.node("table/col/" + tbColumnJob.getText()).get("width", "80"), 80));
        final TableColumn tbColumnStatus = new TableColumn(tableEvents, SWT.NONE);
        tbColumnStatus.setWidth(50);
        tbColumnStatus.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + tbColumnStatus.getText()).put("width", String.valueOf(tbColumnStatus.getWidth()));

            }
        });
        tbColumnStatus.setText("Event Status");
        tbColumnStatus.setWidth(this.getIntValue(prefs.node("table/col/" + tbColumnStatus.getText()).get("width", "170"), 170));
        final TableColumn tbColumnCreated = new TableColumn(tableEvents, SWT.NONE);
        tbColumnCreated.setWidth(50);
        tbColumnCreated.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + tbColumnCreated.getText()).put("width", String.valueOf(tbColumnCreated.getWidth()));

            }
        });
        tbColumnCreated.setText("Created");
        tbColumnCreated.setWidth(this.getIntValue(prefs.node("table/col/" + tbColumnCreated.getText()).get("width", "100"), 100));
        final TableColumn tbColumnExpires = new TableColumn(tableEvents, SWT.NONE);
        tbColumnExpires.setWidth(50);
        tbColumnExpires.addControlListener(new ControlAdapter() {

            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + tbColumnExpires.getText()).put("width", String.valueOf(tbColumnExpires.getWidth()));

            }
        });
        tbColumnExpires.setText("Expires");
        tbColumnExpires.setWidth(this.getIntValue(prefs.node("table/col/" + tbColumnExpires.getText()).get("width", "80"), 80));
        final TableColumn tbColumnComment = new TableColumn(tableEvents, SWT.NONE);
        tbColumnComment.setWidth(100);
        tbColumnComment.setText("Comment");
        new TableColumn(tableEvents, SWT.NONE);
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
        msg = new Label(parent, SWT.NONE);
        msg.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        msg.setText("…");
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
        fillTree();
    }

    public void showShell() throws DOMException, Exception {
        Composite parent = null;
        eventsInSchedulerShell = new Shell();
        parent = eventsInSchedulerShell;
        eventsInSchedulerShell.setSize(782, 478);
        eventsInSchedulerShell.setText("Events in Scheduler " + this.scheduler.getHostName() + ":" + this.scheduler.getTcpPort());
        createContents(parent);
        eventsInSchedulerShell.open();
        eventsInSchedulerShell.layout();
    }

    public void showComposite() throws DOMException, Exception {
        eventsInSchedulerShell = composite.getShell();
        createContents(composite);
        eventsInSchedulerShell.layout();
    }

    public void open() throws DOMException, Exception {
        if (composite != null) {
            eventsInSchedulerShell = composite.getShell();
        }
        display = Display.getDefault();
        showShell();
        while (!eventsInSchedulerShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private int getIntValue(String s, int d) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException n) {
            return d;
        }
    }

    public String getConfigurationDirectory() {
        return configurationDirectory;
    }

    public Table getTableEvents() {
        return tableEvents;
    }

    public void setConfigurationDirectory(String configurationDirectory) {
        this.configurationDirectory = configurationDirectory;
        try {
            getConfigurationFiles();
        } catch (Exception e) {
            msg.setText(String.format("Could read directory: %s", configurationDirectory));
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Timer getTimer() {
        return timer;
    }

    public void resetRefreshTimer() {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new RefreshTask(), refresh * 1000, refresh * 1000);
    }

    public void disableRefresh() {
        timer.cancel();
    }

    public static void main(final String[] args) {
        try {
            String host = "localhost";
            String port = "4445";
            Shell shell = new Shell();
            Composite composite = new Composite(shell, SWT.NONE);
            Scheduler scheduler = new Scheduler();
            SOSSettings profileSettings = new SOSProfileSettings("./config/settings.ini");
            if (profileSettings.getSection(SECTION_PLUGIN_ACTION_SHOW_DIALOG) != null) {
                host = profileSettings.getSection(SECTION_PLUGIN_ACTION_SHOW_DIALOG).getProperty("host");
            }
            if (host == null || host.isEmpty()) {
                host = "localhost";
            }
            if (profileSettings.getSection(SECTION_PLUGIN_ACTION_SHOW_DIALOG) != null) {
                port = profileSettings.getSection(SECTION_PLUGIN_ACTION_SHOW_DIALOG).getProperty("port");
            }
            if (port == null || port.isEmpty()) {
                port = "4445";
            }
            String configurationsDirectory = profileSettings.getSection(SECTION_PLUGIN_ACTION_SHOW_DIALOG).getProperty("configuration_dirctory");
            scheduler.setHostName(host);
            scheduler.setTcpPort(port);
            SOSStandardLogger sosLogger = new SOSStandardLogger(9);
            ActionShowDialog window = new ActionShowDialog(composite, scheduler, configurationsDirectory, sosLogger);
            window.open();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}