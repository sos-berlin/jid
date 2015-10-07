package com.sos.dailyschedule.job;

import org.apache.log4j.Logger;
import com.sos.JSHelper.Basics.JSToolBox;


/**
 * \class 		CreateDaysScheduleMain - Main-Class for "Creating a DaysSchedule depending on actual Runtimes"
 *
 * \brief MainClass to launch CreateDaysSchedule as an executable command-line program
 *
 * This Class CreateDaysScheduleMain is the worker-class.
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-2235912449518755069.html for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSMainClass.xsl from http://www.sos-berlin.com at 20111027105329 
 * \endverbatim
 */
public class CreateDailyScheduleMain extends JSToolBox {
	private final static String					conClassName						= "CreateDaysScheduleMain"; //$NON-NLS-1$
	private static Logger		logger			= Logger.getLogger(CreateDailyScheduleMain.class);

	protected CreateDailyScheduleOptions	objOptions			= null;

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
		logger.info("CreateDaysSchedule - Main"); //$NON-NLS-1$

		try {
			CreateDailySchedule objM = new CreateDailySchedule();
			CreateDailyScheduleOptions objO = objM.getOptions();
			
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

}  // class CreateDaysScheduleMain