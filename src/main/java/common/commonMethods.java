package common;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;


import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestResult;
import org.testng.Reporter;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import payload.getPayLoad;

public class commonMethods {

	public static String consumerKey = "F6jcdbKPA0O1eEgFUt8hzwNZ2rP4PimJ";
	public static String urlCdt = "https://api-cdt.maersk.com/truckerlogapp-api/InlandRVP/TruckerLogApp";
	public static String urlStage = "https://api-stage.maersk.com/truckerlogapp-api/InlandRVP/TruckerLogApp";
	public static String urlProd = "https://api.maersk.com/truckerlogapp-api/InlandRVP/TruckerLogApp";
	public static String containerID = "TEST1234567";
	public static String workOrderID = "12345678";
	public static String delay_reason = "CN_DAMAGE";
	public static String dealy_time = "35";

	public static String[] getEnvDetails(String envType ) {
	
		String[] a = new String[]{"", "", ""};
		
		if(envType.equals("cdt"))
		{
			a[0] = urlCdt;
		}
		else if(envType.equals("prod"))
		{
			a[0] = urlCdt;
		}
		else
		{
			a[0] = urlStage;
		}
		return a;
		
		
	}
	
	public static String getCurrentDateTime() {
		
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("HH:mm:ss");

		String dt1 = df1.format(new Date());
		String dt2 = df2.format(new Date());
		String dateTime = dt1+"T"+dt2;
		
		return dateTime;
	}
	
	public static String readDataFromExcel(String envType, String activityName) throws IOException {
		
		 String sessionToken = null;
		 String statusMessage = "";
		 String[] aData = {"", "", "", "", "", "",""};
		 
		 DataFormatter formatter = new DataFormatter(Locale.US);
		 
		 	aData[0] = containerID;
			aData[1] = workOrderID;
			aData[2] = getCurrentDateTime();
			aData[5] = "";
			aData[6] = "";
	
		//Create an object of File class to open xlsx file

	    File myFile =    new File("/Users/nethrapapegowda/eclipse-workspace/TruckerLogAPITest/src/main/resources/latlong.xlsx");

	    FileInputStream fis = new FileInputStream(myFile); 
	    
	    // Finds the workbook instance for XLSX file 
	    XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); 
	    // Return first sheet from the XLSX workbook 
	    XSSFSheet mySheet = myWorkBook.getSheetAt(0); 
	    // Get iterator to all the rows in current sheet 
	   
	    for (int i=0; i <mySheet.getLastRowNum(); i++) {
	        Row r = mySheet.getRow(i);
	        if (r == null) { 
	           // empty row, skip
	        } 
	        else
	        {
	           String lat = formatter.formatCellValue(r.getCell(0));
	           String lng =  formatter.formatCellValue(r.getCell(1));
	           
	           aData[3] = lat;
	           aData[4] = lng;
		    	

	           if(i==0)
		    	{
		    		activityName = "START";
		    		sessionToken = getTokenID(envType,activityName,aData);
		    		if(sessionToken.contains("FAIL"))
		    		{
		    			statusMessage = "FAIL";
	           			break;
		    		}
		    	}
	           else if(i==mySheet.getLastRowNum()-1)
	           {
	        	    activityName = "STOP";
	           		statusMessage = postGPSMethods(envType, activityName, sessionToken, aData);
	           		if(statusMessage.contains("FAIL"))
	           			break;
	           }
	           else if(i==10)
		    	{
		    		activityName = "DELAY";
		    		aData[5] = delay_reason;
					aData[6] = dealy_time;
		    		statusMessage = postGPSMethods(envType, activityName, sessionToken, aData);
		    		if(statusMessage.contains("FAIL"))
	           			break;
		    	}
	           else
		    	{
		    		activityName = "ENROUTE";
		    		statusMessage = postGPSMethods(envType, activityName, sessionToken, aData);
		    		if(statusMessage.contains("FAIL"))
	           			break;
		    	}
	        }
	 }
        
	    
	fis.close();
	return statusMessage;
	    
}

	public static String getTokenID(String envType, String activityName, String[] aData) {
		
		String[] b = getEnvDetails(envType );
		String envUrl=b[0];
		String bodyText = getPayLoad.getGPSData(activityName,aData);

		RestAssured.baseURI = envUrl;

		Response response = 
				given().log().all()
				.header("Consumer-Key", consumerKey)
				.header("Content-Type", "application/json")
				.header("device_id", "8aeefff3913c4fa4ba9adb87928e3013")
				
				.body(bodyText)

				.when().post("/Gps")

				.then().log().all().extract().response();

		int status = response.statusCode();
		JsonPath js = new JsonPath(response.asString());
		
		if(status == 200 )
		{
			String token = js.getString("session_token");
			Reporter.log("Token ID is Generated" + token, 2, true);
			return token;
		}
		else
		{
			//String message = js.getString("message");
			Reporter.log("Token ID is not Generated",  2, false);
			String message = "FAIL";
			return message;
		}
	
	}
	
	public static String postGPSMethods(String envType, String activityName, String sessionToken, String[] aData) {
		
		String[] b = getEnvDetails(envType);
		String envUrl=b[0];
		
		String statusMessage = "";
		
		String bodyText = getPayLoad.getGPSData(activityName,aData);
		Response response = null;
		int status = 0;
		
		RestAssured.baseURI = envUrl;
		
		if(activityName.contains("STOP") || activityName.contains("ENROUTE") || activityName.contains("DELAY"))
		{
			 response = 
					given().log().all()
					.header("Consumer-Key", consumerKey)
					.header("Content-Type", "application/json")
					.header("device_id", "8aeefff3913c4fa4ba9adb87928e3013")
					.header("session_token", sessionToken)
					
					.body(bodyText)

					.when().post("/Gps")

					.then().log().all().extract().response();
			 
			 status = response.statusCode();
			 //JsonPath js = new JsonPath(response.asString());
				
			if(status == 200 )
			{
				statusMessage = "PASS";
				Reporter.log("Requested sent Sucessfully", 2, true);
			}
			else
			{
				statusMessage = "FAIL";
				Reporter.log("Requested Failed", 2, false);
			}
		}
		else
		{
			statusMessage = "FAIL";
		}
		
		return statusMessage;
	
	}
	
}