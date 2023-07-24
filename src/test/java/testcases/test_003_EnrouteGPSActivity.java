package testcases;

import java.io.IOException;


import org.testng.Assert;
import org.testng.annotations.Test;

import common.commonMethods;

public class test_003_EnrouteGPSActivity {
	
	public static String[] envType = {"cdt","stage","prod"};
	public static String activityName;

	@Test
	public void test_EnrouteGPSActivity_003() throws IOException {
		
		activityName = "ENROUTE";
		String statusMessage = commonMethods.readDataFromExcel(envType[0],activityName);
		Assert.assertEquals(statusMessage, "PASS");
	}
		
}
