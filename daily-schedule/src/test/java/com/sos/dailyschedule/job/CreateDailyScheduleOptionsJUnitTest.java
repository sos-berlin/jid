package com.sos.dailyschedule.job;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import org.apache.log4j.Logger;
import org.junit.*;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CreateDailyScheduleOptionsJUnitTest extends JSToolBox {

    private final String conClassName = "CreateDaysScheduleOptionsJUnitTest";						//$NON-NLS-1$
    @SuppressWarnings("unused")//$NON-NLS-1$
    private static Logger logger = Logger.getLogger(CreateDailyScheduleOptionsJUnitTest.class);
    private CreateDailySchedule objE = null;

    protected CreateDailyScheduleOptions objOptions = null;

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
        objE = new CreateDailySchedule();
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
    public void testSchedulerHost() { // SOSOptionString
        objOptions.SchedulerHostName.Value("++----++");
        assertEquals("", objOptions.SchedulerHostName.Value(), "++----++");

    }

    @Test
    public void testSchedulerPort() { // SOSOptionString
        objOptions.scheduler_port.value(4139);
        assertEquals("", objOptions.scheduler_port.value(), 4139);

    }

    @Test
    public void testdayOffset() { // SOSOptionString
        objOptions.dayOffset.value(7);
        assertEquals("", objOptions.dayOffset.value(), 7);

    }

    @Test
    public void testconfiguration_file() {  // SOSOptionString
        objOptions.configuration_file.Value("++----++");
        assertEquals("", objOptions.configuration_file.Value(), "++----++");

    }

    @Test
    public void testHashMap() throws Exception {
        objOptions.setAllOptions(SetJobSchedulerSSHJobOptions(new HashMap<String, String>()));
    }

    private HashMap<String, String> SetJobSchedulerSSHJobOptions(HashMap<String, String> pobjHM) {
        pobjHM.put("CreateDaysScheduleOptionsJUnitTest.auth_file", "test");
        return pobjHM;
    }

} // public class CreateDaysScheduleOptionsJUnitTest