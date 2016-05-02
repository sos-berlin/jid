package com.sos.dailyschedule.job;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;

public class CreateDailyScheduleOptionsJUnitTest extends JSToolBox {

    protected CreateDailyScheduleOptions objOptions = null;
    private CreateDailySchedule objE = null;

    public CreateDailyScheduleOptionsJUnitTest() {
        //
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

    @Test
    public void testSchedulerHost() {
        objOptions.SchedulerHostName.Value("++----++");
        assertEquals("", objOptions.SchedulerHostName.Value(), "++----++");
    }

    @Test
    public void testSchedulerPort() {
        objOptions.scheduler_port.value(4139);
        assertEquals("", objOptions.scheduler_port.value(), 4139);
    }

    @Test
    public void testdayOffset() {
        objOptions.dayOffset.value(7);
        assertEquals("", objOptions.dayOffset.value(), 7);
    }

    @Test
    public void testconfiguration_file() {
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

}