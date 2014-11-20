

package com.sos.dailyschedule.job;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.dailyschedule.db.DailyScheduleAdjustment;

/**
 * \class 		CheckDailySchedule - Workerclass for "Checking a DailySchedule with runs in History"
 *
 * \brief AdapterClass of CheckDailySchedule for the SOSJobScheduler
 *
 * This Class CheckDailySchedule is the worker-class.
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-3456357879351999228.html for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSWorkerClass.xsl from http://www.sos-berlin.com at 20111212115623
 * \endverbatim
 */
public class CheckDailySchedule extends JSJobUtilitiesClass <CheckDailyScheduleOptions> {
	private final String					conClassName						= "CheckDailySchedule";  //$NON-NLS-1$
	private static Logger		logger			= Logger.getLogger(CheckDailySchedule.class);

//	protected CheckDailyScheduleOptions	objOptions			= null;
//    private JSJobUtilities      objJSJobUtilities   = this;


	/**
	 *
	 * \brief CheckDailySchedule
	 *
	 * \details
	 *
	 */
	public CheckDailySchedule() {
		super(new CheckDailyScheduleOptions());
	}

	/**
	 *
	 * \brief Options - returns the CheckDailyScheduleOptionClass
	 *
	 * \details
	 * The CheckDailyScheduleOptionClass is used as a Container for all Options (Settings) which are
	 * needed.
	 *
	 * \return CheckDailyScheduleOptions
	 *
	 */
	@Override
	public CheckDailyScheduleOptions Options() {

		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::Options";  //$NON-NLS-1$

		if (objOptions == null) {
			objOptions = new CheckDailyScheduleOptions();
		}
		return objOptions;
	}

	/**
	 *
	 * \brief Execute - Start the Execution of CheckDailySchedule
	 *
	 * \details
	 *
	 * For more details see
	 *
	 * \see JobSchedulerAdapterClass
	 * \see CheckDailyScheduleMain
	 *
	 * \return CheckDailySchedule
	 *
	 * @return
	 */
	public CheckDailySchedule Execute() throws Exception {
		final String conMethodName = conClassName + "::Execute";  //$NON-NLS-1$

		//logger.debug(String.format(Messages.getMsg("JSJ-I-110"), conMethodName ) );

		try {
			Options().CheckMandatory();
			logger.debug(Options().dirtyString());
			DailyScheduleAdjustment dailyScheduleAdjustment = new DailyScheduleAdjustment(new File(objOptions.configuration_file.Value()));
			dailyScheduleAdjustment.setOptions(objOptions);
			//It is not neccessary to check into the future.
			dailyScheduleAdjustment.setTo(new Date());
 			dailyScheduleAdjustment.adjustWithHistory();
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new Exception (e);
			//logger.error(String.format(Messages.getMsg("JSJ-I-107"), conMethodName ), e);
		}
		finally {

		}

		return this;
	}

	public void init() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::init";  //$NON-NLS-1$
		doInitialize();
	}

	private void doInitialize() {
	} // doInitialize

}  // class CheckDailySchedule