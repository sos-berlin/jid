package com.sos.eventing.frontend;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.dashboard.globals.DashBoardConstants;

import sos.scheduler.command.SOSSchedulerCommand;
import sos.scheduler.job.JobSchedulerConstants;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

public class EventShowDialog extends JSToolBox {

    private static Logger logger = Logger.getLogger(EventShowDialog.class);
    private static final String DEFAULT_JOBSCHEDULER_PORT = "4444";
    private static final String DEFAULT_JOBSCHEDULER_HOST_NAME = "localhost";
    protected Composite composite = null;
    private Table table = null;
    private Scheduler scheduler = null;
    private String host;
    private int port;
    private Shell eventsInSchedulerShell = null;
    private Label msg;
    private Display display;
    private Timer timer;
    private EventSortBaseComapator[][] comparables = null;
    private int refresh;
    private Preferences prefs;
    private MyStandardLogger sosLogger = null;
    private SOSSchedulerCommand socket;
    private boolean enableRefresh = false;

    class MyStandardLogger {

        private SOSLogger sosLogger = null;

        public MyStandardLogger(final SOSLogger sosLogger_) {
            sosLogger = sosLogger_;
        }

        public void warn(final String s) {
            try {
                sosLogger.warn(s);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        public void info(final String s) {
            try {
                sosLogger.info(s);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        public void debug3(final String s) {
            try {
                sosLogger.debug3(s);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

    }

    class EventSortBaseComapator implements Comparable {

        String[] _textBuffer;
        int _oldRowNum;
        int _colPos;
        boolean _sortFlag;

        public EventSortBaseComapator(final String[] textBuffer, final int oldRowNum, final int colPos) {
            _textBuffer = textBuffer;
            _oldRowNum = oldRowNum;
            _colPos = colPos;
        }

        @Override
        public boolean equals(final Object o) {
            return super.equals(o);
        }
        
        @Override
        public int hashCode() {
            return _colPos;
        }
        
        @Override
        public int compareTo(final Object o) {
            return 0;
        }
    }

    class StringComparator extends EventSortBaseComapator implements Comparable {

        public StringComparator(final String[] textBuffer, final int rowNum, final int colPos) {
            super(textBuffer, rowNum, colPos);
        }

        @Override
        public boolean equals(final Object o) {
            return _textBuffer[_colPos].equals(((StringComparator) o)._textBuffer[_colPos]);
        }
        
        @Override
        public int hashCode() {
            return _colPos;
        }
        
        @Override
        public final int compareTo(final Object arg0) {
            int ret = _textBuffer[_colPos].compareTo(((StringComparator) arg0)._textBuffer[_colPos]);
            return _sortFlag ? ret : ret * -1;
        }

    }

    class EventSortNumberComparator extends EventSortBaseComapator implements Comparable {

        int _intValue;

        public EventSortNumberComparator(final String[] textBuffer, final int rowNum, final int colPos) {
            super(textBuffer, rowNum, colPos);
            try {
                _intValue = Integer.parseInt(textBuffer[colPos]);
            } catch (NumberFormatException n) {
                _intValue = 0;
            }
        }

        @Override
        public boolean equals(final Object o) {
            int _intValToCompare = ((EventSortNumberComparator) o)._intValue;
            return _intValue == _intValToCompare;
        }

        @Override
        public int hashCode() {
            return _intValue;
        }

        @Override
        public final int compareTo(final Object o) {
            int _intValToCompare = ((EventSortNumberComparator) o)._intValue;
            int ret = _intValue < _intValToCompare ? -1 : _intValue == _intValToCompare ? 0 : 1;
            return _sortFlag ? ret : ret * -1;
        }
 
    }

    public class RefreshTask extends TimerTask {

        @Override
        public void run() {

            if (display == null) {
                display = Display.getDefault();
            }
            display.syncExec(new Runnable() {

                @Override
                public void run() {
                    refresh();
                };
            });
        }
    }

    public class Xml_parser_handler extends org.xml.sax.helpers.DefaultHandler {

        @Override
        public void warning(final SAXParseException e) throws SAXException {
            throw e;
        }

        @Override
        public void error(final SAXParseException e) throws SAXException {
            throw e;
        }

        @Override
        public void fatalError(final SAXParseException e) throws SAXException {
            throw e;
        }
    }

    public EventShowDialog(final Composite composite_, final Scheduler scheduler_, final SOSLogger sosLogger_) {

        super(DashBoardConstants.conPropertiesFileName);
        scheduler = scheduler_;
        host = scheduler_.getHostName();
        port = Integer.parseInt(scheduler_.getTcpPort());
        composite = composite_;
        sosLogger = new MyStandardLogger(sosLogger_);
    }

    private String getText(final Node n) {
        if (n != null) {
            return n.getNodeValue();
        } else {
            return "";
        }
    }

    private String sendCommand(final String command) {
        String s = "";
        sosLogger.debug3("...sendCommand: " + command);
        try {

            if (socket == null) {
                socket = new SOSSchedulerCommand();
                socket.connect(host, port);
                socket.sendRequest(command);
                s = socket.getResponse();
            } else {
                socket.sendRequest(command);
                s = socket.getResponse();
            }
        } catch (Exception ee) {
            sosLogger.warn("Error sending command to Job Scheduler: " + ee.getMessage());

        } finally {

        }
        return s;
    }

    protected void refresh() {
        sosLogger.debug3("... refresh");
        try {
            buildEventsFromXMl();
        } catch (SAXException e1) {
            msg.setText("XML-Answer from Scheduler was invalid");
            ;
        } catch (IOException e1) {
            logger.error(e1.getMessage(), e1);
        }
    }

    private void buildEventsFromXMl() throws SAXException, IOException {
        sosLogger.debug3("... buildEventsFromXMl");

        String response = sendCommand("<param.get name=\"" + JobSchedulerConstants.eventVariableName + "\"/>");
        if (response.equals("")) {
            msg.setText("No Answer from Scheduler " + host + ":" + port);
            sosLogger.warn("No Answer from Scheduler " + host + ":" + port);
        } else {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            try {
                table.clearAll();
                int l = table.getItemCount();
                for (int i = 0; i < l; i++) {
                    TableItem t = table.getItem(table.getItemCount() - 1);
                    t.dispose();
                }

                docBuilder = docFactory.newDocumentBuilder();

                Document doc = docBuilder.parse(new InputSource(new StringReader(response)));
                NodeList params = doc.getElementsByTagName("param");
                if (params.item(0) == null) {
                    msg.setText("No events param found in Job Scheduler answer");
                } else {
                    NamedNodeMap attr = params.item(0).getAttributes();
                    String eventString = getText(attr.getNamedItem("value"));
                    eventString = eventString.replaceAll(String.valueOf((char) 254), "<").replaceAll(String.valueOf((char) 255), ">");

                    docFactory = DocumentBuilderFactory.newInstance();
                    docBuilder = docFactory.newDocumentBuilder();
                    doc = docBuilder.parse(new InputSource(new StringReader(eventString)));

                    NodeList events = doc.getElementsByTagName("event");

                    comparables = new EventSortBaseComapator[table.getColumnCount()][events.getLength()];
                    String[] textBuffer = null;

                    for (int i = 0; i < events.getLength(); i++) {

                        Node n = events.item(i);
                        attr = n.getAttributes();
                        final TableItem newItemTableItem = new TableItem(table, SWT.BORDER);
                        textBuffer = new String[] { getText(attr.getNamedItem("event_class")), getText(attr.getNamedItem("event_id")),
                                getText(attr.getNamedItem("job_name")), getText(attr.getNamedItem("job_chain")),
                                getText(attr.getNamedItem("order_id")), getText(attr.getNamedItem("exit_code")),
                                getText(attr.getNamedItem("created")), getText(attr.getNamedItem("expires")),
                                getText(attr.getNamedItem("remote_scheduler_host")), getText(attr.getNamedItem("remote_scheduler_port")),
                                getText(attr.getNamedItem("scheduler_id")) };

                        newItemTableItem.setText(textBuffer);

                        comparables[table.getColumnCount() - 2][i] = new EventSortNumberComparator(textBuffer, i, table.getColumnCount() - 2);

                        for (int k = 0; k < table.getColumnCount() - 2; k++)
                            comparables[k][i] = new StringComparator(textBuffer, i, k);

                    }
                }

            } catch (ParserConfigurationException e) {
                msg.setText("XML-Answer from Scheduler was invalid");
            }
        }

    }

    protected final void sortTable(final Table table, final TableItem[] tableItems, final int colPos, final EventSortBaseComapator[][] comparables,
            final boolean sortFlag) {

        for (int i = 0; i < comparables[colPos].length; i++) {
            comparables[colPos][i]._sortFlag = sortFlag;
        }

        Arrays.sort(comparables[colPos]);

        String[] tmp;

        for (int i = 0; i < comparables[colPos].length; i++) {
            int rowNum = comparables[colPos][i]._oldRowNum;
            tmp = comparables[colPos][i]._textBuffer;
            tableItems[rowNum].setText(comparables[colPos][rowNum]._textBuffer);
            tableItems[i].setText(tmp);
        }
    }

    protected void createContents(final Composite parent) {
        prefs = Preferences.userNodeForPackage(this.getClass());

        final GridLayout gridLayout = new GridLayout(3, false);
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        parent.setLayout(gridLayout);

        eventsInSchedulerShell.addShellListener(new ShellAdapter() {

            @Override
            public void shellActivated(final ShellEvent e) {

                try {
                    if (enableRefresh) {
                        buildEventsFromXMl();
                    }
                } catch (SAXException e1) {
                    msg.setText("XML-Answer from Scheduler was invalid");

                } catch (IOException e1) {
                    logger.error(e1.getMessage(), e1);
                }
            }
        });

        timer = new Timer();
        timer.schedule(new RefreshTask(), 1000, 5000);

        final Button refreshButton = new Button(parent, SWT.NONE);
        refreshButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                refresh();
            }
        });
        refreshButton.setText(Messages.getLabel(DashBoardConstants.conSOSDashB_Refresh));

        final Text refreshInterval = new Text(parent, SWT.RIGHT | SWT.BORDER);
        refreshInterval.setText(prefs.get("refresh", "300"));
        refresh = getIntValue(refreshInterval.getText(), 300);
        refreshInterval.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent arg0) {
                refresh = getIntValue(refreshInterval.getText(), 300);
                timer.cancel();
                timer = new Timer();
                timer.schedule(new RefreshTask(), refresh * 1000, refresh * 1000);
                prefs.put("refresh", refreshInterval.getText());

            }
        });
        final GridData gd_refreshInterval = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gd_refreshInterval.widthHint = 69;
        gd_refreshInterval.minimumWidth = 50;
        refreshInterval.setLayoutData(gd_refreshInterval);

        final Button deleteEventButton = new Button(parent, SWT.NONE);
        deleteEventButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {

                String job_chain = "sos/events/scheduler_event_service";
                String state = "start";
                if (table.getSelectionIndex() >= 0) {
                    TableItem[] selection = table.getSelection();

                    for (TableItem t : selection) {
                        // TableItem t =
                        // table.getItem(table.getSelectionIndex());
                        String s = "";
                        if (t != null) {
                            s = "<add_order job_chain=\"" + job_chain + "\" state=\"" + state + "\" at=\"now\">";
                            s += " <params>" + "<param name=\"action\"                 value=\"remove\"/>"
                                    + "<param name=\"event_class\"            value=\""
                                    + t.getText()
                                    + "\"/>"
                                    + "<param name=\"event_id\"               value=\""
                                    + t.getText(1)
                                    + "\"/>"
                                    + "<param name=\"job_name\"               value=\""
                                    + t.getText(2)
                                    + "\"/>"
                                    + "<param name=\"job_chain\"              value=\""
                                    + t.getText(3)
                                    + "\"/>"
                                    + "<param name=\"order_id\"               value=\""
                                    + t.getText(4)
                                    + "\"/>"
                                    + "<param name=\"exit_code\"              value=\""
                                    + t.getText(5)
                                    + "\"/>"
                                    + "<param name=\"created\"                value=\""
                                    + t.getText(6)
                                    + "\"/>"
                                    + "<param name=\"expiration_date\"        value=\""
                                    + t.getText(7)
                                    + "\"/>"
                                    + "<param name=\"remote_scheduler_host\"  value=\""
                                    + t.getText(8)
                                    + "\"/>"
                                    + "<param name=\"remote_scheduler_port\"  value=\""
                                    + t.getText(9)
                                    + "\"/>"
                                    + "<param name=\"scheduler_id\"           value=\"" + t.getText(10) + "\"/>" + " </params></add_order>";

                            sendCommand(s);
                        }
                    }
                }

                table.redraw();

                refresh();

            }
        });
        deleteEventButton.setText("Delete Event");

        table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        table.setSortDirection(SWT.UP);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        final GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        gd_table.widthHint = 594;
        table.setLayoutData(gd_table);

        final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
        newColumnTableColumn.setWidth(50);
        newColumnTableColumn.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn.getText()).put("width", String.valueOf(newColumnTableColumn.getWidth()));

            }
        });
        newColumnTableColumn.setText("Class");
        newColumnTableColumn.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn.getText()).get("width", "50"), 50));

        final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_1.setWidth(50);
        newColumnTableColumn_1.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_1.getText()).put("width", String.valueOf(newColumnTableColumn_1.getWidth()));
            }
        });
        newColumnTableColumn_1.setText("Id");
        newColumnTableColumn_1.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_1.getText()).get("width", "50"), 50));

        final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_2.setWidth(50);
        newColumnTableColumn_2.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_2.getText()).put("width", String.valueOf(newColumnTableColumn_2.getWidth()));

            }
        });
        newColumnTableColumn_2.setText("Job");
        newColumnTableColumn_2.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_2.getText()).get("width", "100"), 100));

        final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_3.setWidth(50);
        newColumnTableColumn_3.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_3.getText()).put("width", String.valueOf(newColumnTableColumn_3.getWidth()));

            }
        });
        newColumnTableColumn_3.setText("Job Chain");
        newColumnTableColumn_3.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_3.getText()).get("width", "100"), 100));

        final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_4.setWidth(50);
        newColumnTableColumn_4.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_4.getText()).put("width", String.valueOf(newColumnTableColumn_4.getWidth()));

            }
        });
        newColumnTableColumn_4.setText("Order");
        newColumnTableColumn_4.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_4.getText()).get("width", "50"), 50));

        final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_5.setWidth(50);
        newColumnTableColumn_5.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_5.getText()).put("width", String.valueOf(newColumnTableColumn_5.getWidth()));

            }
        });
        newColumnTableColumn_5.setText("Exit Code");
        newColumnTableColumn_5.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_5.getText()).get("width", "50"), 50));

        final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_6.setWidth(50);
        newColumnTableColumn_6.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_6.getText()).put("width", String.valueOf(newColumnTableColumn_6.getWidth()));

            }
        });
        newColumnTableColumn_6.setText("Created");
        newColumnTableColumn_6.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_6.getText()).get("width", "100"), 100));

        final TableColumn newColumnTableColumn_7 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_7.setWidth(50);
        newColumnTableColumn_7.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_7.getText()).put("width", String.valueOf(newColumnTableColumn_7.getWidth()));

            }
        });
        newColumnTableColumn_7.setText("Expires");
        newColumnTableColumn_7.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_7.getText()).get("width", "100"), 100));

        final TableColumn newColumnTableColumn_8 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_8.setWidth(50);
        newColumnTableColumn_8.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_8.getText()).put("width", String.valueOf(newColumnTableColumn_8.getWidth()));

            }
        });
        newColumnTableColumn_8.setText("Originating Host");
        newColumnTableColumn_8.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_8.getText()).get("width", "100"), 100));

        final TableColumn newColumnTableColumn_9 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_9.setWidth(50);
        newColumnTableColumn_9.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent e) {
                prefs.node("table/col/" + newColumnTableColumn_9.getText()).put("width", String.valueOf(newColumnTableColumn_9.getWidth()));

            }
        });
        newColumnTableColumn_9.setText("Originating Port");
        newColumnTableColumn_9.setWidth(this.getIntValue(prefs.node("table/col/" + newColumnTableColumn_9.getText()).get("width", "100"), 100));

        TableColumn[] columns = table.getColumns();

        final TableColumn newColumnTableColumn_10 = new TableColumn(table, SWT.NONE);
        newColumnTableColumn_10.setWidth(50);
        newColumnTableColumn_10.setText("Scheduler ID");

        new TableColumn(table, SWT.NONE);
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);

        for (int i = 0; i < columns.length; i++) {
            final int _i = i;
            columns[i].addListener(SWT.Selection, new Listener() {

                private int colPos = -1;
                private boolean sortFlag;
                {
                    colPos = _i;
                }

                @Override
                public void handleEvent(final Event event) {
                    sortFlag = !sortFlag;
                    table.setSortColumn(table.getColumn(colPos));
                    if (sortFlag) {
                        table.setSortDirection(SWT.UP);
                    } else {
                        table.setSortDirection(SWT.DOWN);
                    }

                    TableItem[] tableItems = table.getItems();
                    sortTable(table, tableItems, colPos, comparables, sortFlag);
                }

            });
        }
        new Label(parent, SWT.NONE);
        new Label(parent, SWT.NONE);
        msg = new Label(parent, SWT.NONE);
        msg.setText("");

    }

    public void showShell() throws Exception {

        Composite parent = null;

        eventsInSchedulerShell = new Shell();
        parent = eventsInSchedulerShell;

        eventsInSchedulerShell.setSize(782, 478);
        eventsInSchedulerShell.setText("Events in Scheduler " + scheduler.getHostName() + ":" + scheduler.getTcpPort());
        createContents(parent);

        eventsInSchedulerShell.open();
        eventsInSchedulerShell.layout();
    }

    public void showComposite() throws Exception {
        eventsInSchedulerShell = composite.getShell();
        createContents(composite);

        // eventsInSchedulerShell.open();
        eventsInSchedulerShell.layout();
    }

    public void open() throws Exception {

        if (composite != null) {
            eventsInSchedulerShell = composite.getShell();
        }
        display = Display.getDefault();

        showShell();

        while (!eventsInSchedulerShell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }

    private int getIntValue(final String s, final int d) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException n) {
            return d;
        }
    }

    public Table getTable() {
        return table;
    }

    public static void main(final String[] args) {
        try {

            Shell shell = new Shell();

            Composite composite = new Composite(shell, SWT.NONE);
            Scheduler scheduler = new Scheduler();

            scheduler.setHostName(DEFAULT_JOBSCHEDULER_HOST_NAME);
            scheduler.setTcpPort(DEFAULT_JOBSCHEDULER_PORT);
            SOSStandardLogger sosLogger = new SOSStandardLogger(9);
            EventShowDialog window = new EventShowDialog(composite, scheduler, sosLogger);

            window.open();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("deprecation")
    public void setScheduler(final Scheduler scheduler) {
        this.scheduler = scheduler;
        host = scheduler.getHostName();
        port = Integer.valueOf(scheduler.getTcpPort());
    }

    public Timer getTimer() {
        return timer;
    }

    public void resetRefreshTimer() {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new RefreshTask(), refresh * 1000, refresh * 1000);
        enableRefresh = true;

    }

    public void disableRefresh() {
        timer.cancel();
        enableRefresh = false;

    }

}
