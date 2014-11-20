

package com.sos.dailyschedule.job;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import org.apache.log4j.Logger;
import org.junit.*;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * \class 		CheckDailyScheduleJUnitTest - JUnit-Test for "Checking a DailySchedule with runs in History"
 *
 * \brief MainClass to launch CheckDailySchedule as an executable command-line program
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-463771972724180853.html for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSJUnitClass.xsl from http://www.sos-berlin.com at 20111212133923 
 * \endverbatim
 */
public class CheckDailyScheduleJUnitTest extends JSToolBox {
	@SuppressWarnings("unused")	 
	private final static String					conClassName						= "CheckDailyScheduleJUnitTest"; //$NON-NLS-1$
	@SuppressWarnings("unused")	 
	private static Logger		logger			= Logger.getLogger(CheckDailyScheduleJUnitTest.class);
	@SuppressWarnings("unused")	 

	protected CheckDailyScheduleOptions	objOptions			= null;
	private CheckDailySchedule				objE			= null;

	
	public CheckDailyScheduleJUnitTest() {
		//
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		objE = new CheckDailySchedule();
		objE.registerMessageListener(this);
		objOptions = objE.Options();
		objOptions.registerMessageListener(this);
		
		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = 9;
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() throws Exception {

			HashMap pobjHM = new HashMap();
			pobjHM.put("CheckDailyScheduleOptionsSuperClass.scheduler_id", "");
 			pobjHM.put("dayOffset",-1);
 			pobjHM.put("configurationFile","R:/nobackup/junittests/hibernate/hibernate_oracle.cfg.xml");

 			CheckDailySchedule objE = new CheckDailySchedule();
 			
			objE.Options().setAllOptions(pobjHM);
	    	assertEquals("", objE.Options().scheduler_id.Value(), "");
			assertEquals("", objE.Options().dayOffset.value(), -1);

			objE.Execute();



	}
}  // class CheckDailyScheduleJUnitTest