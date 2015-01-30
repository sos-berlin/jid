package com.sos.dailyschedule.dialog.classes;

import java.util.prefs.Preferences;

import org.eclipse.swt.widgets.Menu;

import com.sos.jid.dialog.classes.SOSDashboardTableView;
import com.sos.localization.Messages;

public class SOSHandleIgnoreSubmenuPlanned extends SOSHandleIgnoreSubmenu{

	private static final String USE_CONTEXT_PLANNED = "sosDashboardPlanned";

	public SOSHandleIgnoreSubmenuPlanned(Menu parent, SOSDashboardTableView sosDashboardTableView_, Messages messages_,
			Preferences prefs_) {
		super(parent, sosDashboardTableView_, messages_, prefs_);
 	}
	
	public String getContext(){
		return USE_CONTEXT_PLANNED;
	}

	@Override
	void addSpecialContextMenu() {
		// Do nothing
		
	}
	 
}
