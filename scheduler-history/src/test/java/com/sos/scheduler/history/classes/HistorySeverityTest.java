package com.sos.scheduler.history.classes;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.scheduler.history.classes.HistorySeverity;

 

public class HistorySeverityTest {

	@SuppressWarnings("unused")
	private final String	conClassName	= "HistorySeverityTest";
	private HistorySeverity historySeverity = null;

	public HistorySeverityTest() {
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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHistorySeverityString() {
		historySeverity = new HistorySeverity("success");
		int v = historySeverity.getIntValue();
        assertEquals("Test HistorySeverityString fails ...", 0,v);		

	}

	@Test
	public void testHistorySeverityInteger() {
		historySeverity = new HistorySeverity(0);
        assertEquals("Test historySeverityInteger fails ...", "success",historySeverity.getStrValue());		
	}
	 

	@Test
	public void testSetStrValue() {
		historySeverity = new HistorySeverity(0);
        assertEquals("Test setStrValue fails ...", "success",historySeverity.getStrValue());		
        historySeverity.setStrValue("error");
		int v = historySeverity.getIntValue();
        assertEquals("Test setStrValue fails ...", 1,v);		
	}

	 
	@Test
	public void testSetIntValue() {
		historySeverity = new HistorySeverity("success");
        assertEquals("Test setIntValue fails ...", "success",historySeverity.getStrValue());		
        historySeverity.setIntValue(1);
        assertEquals("Test setIntValue fails ...", "error",historySeverity.getStrValue());		
	}

	@Test
	public void testHasValue() {
		historySeverity = new HistorySeverity(1);
		historySeverity.setStrValue("");
        assertEquals("Test hasValue fails ...", false,historySeverity.hasValue());		
        historySeverity.setIntValue(1);
        assertEquals("Test hasValue fails ...", true,historySeverity.hasValue());		
	}
	
	@Test
	public void testGetConClassName() {
		historySeverity = new HistorySeverity(1);
		assertEquals("Test getConClassName fails ...", "HistorySeverity",historySeverity.getConClassName());			
	}
}
