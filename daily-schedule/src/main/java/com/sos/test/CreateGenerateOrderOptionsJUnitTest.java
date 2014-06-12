

package com.sos.test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;
import org.apache.log4j.Logger;
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 * \class 		CreateGenerateOrderOptionsJUnitTest - Creating PDIR generate order
 *
 * \brief
 *
 *

 *
 *
 * \verbatim ;
 * mechanicaly created by C:\ProgramData\sos-berlin.com\jobscheduler\scheduler_ur\config\JOETemplates\java\xsl\JSJobDoc2JSJUnitOptionSuperClass.xsl from http://www.sos-berlin.com at 20130225111725
 * \endverbatim
 *
 * \section TestData Eine Hilfe zum Erzeugen einer HashMap mit Testdaten
 *
 * Die folgenden Methode kann verwendet werden, um für einen Test eine HashMap
 * mit sinnvollen Werten für die einzelnen Optionen zu erzeugen.
 *
 * \verbatim
 private HashMap <String, String> SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) {
	pobjHM.put ("		CreateGenerateOrderOptionsJUnitTest.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
public class CreateGenerateOrderOptionsJUnitTest extends  JSToolBox {
	private final String					conClassName						= "CreateGenerateOrderOptionsJUnitTest"; //$NON-NLS-1$
		@SuppressWarnings("unused") 
	private static Logger		logger			= Logger.getLogger(CreateGenerateOrderOptionsJUnitTest.class);
	@SuppressWarnings("unused")
	private CreateGenerateOrder objE = null;

	protected CreateGenerateOrderOptions	objOptions			= null;

	public CreateGenerateOrderOptionsJUnitTest() {
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




/**
 * \brief testhibernate_configuration_file :
 *
 * \details
 *
 *
 */
    @Test
    public void testhibernate_configuration_file() {  // SOSOptionString
    	 objOptions.hibernate_configuration_file.Value("++----++");
    	 assertEquals ("", objOptions.hibernate_configuration_file.Value(),"++----++");

    }



/**
 * \brief testnumber_of_documents :
 *
 * \details
 *
 *
 */
    @Test
    public void testnumber_of_documents() {  // SOSOptionString
    	 objOptions.number_of_documents.Value("++----++");
    	 assertEquals ("", objOptions.number_of_documents.Value(),"++----++");

    }



/**
 * \brief testnumber_of_documents_probe :
 *
 * \details
 *
 *
 */
    @Test
    public void testnumber_of_documents_probe() {  // SOSOptionString
    	 objOptions.number_of_documents_probe.Value("++----++");
    	 assertEquals ("", objOptions.number_of_documents_probe.Value(),"++----++");

    }



/**
 * \brief testnumber_of_sample_documents :
 *
 * \details
 *
 *
 */
    @Test
    public void testnumber_of_sample_documents() {  // SOSOptionString
    	 objOptions.number_of_sample_documents.Value("++----++");
    	 assertEquals ("", objOptions.number_of_sample_documents.Value(),"++----++");

    }



/**
 * \brief testoutput_file_name :
 *
 * \details
 *
 *
 */
    @Test
    public void testoutput_file_name() {  // SOSOptionString
    	 objOptions.output_file_name.Value("++----++");
    	 assertEquals ("", objOptions.output_file_name.Value(),"++----++");

    }



/**
 * \brief testsource_id :
 *
 * \details
 *
 *
 */
    @Test
    public void testsource_id() {  // SOSOptionString
    	 objOptions.source_id.Value("++----++");
    	 assertEquals ("", objOptions.source_id.Value(),"++----++");

    }



/**
 * \brief testworkflow_execution_id :
 *
 * \details
 *
 *
 */
    @Test
    public void testworkflow_execution_id() {  // SOSOptionString
    	 objOptions.workflow_execution_id.Value("++----++");
    	 assertEquals ("", objOptions.workflow_execution_id.Value(),"++----++");

    }



} // public class CreateGenerateOrderOptionsJUnitTest