package com.sos.dailyschedule.dialog.classes;

import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;

import com.sos.dashboard.globals.DashBoardConstants;
import com.sos.jid.dialog.classes.SOSDashboardTableView;
import com.sos.localization.Messages;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;

public class SOSHandleIgnoreSubmenuExecuted extends SOSHandleIgnoreSubmenu{

	private static final String USE_CONTEXT_EXECUTED = "sosDashboardExecuted";   
	

	public SOSHandleIgnoreSubmenuExecuted(Menu parent, SOSDashboardTableView sosDashboardTableView_, Messages messages_,
			Preferences prefs_) {
		super(parent, sosDashboardTableView_, messages_, prefs_);
 	}
	
	public String getContext(){
		return USE_CONTEXT_EXECUTED;
	}

	@Override 
	protected void addSpecialContextMenu() {
		
	}

	 
}
