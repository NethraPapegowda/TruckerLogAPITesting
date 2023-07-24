package payload;

public class getPayLoad {

	public static String getGPSData1(String activityName, String dateTime)
	{
		return "[{\n"
				+ "  \"activity\": \""+ activityName +"\",\n"
				+ "  \"latitude\": \"44.419198\",\n"
				+ "  \"longitude\": \"8.882268\",\n"
				+ "  \"position_ts\": \"" + dateTime + "\",\n"
				+ "  \"container_id\": \"TEST1234567\",\n"
				+ "  \"work_order_id\": \"72000166550\"\n"
				+ "}]";
	}

	public static String getGPSData(String activityName, String[] enrouteData)
	{
		
		String containerId = enrouteData[0];
		String workOrderId = enrouteData[1];
		String dateTime = enrouteData[2];
		String latitude = enrouteData[3];
		String longitude = enrouteData[4];
		String delay_reason = enrouteData[5];
		String dealy_time = enrouteData[6];
		
		if(activityName.contains("DELAY"))
		{
			activityName = "ENROUTE";
			return "[{\n"
				+ "  \"container_id\": \""+containerId+"\",\n"
				+ "  \"work_order_id\": \""+workOrderId+"\",\n"
				+ "  \"latitude\": \""+latitude+"\",\n"
				+ "  \"longitude\": \""+longitude+"\",\n"
				+ "  \"position_ts\": \""+dateTime+"\",\n"
				+ "  \"activity\": \""+activityName+"\",\n"
				+ "  \"delay_reason\": \""+delay_reason+"\",\n"
				+ "  \"delay_time\": \""+dealy_time+"\"\n"
				+ "}]\n"
				+ "";
		}
		else
		{
			return "[{\n"
			+ "  \"container_id\": \""+containerId+"\",\n"
			+ "  \"work_order_id\": \""+workOrderId+"\",\n"
			+ "  \"latitude\": \""+latitude+"\",\n"
			+ "  \"longitude\": \""+longitude+"\",\n"
			+ "  \"position_ts\": \""+dateTime+"\",\n"
			+ "  \"activity\": \""+activityName+"\"\n"
			+ "}]\n"
			+ "";
		}
	}

}
