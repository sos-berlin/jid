

package com.sos.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;

/**
 * \class 		CreateGenerateOrderJUnitTest - JUnit-Test for "Creating PDIR generate order"
 *
 * \brief MainClass to launch CreateGenerateOrder as an executable command-line program
 *

 *
 *
 * \verbatim ;
 * mechanicaly created by C:\ProgramData\sos-berlin.com\jobscheduler\scheduler_ur\config\JOETemplates\java\xsl\JSJobDoc2JSJUnitClass.xsl from http://www.sos-berlin.com at 20130225111725 
 * \endverbatim
 */
public class CreateGenerateOrderJUnitTest extends JSToolBox {
	@SuppressWarnings("unused")	 
	private final static String					conClassName						= "CreateGenerateOrderJUnitTest"; //$NON-NLS-1$
	@SuppressWarnings("unused")	 
	private static Logger		logger			= Logger.getLogger(CreateGenerateOrderJUnitTest.class);

	protected CreateGenerateOrderOptions	objOptions			= null;
	private CreateGenerateOrder objE = null;
	
	
	public CreateGenerateOrderJUnitTest() {
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
		objE = new CreateGenerateOrder();
		objE.registerMessageListener(this);
		objOptions = objE.Options();
		objOptions.registerMessageListener(this);
		
		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = 9;
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() throws Exception {
		
		
		objE.Execute();
		
//		assertEquals ("auth_file", objOptions.auth_file.Value(),"test"); //$NON-NLS-1$
//		assertEquals ("user", objOptions.user.Value(),"test"); //$NON-NLS-1$


	}
}  // class CreateGenerateOrderJUnitTest