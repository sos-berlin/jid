

package com.sos.test;

import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.JSToolBox;


/**
 * \class 		CreateGenerateOrderMain - Main-Class for "Creating PDIR generate order"
 *
 * \brief MainClass to launch CreateGenerateOrder as an executable command-line program
 *
 * This Class CreateGenerateOrderMain is the worker-class.
 *

 *
 *
 * \verbatim ;
 * mechanicaly created by C:\ProgramData\sos-berlin.com\jobscheduler\scheduler_ur\config\JOETemplates\java\xsl\JSJobDoc2JSMainClass.xsl from http://www.sos-berlin.com at 20130225111725 
 * \endverbatim
 */
public class CreateGenerateOrderMain extends JSToolBox {
	private final static String					conClassName						= "CreateGenerateOrderMain"; //$NON-NLS-1$
	private static Logger		logger			= Logger.getLogger(CreateGenerateOrderMain.class);

	protected CreateGenerateOrderOptions	objOptions			= null;

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
	public final static void main(final String[] pstrArgs) {

		final String conMethodName = conClassName + "::Main"; //$NON-NLS-1$

		logger.info("CreateGenerateOrder - Main"); //$NON-NLS-1$

		try {
			CreateGenerateOrder objM = new CreateGenerateOrder();
			CreateGenerateOrderOptions objO = objM.Options();
			
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

}  // class CreateGenerateOrderMain