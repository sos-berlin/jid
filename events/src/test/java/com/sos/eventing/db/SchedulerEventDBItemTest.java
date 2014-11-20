package com.sos.eventing.db;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
* \class SchedulerEventDBItemTest 
* 
* \brief SchedulerEventDBItemTest - 
* 
* \details
*
* \section SchedulerEventDBItemTest.java_intro_sec Introduction
*
* \section SchedulerEventDBItemTest.java_samples Some Samples
*
* \code
*   .... code goes here ...
* \endcode
*
* <p style="text-align:center">
* <br />---------------------------------------------------------------------------
* <br /> APL/Software GmbH - Berlin
* <br />##### generated by ClaviusXPress (http://www.sos-berlin.com) #########
* <br />---------------------------------------------------------------------------
* </p>
* \author Uwe Risse
* \version 07.05.2013
* \see reference
*
* Created on 07.10.2011 11:35:33
 */

public class SchedulerEventDBItemTest {

	@SuppressWarnings("unused")
	private final String	conClassName	= "SchedulerEventDBItemTest";
    private SchedulerEventDBItem   schedulerEventDBItem=null;
 
    
	public SchedulerEventDBItemTest() {
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
		schedulerEventDBItem= new SchedulerEventDBItem();
	}

	@After
	public void tearDown() throws Exception {
	}
 
	@Test
	public void testSetId() {
		Long myId = new Long(4711);
		schedulerEventDBItem.setId(myId);
		Long id = schedulerEventDBItem.getId();
		assertEquals("testSetid faild: ",myId,id);
	}

	@Test
	public void testSetSchedulerId() {
		String myId = "mySchedulerId";
		schedulerEventDBItem.setSchedulerId(myId);
		String id = schedulerEventDBItem.getSchedulerId();
		assertEquals("testSetSchedulerId faild: ",myId,id);
	}

	 
	@Test
	public void testSetJobName() {
	  String myJobName = "JobName";
      schedulerEventDBItem.setJobName(myJobName);
      String jobName = schedulerEventDBItem.getJobName();
      assertEquals("testSetjob failed: ",myJobName,jobName);
	}

 

	@Test
	public void testSetOrderId() {
		String myId = "myId";
		schedulerEventDBItem.setOrderId(myId);
		String id = schedulerEventDBItem.getOrderId();
		assertEquals("testSetOrderId faild: ",myId,id);		}

	 
	@Test
	public void testSetJobChain() {
	  String myJobChain = "JobChain";
      schedulerEventDBItem.setJobChain(myJobChain);
      String jobChain = schedulerEventDBItem.getJobChain();
      assertEquals("testSetjobChain failed: ",myJobChain,jobChain);
	}
   
	@Test
	public void testSetEventId() {
	  String myEventId = "EventId";
      schedulerEventDBItem.setEventId(myEventId);
      String eventId = schedulerEventDBItem.getEventId();
      assertEquals("testSetEventId failed: ",myEventId,eventId);
	}
	
	@Test
	public void testSetEventClas() {
	  String myEventClass = "EventClass";
      schedulerEventDBItem.setEventClass(myEventClass);
      String eventClass = schedulerEventDBItem.getEventClass();
      assertEquals("testSetEventClass failed: ",myEventClass,eventClass);
	}
    
	@Test
	public void testSetExitCode(){
	  String myExitCode = "ExitCode";
      schedulerEventDBItem.setExitCode(myExitCode);
      String eventCode = schedulerEventDBItem.getExitCode();
      assertEquals("testSetExitCode failed: ",myExitCode,eventCode);
	}
    
}
