

package com.sos.dailyschedule.job;

import java.io.File;

import org.apache.log4j.Logger;

import sos.scheduler.job.JobSchedulerJobAdapter;
import sos.spooler.Spooler;
/**
 * \class 		CheckDailyScheduleJSAdapterClass - JobScheduler Adapter for "Checking a DailySchedule with runs in History"
 *
 * \brief AdapterClass of CheckDailySchedule for the SOSJobScheduler
 *
 * This Class CheckDailyScheduleJSAdapterClass works as an adapter-class between the SOS
 * JobScheduler and the worker-class CheckDailySchedule.
 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-3456357879351999228.html for more details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSAdapterClass.xsl from http://www.sos-berlin.com at 20111212115623
 * \endverbatim
 */
public class CheckDailyScheduleJSAdapterClass extends JobSchedulerJobAdapter  {
	private final String					conClassName						= "CheckDailyScheduleJSAdapterClass";  //$NON-NLS-1$
	private static Logger		logger			= Logger.getLogger(CheckDailyScheduleJSAdapterClass.class);

	@Override
	public boolean spooler_init() {
		return super.spooler_init();
	}

	@Override
	public boolean spooler_process() throws Exception {
		try {
			super.spooler_process();
			doProcessing();
		}
		catch (Exception e) {
			return false;
		}
		// return value for classic and order driven processing
		// TODO create method in base-class for this functionality
		return spooler_task.job().order_queue() != null;

	} // spooler_process

	@Override
	public void spooler_exit() {
		super.spooler_exit();
	}

	private void doProcessing() throws Exception {
		final String conMethodName = conClassName + "::doProcessing"; //$NON-NLS-1$
		logger.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName ) );
		CheckDailySchedule objR = new CheckDailySchedule();
		CheckDailyScheduleOptions objO = objR.getOptions();
		
		objO.setAllOptions(getSchedulerParameterAsProperties(getParameters()));

		 
		Object objSp = getSpoolerObject();

		String schedulerId = "";
		String configuration_file = "";
	    Spooler objSpooler = (Spooler) objSp;

		if (objO.getItem("scheduler_id") != null) {
			logger.debug("scheduler_id from param");
			schedulerId = objO.scheduler_id.Value();
		}else {
			logger.debug("scheduler_id from scheduler");
			schedulerId = objSpooler.id();
		}

		if (objO.configuration_file.IsEmpty() == false) {
			logger.debug("configuration_file from param");
			configuration_file = objO.configuration_file.Value();
		}else {
			logger.debug("configuration_file from scheduler");
			File f = new File(new File(objSpooler.configuration_directory()).getParent(),"hibernate.cfg.xml");
			if (!f.exists()){
 			   f = new File(new File(objSpooler.directory()),"config/hibernate.cfg.xml");
			}
			configuration_file = f.getAbsolutePath();
		}
		
		if (objO.check_all_jobscheduler_instances.value()) {
	        schedulerId="";
	        spooler_log.debug3("Checking all JobScheduler instance");
		}

		objO.configuration_file.Value(configuration_file);
		objO.scheduler_id.Value(schedulerId);

		objO.CheckMandatory();
        objR.setJSJobUtilites(this);
		objR.Execute();
	} // doProcessing

}

