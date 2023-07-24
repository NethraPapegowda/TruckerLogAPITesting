package testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import common.commonMethods;

public class test_001_StartGPSActivity {
	
	public static String[] envType = {"cdt","stage","prod"};
	public static String activityName;
	public String[] aData = {"TEST1234567", "12345678", "", "44.419198", "8.882268", "",""};

	@Test
	public void test_StartGPSActivity_001() throws IOException {
		
		activityName = "START";
	 	
		aData[2] = commonMethods.getCurrentDateTime();

		String statusMessage = commonMethods.getTokenID(envType[0],activityName, aData);
				
		
		if(statusMessage.equals("FAIL"))
		{
			Assert.assertTrue(false);
			
		}
		else
		{
			 Assert.assertTrue(true);
		}
	}
}
