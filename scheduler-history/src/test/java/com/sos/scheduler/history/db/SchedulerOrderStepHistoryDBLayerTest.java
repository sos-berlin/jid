package com.sos.scheduler.history.db;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SchedulerOrderStepHistoryDBLayerTest {

    @SuppressWarnings("unused")
    private final String conClassName = "SchedulerOrderStepHistoryDBLayerTest";
    private SchedulerOrderStepHistoryDBLayer schedulerOrderStepHistoryDBLayer;
    private final String configurationFilename = "R:/nobackup/junittests/hibernate/hibernate_oracle.cfg.xml";
    private Logger logger = Logger.getLogger(SchedulerOrderStepHistoryDBLayerTest.class);

    public SchedulerOrderStepHistoryDBLayerTest() {
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        schedulerOrderStepHistoryDBLayer = new SchedulerOrderStepHistoryDBLayer(configurationFilename);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSchedulerOrderStepHistoryDBLayer() {
        SchedulerOrderStepHistoryDBLayer d = new SchedulerOrderStepHistoryDBLayer(configurationFilename);
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
