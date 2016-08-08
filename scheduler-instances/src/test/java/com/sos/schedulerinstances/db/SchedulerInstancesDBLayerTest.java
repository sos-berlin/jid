package com.sos.schedulerinstances.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
 
import org.junit.Before;
import org.junit.Test;

import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;

public class SchedulerInstancesDBLayerTest {

    private SchedulerInstancesDBLayer schedulerInstancesDBLayer;
    private final String configurationFilename = "R:/nobackup/junittests/hibernate/hibernate_oracle.cfg.xml";

    public SchedulerInstancesDBLayerTest() {
    }

    @Before
    public void setUp() throws Exception {
        schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(configurationFilename);
        assertNotNull(schedulerInstancesDBLayer);
    }

    @Test
    public void testSchedulerOrderStepHistoryDBLayer() {
        SchedulerInstancesDBLayer d = new SchedulerInstancesDBLayer(configurationFilename);
        assertNotNull(d);
    }
 
    @Test
    public void testGetInstanceById() throws Exception {
        SchedulerInstancesDBItem schedulerInstancesDBItem = schedulerInstancesDBLayer.getInstance("scheduler_current","",0);
        assertEquals("testGetInstanceById fails...:", "ur", schedulerInstancesDBItem.getHostname());
    }

    
}