

package com.sos.dailyschedule.job;

import com.sos.JSHelper.Basics.JSToolBox;
import org.apache.log4j.Logger;


/**
 * \class 		CheckDailyScheduleMain - Main-Class for "Checking a DailySchedule with runs in History"
 *
 * \brief MainClass to launch CheckDailySchedule as an executable command-line program
 *
 * This Class CheckDailyScheduleMain is the worker-class.
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-3456357879351999228.html for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSMainClass.xsl from http://www.sos-berlin.com at 20111212115623 
 * \endverbatim
 */
public class CheckDailyScheduleMain extends JSToolBox {
	private final static String					conClassName						= "CheckDailyScheduleMain"; //$NON-NLS-1$
	private static Logger		logger			= Logger.getLogger(CheckDailyScheduleMain.class);
	@SuppressWarnings("unused")	

	protected CheckDailyScheduleOptions	objOptions			= null;

	/**
	 * 
	 * \brief main
	 * 
	 * \details
	 *
	 * \return void
	 *
	 * @param pstrArgs
	 * @throws Exception
	 */
	public final static void main(String[] pstrArgs) {

		final String conMethodName = conClassName + "::Main"; //$NON-NLS-1$

		logger = Logger.getRootLogger();
		logger.info("CheckDailySchedule - Main"); //$NON-NLS-1$

		try {
			CheckDailySchedule objM = new CheckDailySchedule();
			CheckDailyScheduleOptions objO = objM.Options();
			
			objO.CommandLineArgs(pstrArgs);
			objM.Execute();
		}
		
		catch (Exception e) {
			System.err.println(conMethodName + ": " + "Error occured ..." + e.getMessage()); 
			e.printStackTrace(System.err);
			int intExitCode = 99;
			logger.error(String.format("JSJ-E-105: %1$s - terminated with exit-code %2$d", conMethodName, intExitCode), e);		
			System.exit(intExitCode);
		}
		
		logger.info(String.format("JSJ-I-106: %1$s - ended without errors", conMethodName));		
	}

}  // class CheckDailyScheduleMain