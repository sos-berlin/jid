package com.sos.dailyschedule.job;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import org.apache.log4j.Logger;
import org.junit.*;

import static org.junit.Assert.assertEquals;

<<<<<<< HEAD
/**
 * \class 		CheckDailyScheduleOptionsJUnitTest - Checking a DailySchedule with runs in History
 *
 * \brief 
 *
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-463771972724180853.html for (more) details.
 * 
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSJUnitOptionSuperClass.xsl from http://www.sos-berlin.com at 20111212133923 
 * \endverbatim
 *
 * \section TestData Eine Hilfe zum Erzeugen einer HashMap mit Testdaten
 *
 * Die folgenden Methode kann verwendet werden, um für einen Test eine HashMap
 * mit sinnvollen Werten für die einzelnen Optionen zu erzeugen.
 *
 * \verbatim
 private HashMap <String, String> SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) {
	pobjHM.put ("		CheckDailyScheduleOptionsJUnitTest.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
public class CheckDailyScheduleOptionsJUnitTest extends  JSToolBox {
	private final String					conClassName						= "CheckDailyScheduleOptionsJUnitTest"; //$NON-NLS-1$
		@SuppressWarnings("unused") //$NON-NLS-1$
	private static Logger		logger			= Logger.getLogger(CheckDailyScheduleOptionsJUnitTest.class);
	private CheckDailySchedule objE = null;

	protected CheckDailyScheduleOptions	objOptions			= null;

	public CheckDailyScheduleOptionsJUnitTest() {
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
		objOptions = objE.getOptions();
		objOptions.registerMessageListener(this);
		
		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = 9;
	}

	@After
	public void tearDown() throws Exception {
	}


		
	/**
	 * \brief testconfiguration_file : Die Datei mit den Einstellungen für Datenbank. Beispiel: 
	 * 
	 * \details
	 * Die Datei mit den Einstellungen für Datenbank. Beispiel: <?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd"> <hibernate-configuration> <session-factory> <property name="hibernate.connection.driver_class">oracle.jdbc.driver.OracleDriver</property> <property name="hibernate.connection.password">dbpwd</property> <property name="hibernate.connection.url">jdbc:oracle:thin:@8of9:1521:dbserver</property> <property name="hibernate.connection.username">dbuser</property> <property name="hibernate.dialect">org.hibernate.dialect.Oracle10gDialect</property> <property name="hibernate.show_sql">true</property> <property name="hibernate.connection.autocommit">false</property> <property name="hibernate.format_sql">true</property> <property name="hibernate.temp.use_jdbc_metadata_defaults">false</property> <mapping class="com.sos.jade.db.JadeTransferDBItem"/> <mapping class="com.sos.jade.db.JadeTransferDetailDBItem"/> <mapping class="com.sos.dailyschedule.db.DailyScheduleDBItem"/> <mapping class="com.sos.scheduler.history.db.SchedulerHistoryDBItem"/> <mapping class="com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem"/> </session-factory> </hibernate-configuration>
	 *
	 */
	    @Test
	    public void testconfiguration_file() {  // SOSOptionString
	    	 objOptions.configuration_file.Value("++----++");
	    	 assertEquals ("Die Datei mit den Einstellungen für Datenbank. Beispiel: <?xml v", objOptions.configuration_file.Value(),"++----++");
	    	
	    }

/**
 * \brief testdayOffset : 
 * 
 * \details
 * set the from-to intervall for calculating the days schedule. Samples: 0: actual day -2:from the before yesterday until today 1: from today until tomorrow For each day a days schedule will be creatied. Existing days schedules will be deleted.
 *
 */
=======
public class CheckDailyScheduleOptionsJUnitTest extends JSToolBox {

    private final String conClassName = "CheckDailyScheduleOptionsJUnitTest"; //$NON-NLS-1$
    @SuppressWarnings("unused")//$NON-NLS-1$
    private static Logger logger = Logger.getLogger(CheckDailyScheduleOptionsJUnitTest.class);
    private CheckDailySchedule objE = null;

    protected CheckDailyScheduleOptions objOptions = null;

    public CheckDailyScheduleOptionsJUnitTest() {
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

>>>>>>> origin/release/1.9
    @Test
    public void testconfiguration_file() {  // SOSOptionString
        objOptions.configuration_file.Value("++----++");
        assertEquals("Die Datei mit den Einstellungen für Datenbank. Beispiel: <?xml v", objOptions.configuration_file.Value(), "++----++");

    }

    @Test
    public void testdayOffset() {  // SOSOptionInteger
        objOptions.dayOffset.Value("12345");
        assertEquals("", objOptions.dayOffset.Value(), "12345");
        assertEquals("", objOptions.dayOffset.value(), 12345);
        objOptions.dayOffset.value(12345);
        assertEquals("", objOptions.dayOffset.Value(), "12345");
        assertEquals("", objOptions.dayOffset.value(), 12345);

    }

    @Test
    public void testscheduler_id() {  // SOSOptionString
        objOptions.scheduler_id.Value("++----++");
        assertEquals("", objOptions.scheduler_id.Value(), "++----++");

    }

} // public class CheckDailyScheduleOptionsJUnitTest