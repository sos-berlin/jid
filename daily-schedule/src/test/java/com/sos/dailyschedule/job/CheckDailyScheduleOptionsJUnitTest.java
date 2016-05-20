package com.sos.dailyschedule.job;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;

public class CheckDailyScheduleOptionsJUnitTest extends JSToolBox {

    protected CheckDailyScheduleOptions objOptions = null;
    private CheckDailySchedule objE = null;

    public CheckDailyScheduleOptionsJUnitTest() {
        //
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

    @Test
    public void testconfiguration_file() {
        objOptions.configuration_file.setValue("++----++");
        assertEquals("Die Datei mit den Einstellungen für Datenbank. Beispiel: <?xml v", objOptions.configuration_file.getValue(), "++----++");
    }

    @Test
    public void testdayOffset() {
        objOptions.dayOffset.setValue("12345");
        assertEquals("", objOptions.dayOffset.getValue(), "12345");
        assertEquals("", objOptions.dayOffset.value(), 12345);
        objOptions.dayOffset.value(12345);
        assertEquals("", objOptions.dayOffset.getValue(), "12345");
        assertEquals("", objOptions.dayOffset.value(), 12345);
    }

    @Test
    public void testscheduler_id() {
        objOptions.scheduler_id.setValue("++----++");
        assertEquals("", objOptions.scheduler_id.getValue(), "++----++");
    }

}