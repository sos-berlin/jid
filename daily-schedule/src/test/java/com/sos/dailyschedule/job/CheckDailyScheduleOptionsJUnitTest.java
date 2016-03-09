package com.sos.dailyschedule.job;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import org.apache.log4j.Logger;
import org.junit.*;

import static org.junit.Assert.assertEquals;

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
        objOptions = objE.getOptions();
        objOptions.registerMessageListener(this);

        JSListenerClass.bolLogDebugInformation = true;
        JSListenerClass.intMaxDebugLevel = 9;
    }

    @After
    public void tearDown() throws Exception {
    }

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