package com.sos.eventing.frontend;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.dashboard.globals.SOSDashboardOptions;
import com.swtdesigner.SWTResourceManager;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import sos.ftp.profiles.FTPProfile;
import sos.ftp.profiles.FTPProfilePicker;
import sos.scheduler.consoleviews.events.*;
import sos.settings.SOSProfileSettings;
import sos.settings.SOSSettings;
import sos.util.SOSFile;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.prefs.Preferences;

/*
 * Created on 23.06.2008
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

public class ActionShowDialog  extends JSToolBox {
 
	private static Logger logger = Logger.getLogger(ActionShowDialog.class);

	private static final String conParameterCONFIGURATION_DIRECTORY = "configuration_directory";
	private static final String conSectionPLUGIN_ACTION_SHOW_DIALOG = "plugin_action_show_dialog";

	@SuppressWarnings("unused")
	private final static String conSVNVersion = "$Id: Editor.java 18748 2013-01-09 21:19:21Z kb $";

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
	 

	// private String configuration_filename="";
	private File configuration_file = null;

	private SOSEvaluateEvents evaluateEvents = null;
	private Combo cboListOfFiles = null;
	private FTPProfilePicker ftpProfilePicker = null;
	private SOSLogger sosLogger = null;
    private SOSDashboardOptions           objOptions                          = null;
    private boolean enableRefresh=false;

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
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
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
			;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void fillTreeItem(SOSActions a, TreeItem item) {
		Iterator iCommands = a.getListOfCommands().iterator();
		while (iCommands.hasNext()) {
			SOSEventCommand ec = (SOSEventCommand) iCommands.next();
			TreeItem command_item = new TreeItem(item, SWT.NONE);
			if (ec.getAttribute("name").equals("")) {
				command_item.setFont(SWTResourceManager.getFont("", 8, SWT.NORMAL));
				command_item.setText(ec.getCommand().getNodeName());
			} else {
				command_item.setFont(SWTResourceManager.getFont("", 8, SWT.ITALIC));
				command_item.setText(ec.getAttribute("name"));
			}
			command_item.setData(a);

			Iterator iCommandElements = ec.getListOfCommandElements().iterator();
			while (iCommandElements.hasNext()) {
				SOSEventCommandElement ece = (SOSEventCommandElement) iCommandElements.next();
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
		Iterator iter = specialFiles.iterator();
		while (iter.hasNext()) {
			File actionEventHandler = (File) iter.next();
			if (actionEventHandler.exists() && actionEventHandler.canRead()) {
				this.cboListOfFiles.add(actionEventHandler.getName());
			}
		}

		// this.listOfFiles.setVisible(this.listOfFiles.getItemCount() > 0);
	}

	private void fillTree() throws DOMException, Exception {

		actionTree.removeAll();
		tableEvents.removeAll();
		// File f = new File(configuration_filename);

		if (configuration_file != null) {
			evaluateEvents.readConfigurationFile(configuration_file);

			Iterator iActions = evaluateEvents.getListOfActions().iterator();
			while (iActions.hasNext()) {
				SOSActions a = (SOSActions) iActions.next();
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
			Iterator i = a.getListOfEventGroups().iterator();

			while (i.hasNext()) {
				SOSEventGroups evg = (SOSEventGroups) i.next();
				String group = evg.getGroup();

				Iterator iEvents = evg.getListOfEvents().iterator();
				while (iEvents.hasNext()) {
					final TableItem newItemTableItem = new TableItem(tableEvents, SWT.BORDER);
					if (evg.getLogic().equals("and")) {
						newItemTableItem.setBackground(SWTResourceManager.getColor(255, 255, 128));
					}
					if (evg.getLogic().equals("or")) {
						newItemTableItem.setBackground(SWTResourceManager.getColor(206, 231, 255));
					}
					newItemTableItem.setText(group);
					newItemTableItem.setFont(0, SWTResourceManager.getFont("", 8, SWT.BOLD));
					SchedulerEvent event = (SchedulerEvent) iEvents.next();
					event.setLogic(evg.getLogic());
					newItemTableItem.setData(event);

					newItemTableItem.setText(1, event.getEvent_title());
					newItemTableItem.setText(2, event.getEvent_name());
					newItemTableItem.setText(3, event.getEvent_id());
					newItemTableItem.setText(4, event.getEvent_class());
					if (!event.getJob_name().equals("")) {
						newItemTableItem.setText(5, event.getJob_name());
					} else {
						newItemTableItem.setText(5, event.getJob_chain());
					}
					newItemTableItem.setText(6, evaluateEvents.getEventStatus(event));
					newItemTableItem.setText(7, event.getCreated());
					newItemTableItem.setText(8, event.getExpires());
					newItemTableItem.setText(9, event.getComment());
					if (evg.isActiv(evaluateEvents.getListOfActiveEvents())) {
						newItemTableItem.setBackground(0, SWTResourceManager.getColor(0, 255, 64));
					}
					if (newItemTableItem.getText(6).equalsIgnoreCase("active")) {
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
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
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
						profile.connect();

						configuration_file = File.createTempFile("JobScheduler", ".xml");

						String filename1 = cboListOfFiles.getText().substring(4);
						String filename2 = configuration_file.getCanonicalPath();
						filename2 = configuration_file.getAbsolutePath();

						profile.getFile(filename1, filename2);

						fillTree();
						configuration_file.delete();
					} else {
						configuration_file = new File(getConfigurationDirectory() + "/" + cboListOfFiles.getText());
						fillTree();
					}
				} catch (DOMException e1) {

					e1.printStackTrace();
				} catch (Exception e1) {

					e1.printStackTrace();
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
	    try{
		  ftpProfilePicker.addEmptyItem();
	    }catch (Exception e){}

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
					if (profile != null) {
						sosLogger.info("user: " + profile.getUser());
						sosLogger.info("root: " + profile.getRoot());

						sosLogger.info("... connecting");
						profile.connect();
						sosLogger.info("... connected");

						msg.setText("successfully connected..");

						Iterator iter = null;
						sosLogger.debug6("get list of files");
						Vector v = profile.getList();

						iter = v.iterator();
						cboListOfFiles.removeAll();

						sosLogger.debug6("reading files");
						while (iter != null && iter.hasNext()) {

							String filename = (String) iter.next();
							sosLogger.debug6("... reading " + filename);
							if (filename.toLowerCase().endsWith(".actions.xml")) {
								String entry = "ftp:" + filename;
								if (cboListOfFiles.indexOf(entry) < 0)
									cboListOfFiles.add(entry);
							}

						}
					} else {

						getConfigurationFiles();

					}

				} catch (Exception ex) {
					try {
						sosLogger.warn("Error while connecting " + ex.getMessage());
					} catch (Exception e1) {
						e1.printStackTrace();
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
				/*
				 * lbJob.setText(event.getJob_name());
				 * lbJobChain.setText(event.getJob_chain());
				 * lbExitCode.setText(event.getExit_code());
				 * lbOrderId.setText(event.getOrder_id());
				 * lbHostPort.setText(event.getRemote_scheduler_host() + ":" +
				 * event.getRemote_scheduler_port());
				 * lbSchedulerId.setText(event.getScheduler_id());
				 */
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
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	private int getIntValue(String s, int d) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException n) {
			return d;
		}
	}

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		try {
			logger = Logger.getRootLogger();
			logger.debug(conSVNVersion);

			String host = "localhost";
			String port = "4445";
			Shell shell = new Shell();

			Composite composite = new Composite(shell, SWT.NONE);
			Scheduler scheduler = new Scheduler();
			SOSSettings profileSettings = new SOSProfileSettings("./config/settings.ini");

			if (profileSettings.getSection(conSectionPLUGIN_ACTION_SHOW_DIALOG) != null) {
				host = profileSettings.getSection(conSectionPLUGIN_ACTION_SHOW_DIALOG).getProperty("host");
			}
			if (host == null || host.length() == 0) {
				host = "localhost";
			}

			if (profileSettings.getSection(conSectionPLUGIN_ACTION_SHOW_DIALOG) != null) {
				port = profileSettings.getSection(conSectionPLUGIN_ACTION_SHOW_DIALOG).getProperty("port");
			}
			if (port == null || port.length() == 0) {
				port = "4445";
			}

			String configurationsDirectory = profileSettings.getSection(conSectionPLUGIN_ACTION_SHOW_DIALOG).getProperty("configuration_dirctory");

			scheduler.setHostName(host);
			scheduler.setTcpPort(port);

			SOSStandardLogger sosLogger = new SOSStandardLogger(9);
			ActionShowDialog window = new ActionShowDialog(composite, scheduler, configurationsDirectory, sosLogger);

			window.open();
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setObjOptions(SOSDashboardOptions objOptions) {
		this.objOptions = objOptions;

		if (configurationDirectory == null || configurationDirectory.equals("")) {
			if (objOptions != null){
				configurationDirectory = objOptions.Scheduler_Home.Value() + "/config/events";				
			}
		}
	}
	
	public Timer getTimer() {
		return timer;
	}

       public void resetRefreshTimer() {
    	enableRefresh=true;
		timer.cancel();
		timer = new Timer();
		timer.schedule(new RefreshTask(), refresh * 1000, refresh * 1000);
	}
       
       public void disableRefresh() {
    	   timer.cancel();
		   enableRefresh=false;
 	 
	}       
}