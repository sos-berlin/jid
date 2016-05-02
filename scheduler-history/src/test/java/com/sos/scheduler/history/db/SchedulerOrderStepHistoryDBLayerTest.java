package com.sos.scheduler.history.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SchedulerOrderStepHistoryDBLayerTest {

    private SchedulerOrderStepHistoryDBLayer schedulerOrderStepHistoryDBLayer;
    private final String configurationFilename = "R:/nobackup/junittests/hibernate/hibernate_oracle.cfg.xml";

    public SchedulerOrderStepHistoryDBLayerTest() {
    }

    @Before
    public void setUp() throws Exception {
        schedulerOrderStepHistoryDBLayer = new SchedulerOrderStepHistoryDBLayer(configurationFilename);
    }

    @Test
    public void testSchedulerOrderStepHistoryDBLayer() {
        SchedulerOrderStepHistoryDBLayer d = new SchedulerOrderStepHistoryDBLayer(configurationFilename);
        assertNotNull(d);
    }

    @Test
    public void testDeleteString() throws ParseException {
        schedulerOrderStepHistoryDBLayer.filter.setExecutedFrom("2011-01-01 00:00:00");
        schedulerOrderStepHistoryDBLayer.filter.setExecutedTo("2011-10-01 00:00:00");
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testGetSchedulerOrderStepHistoryList() throws Exception {
        schedulerOrderStepHistoryDBLayer.filter.setExecutedFrom("2000-01-01 00:00:00");
        schedulerOrderStepHistoryDBLayer.filter.setExecutedTo("2020-01-01 00:00:00");
        List<SchedulerOrderStepHistoryDBItem> historyList = schedulerOrderStepHistoryDBLayer.getSchedulerOrderStepHistoryListFromTo(1);
        assertEquals("testGetSchedulerOrderStepHistoryList fails...:", 1, historyList.size());
    }

    @Test
    @Ignore("Test set to Ignore for later examination")
    public void testGetOrderStepHistoryItems() throws Exception {
        schedulerOrderStepHistoryDBLayer.filter.setExecutedFrom("2000-01-01 00:00:00");
        schedulerOrderStepHistoryDBLayer.filter.setExecutedTo(new Date());
        List<SchedulerOrderStepHistoryDBItem> historyList = schedulerOrderStepHistoryDBLayer.getSchedulerOrderStepHistoryListFromTo(1);
        assertEquals("testGetOrderStepHistoryList fails...:", 1, historyList.size());
    }

}