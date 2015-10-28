package com.sos.dailyschedule.job;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.IJSCommands;
import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.dailyschedule.db.Calendar2DB;

/**
 * \class 		CreateDailySchedule - Workerclass for "Creating a DaysSchedule depending on actual Runtimes"
 *
 * \brief AdapterClass of CreateDailySchedule for the SOSJobScheduler
 *
 * This Class CreateDailySchedule is the worker-class.
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-2235912449518755069.html for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSWorkerClass.xsl from http://www.sos-berlin.com at 20111027105329
 * \endverbatim
 */
public class CreateDailySchedule extends JSJobUtilitiesClass <CreateDailyScheduleOptions> implements IJSCommands {
	private final String					conClassName		= "CreateDailySchedule";						//$NON-NLS-1$
	private static Logger					logger				= Logger.getLogger(CreateDailySchedule.class);

	public CreateDailySchedule() {
		super(new CreateDailyScheduleOptions());
	}

	@Override
	public CreateDailyScheduleOptions getOptions() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Options"; //$NON-NLS-1$
		if (objOptions == null) {
			objOptions = new CreateDailyScheduleOptions();
		}
		return objOptions;
	}

	public CreateDailySchedule Execute() throws Exception {
		try {
			getOptions().CheckMandatory();
			logger.debug(getOptions().dirtyString());
			Calendar2DB calendar2Db = new Calendar2DB(objOptions.getconfiguration_file().Value());
			calendar2Db.setOptions(objOptions);
			calendar2Db.store();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new Exception (e);
		}
		return this;
	}

} // class CreateDailySchedule
