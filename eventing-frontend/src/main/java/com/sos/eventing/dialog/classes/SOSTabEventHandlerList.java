package com.sos.eventing.dialog.classes;

 
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import sos.util.SOSStandardLogger;

import com.sos.dashboard.globals.SOSDashboardOptions;
import com.sos.eventing.frontend.ActionShowDialog;
import com.sos.eventing.frontend.Scheduler;

public class SOSTabEventHandlerList extends CTabItem {

    private static final String conSectionPLUGIN_ACTION_SHOW_DIALOG = "plugin_action_show_dialog";
    private static Logger logger = Logger.getLogger(SOSTabEventHandlerList.class);

    private final Composite parentComposite;
    private final GridLayout layout;
    private Table table;
    private String eventhandlerDirectory;
    private Scheduler scheduler = new Scheduler();
    private ActionShowDialog eventHandlerView;
    private SOSDashboardOptions objOptions = null;

    public SOSTabEventHandlerList(final String caption, final CTabFolder parentTabfolder, final String host, final int port,
            final String _eventhandlerDirectory) {
        super(parentTabfolder, SWT.NONE);
        setText(caption);
        eventhandlerDirectory = _eventhandlerDirectory;
        scheduler = new Scheduler();
        scheduler.setHostName(host);
        scheduler.setTcpPort(String.valueOf(port));
        parentComposite = new Composite(parentTabfolder, SWT.NONE);
        layout = new GridLayout(1, false);
        parentComposite.setLayout(layout);
        createContent();

        parentComposite.layout();
        this.setControl(parentComposite);
    }

    private void createContent() {
        try {

            SOSStandardLogger sosLogger = new SOSStandardLogger(9);
            eventHandlerView = new ActionShowDialog(parentComposite, scheduler, eventhandlerDirectory, sosLogger);
            eventHandlerView.showComposite();
            table = eventHandlerView.getTableEvents();
        } catch (Exception e) {
            logger.error(String.format("could not create event handler view: %s", e.getMessage()));
            logger.error(e.getMessage(), e);
        }

    }

    public Composite getParentComposite() {
        return parentComposite;
    }

    public Table getTable() {
        return table;
    }

    public void setEventhandlerDirectory(final String eventhandlerDirectory) {
        this.eventhandlerDirectory = eventhandlerDirectory;
        eventHandlerView.setConfigurationDirectory(eventhandlerDirectory);
    }

    public void setPort(final int port) {
        scheduler.setTcpPort(String.valueOf(port));
        eventHandlerView.setScheduler(scheduler);
    }

    public void setHost(final String host) {
        scheduler.setHostName(host);
        eventHandlerView.setScheduler(scheduler);
    }

    public void setObjOptions(final SOSDashboardOptions objOptions) {
        this.objOptions = objOptions;
    }

    public void disableRefresh() {
        eventHandlerView.disableRefresh();
    }

    public void enableRefresh() {
        eventHandlerView.resetRefreshTimer();
    }
}
