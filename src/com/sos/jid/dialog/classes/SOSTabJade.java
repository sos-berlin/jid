package com.sos.jid.dialog.classes;

import java.net.URL;
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
import com.sos.dialog.classes.SOSUrl;

public class SOSTabJade extends CTabItem {

	private static final String TABNAME_JADE_COCKPIT = "Jade Cockpit";
	private static final String TABNAME_JADE_BACKGROUND_SERVICE = "Jade Background Service";
 

	private final Composite composite;
	private Preferences prefs;
	private CTabFolder mainTabFolder = null;
	 
	private SOSTabJadeCockpit tbtmJadeCockpit;
    private SosTabJOC tbtmJadeJoc;
    private SOSTabJadeBackground tbtmJadeBackground;
    
    private SOSDashboardOptions  objOptions=null;

	public SOSTabJade(final SOSDashboardOptions objOptions_,final String caption, final CTabFolder parent ) {
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
		 

	}

	public void enableRefresh(){
		  

	}
	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		prefs = Preferences.userNodeForPackage(this.getClass());

		mainTabFolder = new CTabFolder(composite, SWT.NONE);
        mainTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		mainTabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		tbtmJadeCockpit = new SOSTabJadeCockpit(TABNAME_JADE_COCKPIT, mainTabFolder);
        tbtmJadeJoc = new SosTabJOC(mainTabFolder, new SOSUrl("http://sos-berlin.com"));
        tbtmJadeBackground = new SOSTabJadeBackground(TABNAME_JADE_BACKGROUND_SERVICE, mainTabFolder);
	 	
        mainTabFolder.setSelection(0);
  
	}

	 
	 



}