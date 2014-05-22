package com.sos.eventing.dialog.classes;

import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.sos.dashboard.globals.SOSDashboardOptions;

public class SOSTabEVENTS extends CTabItem {

	private static final String TABNAME_SCHEDULER_EVENTLIST = "Events";
	private static final String TABNAME_SCHEDULER_EVENTHANDLERLIST = "Event Handler";

	private final Composite composite;
	private Preferences prefs;
	private CTabFolder mainTabFolder = null;
	private String host;
	private int port;
	private String eventhandlerDirectory;
	private SOSTabEventHandlerList tbtmEventHandlerList;
	private SOSTabEventList tbtmEventList;
    private SOSDashboardOptions           objOptions                          = null;

	public SOSTabEVENTS(final SOSDashboardOptions objOptions_,final String caption, final CTabFolder parent ) {
		super(parent, SWT.NONE);
		objOptions = objOptions_;
		setText(caption);
		composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		createContents();
		composite.layout();
		this.setControl(composite);

	}

	public void disableRefresh(){
		 if (tbtmEventList != null){
 			 tbtmEventList.disableRefresh();
 			 tbtmEventHandlerList.disableRefresh();
		 }

	}

	public void enableRefresh(){
		 if (tbtmEventList != null){
			 tbtmEventList.enableRefresh();
			 tbtmEventHandlerList.enableRefresh();
		 }

	}
	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		prefs = Preferences.userNodeForPackage(this.getClass());

		host = prefs.node("EVENT_CONFIGURATION").get("HOST", "localhost");
		port = Integer.valueOf(prefs.node("EVENT_CONFIGURATION").get("PORT", "4444"));
		eventhandlerDirectory = prefs.node("EVENT_CONFIGURATION").get("EVENTHANDLERDIRECTORY", objOptions.Scheduler_Data.Value() + "/config/events");

		mainTabFolder = new CTabFolder(composite, SWT.NONE);
        mainTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		mainTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		tbtmEventList = new SOSTabEventList(TABNAME_SCHEDULER_EVENTLIST, mainTabFolder, host, port);
		tbtmEventHandlerList = new SOSTabEventHandlerList(TABNAME_SCHEDULER_EVENTHANDLERLIST, mainTabFolder, host, port, eventhandlerDirectory);
		tbtmEventHandlerList.setObjOptions(objOptions);

		mainTabFolder.setSelection(0);
		createMenue(mainTabFolder);
		createMenue(tbtmEventList.getParentComposite());
		createMenue(tbtmEventList.getTable());
		createMenue(tbtmEventHandlerList.getParentComposite());
		createMenue(tbtmEventHandlerList.getTable());
	}

	private void createMenue(final Composite parent) {

		if (parent != null) {
			Menu contentMenu = new Menu(parent);
			parent.setMenu(contentMenu);
			MenuItem addItem = new MenuItem(contentMenu, SWT.PUSH);
			addItem.setText("Configuration");
			addItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
				@Override
				public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
					SosDialogEventConfiguration s = new SosDialogEventConfiguration(composite.getShell(), host, port, eventhandlerDirectory);

					if (s.getHost() != null) {
						prefs.node("EVENT_CONFIGURATION").put("HOST", s.getHost());
						host = s.getHost();
	                    tbtmEventHandlerList.setHost(host);
	                    tbtmEventList.setHost(host);
					}

					prefs.node("EVENT_CONFIGURATION").put("PORT", s.getPort());
					port = Integer.valueOf(s.getPort());
					tbtmEventHandlerList.setPort(port);
					tbtmEventList.setPort(port);

					if (s.getEventhandlerDirectory() != null) {
						prefs.node("EVENT_CONFIGURATION").put("EVENTHANDLERDIRECTORY", s.getEventhandlerDirectory());
						eventhandlerDirectory = s.getEventhandlerDirectory();
						tbtmEventHandlerList.setEventhandlerDirectory(eventhandlerDirectory);
					}
				}

				@Override
				public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
				}
			});
		}
	}



}