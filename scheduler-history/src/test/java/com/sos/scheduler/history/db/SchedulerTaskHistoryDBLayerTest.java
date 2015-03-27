package com.sos.scheduler.history.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sos.resources.SOSResourceFactory;
import com.sos.resources.SOSTestResource;

/**
 * \class SchedulerHistoryDBLayerTest
 * 
 * \brief SchedulerHistoryDBLayerTest -
 * 
 * \details
 * 
 * \section SchedulerHistoryDBLayerTest.java_intro_sec Introduction
 * 
 * \section SchedulerHistoryDBLayerTest.java_samples Some Samples
 * 
 * \code .... code goes here ... \endcode
 * 
 * <p style="text-align:center">
 * <br />
 * --------------------------------------------------------------------------- <br />
 * APL/Software GmbH - Berlin <br />
 * ##### generated by ClaviusXPress (http://www.sos-berlin.com) ######### <br />
 * ---------------------------------------------------------------------------
 * </p>
 * \author Uwe Risse \version 14.10.2011 \see reference
 * 
 * Created on 14.10.2011 14:22:06
 */

public class SchedulerTaskHistoryDBLayerTest {

  @SuppressWarnings("unused")
  private final String conClassName = "SchedulerHistoryDBLayerTest";
  private SchedulerTaskHistoryDBLayer schedulerTaskHistoryDBLayer;
  private final String configurationFilename = "R:/nobackup/junittests/hibernate/hibernate_oracle.cfg.xml";
  private File configurationFile;

  public SchedulerTaskHistoryDBLayerTest() {
  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    configurationFile = SOSResourceFactory
        .asFile(SOSTestResource.HIBERNATE_CONFIGURATION_ORACLE);
    ;
    schedulerTaskHistoryDBLayer = new SchedulerTaskHistoryDBLayer(
        configurationFile);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testDeleteString() throws ParseException {
    schedulerTaskHistoryDBLayer.getFilter().setExecutedFrom(
        "2011-01-01 00:00:00");
    schedulerTaskHistoryDBLayer.getFilter()
        .setExecutedTo("2011-10-01 00:00:00");
    schedulerTaskHistoryDBLayer.deleteInterval();
    long i = schedulerTaskHistoryDBLayer.deleteInterval();
    schedulerTaskHistoryDBLayer.commit();
    assertEquals("testDeleteString fails...:", 0, i);
  }

  @Test
  @Ignore("Test set to Ignore for later examination")
  public void testGetSchedulerTaskHistoryList() throws ParseException {
    schedulerTaskHistoryDBLayer.getFilter().setExecutedFrom(
        "2011-01-01 00:00:00");
    schedulerTaskHistoryDBLayer.getFilter().setExecutedTo(new Date());
    List<SchedulerTaskHistoryDBItem> historyList = schedulerTaskHistoryDBLayer
        .getSchedulerHistoryListFromTo();
    assertEquals("testGetSchedulerTaskHistoryList fails...:", 1,
        historyList.size());
  }

  @Test
  @Ignore("Test set to Ignore for later examination")
  public void testGetTaskHistoryItems() throws Exception {
    schedulerTaskHistoryDBLayer.getFilter().setExecutedFrom(
        "2011-01-01 00:00:00");
    schedulerTaskHistoryDBLayer.getFilter().setExecutedTo(new Date());
    List<SchedulerTaskHistoryDBItem> historyList = schedulerTaskHistoryDBLayer
        .getHistoryItems();
    assertEquals("testTaskGetHistoryItems fails...:", 1, historyList.size());
  }

  @Test
  @Ignore("Test set to Ignore for later examination")
  public void testGetTaskHistoryItem() throws Exception {
    schedulerTaskHistoryDBLayer.getFilter().setExecutedFrom(
        "2010-01-01 00:00:00");
    schedulerTaskHistoryDBLayer.getFilter().setExecutedTo(new Date());
    List<SchedulerTaskHistoryDBItem> historyList = schedulerTaskHistoryDBLayer
        .getHistoryItems();
    assertEquals("testGetTaskHistoryItem fails...:", 1, historyList.size());
    Long id = historyList.get(0).getId();

    SchedulerTaskHistoryDBItem schedulerHistoryDBItem = schedulerTaskHistoryDBLayer
        .get(id);
    assertEquals("testGetTaskHistoryItem fails...:", id,
        schedulerHistoryDBItem.getId());
  }

  @Test
  @Ignore("Test set to Ignore for later examination")
  public void testSaveHistory() {
    schedulerTaskHistoryDBLayer.beginTransaction();
    schedulerTaskHistoryDBLayer.getFilter().setSchedulerId("Hibernate_Spooler");
    schedulerTaskHistoryDBLayer.delete("SchedulerHistoryDBItem");
    schedulerTaskHistoryDBLayer.commit();

    SchedulerTaskHistoryDBItem historyTable = new SchedulerTaskHistoryDBItem();
    historyTable.setErrorText("Thunfisch");
    historyTable.setSpoolerId("Hibernate_Spooler");
    historyTable.setJobName("Hibernate_JobName");
    historyTable.setStartTime(new Date());
    historyTable.setEndTime(new Date());

    schedulerTaskHistoryDBLayer.save(historyTable);

    assertNotNull(historyTable.getId());

    schedulerTaskHistoryDBLayer.commit();

    Long id = historyTable.getId();
    schedulerTaskHistoryDBLayer.beginTransaction();
    SchedulerTaskHistoryDBItem loadedhistory = schedulerTaskHistoryDBLayer
        .get(id);
    assertNotNull(loadedhistory);
    assertEquals("Thunfisch", loadedhistory.getErrorText());
    schedulerTaskHistoryDBLayer.commit();
  }

  // Test runs local but fails on Jenkins [SP]
  @Test
  @Ignore("Test set to Ignore for later examination")
  public void testDeleteInterval() throws ParseException {
    schedulerTaskHistoryDBLayer.beginTransaction();
    long i = schedulerTaskHistoryDBLayer.deleteInterval(30);
    schedulerTaskHistoryDBLayer.beginTransaction();
    i = schedulerTaskHistoryDBLayer.deleteInterval(3);
    assertEquals("testDeleteInterval fails...:", 0, i);
  }

}
