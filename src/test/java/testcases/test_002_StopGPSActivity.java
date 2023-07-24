package testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import common.commonMethods;

public class test_002_StopGPSActivity {
	
	public static String[] envType = {"cdt","stage","prod"};
	
	public static String activityName;
	public String[] aData = {"TEST1234567", "12345678", "", "44.419198", "8.882268", "",""};

	@Test
	public void test_StopGPSActivity_002() throws InterruptedException {
		
		activityName = "START";
		
		aData[2] = commonMethods.getCurrentDateTime();
		String sessionToken = commonMethods.getTokenID(envType[0],activityName, aData);
		
		activityName = "STOP";
		
		aData[2] = commonMethods.getCurrentDateTime();
		String statusMessage = commonMethods.postGPSMethods(envType[0], activityName, sessionToken, aData);
		Assert.assertEquals(statusMessage, "PASS");
	}

}
