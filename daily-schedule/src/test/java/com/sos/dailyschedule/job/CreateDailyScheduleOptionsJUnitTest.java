package com.sos.dailyschedule.job;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import com.sos.JSHelper.Logging.Log4JHelper;

/**
 * \class 		CreateDaysScheduleOptionsJUnitTest - Creating a DaysSchedule depending on actual Runtimes
 *
 * \brief 
 *
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-2235912449518755069.html for (more) details.
 * 
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSJUnitOptionSuperClass.xsl from http://www.sos-berlin.com at 20111027105329 
 * \endverbatim
 *
 * \section TestData Eine Hilfe zum Erzeugen einer HashMap mit Testdaten
 *
 * Die folgenden Methode kann verwendet werden, um für einen Test eine HashMap
 * mit sinnvollen Werten für die einzelnen Optionen zu erzeugen.
 *
 * \verbatim
 private HashMap <String, String> SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) {
	pobjHM.put ("		CreateDaysScheduleOptionsJUnitTest.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
public class CreateDailyScheduleOptionsJUnitTest extends JSToolBox {
	private final String					conClassName	= "CreateDaysScheduleOptionsJUnitTest";						//$NON-NLS-1$
	@SuppressWarnings("unused")//$NON-NLS-1$
	private static Logger					logger			= Logger.getLogger(CreateDailyScheduleOptionsJUnitTest.class);
	@SuppressWarnings("unused")
	private static Log4JHelper				objLogger		= null;
	private CreateDailySchedule				objE			= null;

	protected CreateDailyScheduleOptions	objOptions		= null;

	public CreateDailyScheduleOptionsJUnitTest() {
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
		objLogger = new Log4JHelper("./log4j.properties"); //$NON-NLS-1$
		objE = new CreateDailySchedule();
		objE.registerMessageListener(this);
		objOptions = objE.Options();
		objOptions.registerMessageListener(this);

		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = 9;
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * \brief testSchedulerHost : 
	 * 
	 * \details
	 * This is the host name of the Job Scheduler for which the days schedule should be generatet. The parameter is mandatory, if the job runs not with Job Scheduler but with console
	 *
	 */
	@Test
	public void testSchedulerHost() { // SOSOptionString
		objOptions.SchedulerHostName.Value("++----++");
		assertEquals("", objOptions.SchedulerHostName.Value(), "++----++");

	}

	/**
	 * \brief testSchedulerPort : 
	 * 
	 * \details
	 * This is the port of the Job Scheduler for which the days schedule should be generatet. The parameter is mandatory, if the job runs not with Job Scheduler but with console
	 *
	 */
	@Test
	public void testSchedulerPort() { // SOSOptionString
		objOptions.scheduler_port.value(4139);
		assertEquals("", objOptions.scheduler_port.value(), 4139);

	}

	/**
	 * \brief testdayOffset : 
	 * 
	 * \details
	 * set the from-to intervall for calculating the days schedule. Samples: 0: actual day -2:from the before yesterday until today 1: from today until tomorrow For each day a days schedule will be creatied. Existing days schedules will be deletet.
	 *
	 */
	@Test
	public void testdayOffset() { // SOSOptionString
		objOptions.dayOffset.value(7);
		assertEquals("", objOptions.dayOffset.value(), 7);

	}
	
	/**
	 * \brief testconfiguration_file : 
	 * 
	 * \details
	 * 
	 *
	 */
	    @Test
	    public void testconfiguration_file() {  // SOSOptionString
	    	 objOptions.configuration_file.Value("++----++");
	    	 assertEquals ("", objOptions.configuration_file.Value(),"++----++");
	    	
	    }


	@Test public void testHashMap () throws Exception {
		objOptions.setAllOptions(SetJobSchedulerSSHJobOptions(new HashMap <String, String> ()));
	}
	private HashMap<String, String> SetJobSchedulerSSHJobOptions(HashMap<String, String> pobjHM) {
		pobjHM.put("CreateDaysScheduleOptionsJUnitTest.auth_file", "test"); // This parameter specifies the path and name of a user's pr
		return pobjHM;
	} //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)

} // public class CreateDaysScheduleOptionsJUnitTest