import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

	public class APIAutomationTest {

	    private static final String REQUEST_JSON_FILE = "C:/Temp/requestJson.json";
	    private static final String TECH_DETAILS_JSON_FILE = "C:/Temp/TechDetails.json";
	    private static final String ADDRESSES_JSON_FILE = "C:/Temp/addresses.json";
	    private static final String API_ENDPOINT = "https://thetestingworldapi.com/api/studentsDetails";
	    private static final String TECH_SKILLS_ENDPOINT = "https://thetestingworldapi.com/api/technicalskills";
	    private static final String ADDRESSES_ENDPOINT = "https://thetestingworldapi.com/api/addresses";
	    private static final String FINAL_STUDENT_DETAILS_ENDPOINT = "https://thetestingworldapi.com/api/FinalStudentDetails/";
		public static void main(String[] args) {
	        try {
	            // Step 1: Read the content of requestJson.json 
	            String requestBody = new String(Files.readAllBytes(Paths.get(REQUEST_JSON_FILE)));

	            // Step 2: Send POST request to studentsDetails endpoint
	            URL url = new URL(API_ENDPOINT);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Accept", "application/json");
	            connection.setDoOutput(true);
	            try (OutputStream outputStream = connection.getOutputStream()) {
	                outputStream.write(requestBody.getBytes());
	                outputStream.flush();
	            }

	            // Step 3: Get the response code
	            int responseCode = connection.getResponseCode();
	            System.out.println("Response Code for requestJson: " + responseCode);

	            // Step 4: Assert the response code starts with 2XX
	            if (responseCode >= 200 && responseCode < 300) {
	                System.out.println("Request completed successfully!");

	                // Step 5: Read and print the response content
	                BufferedReader responseReader = new BufferedReader(
	                        new InputStreamReader(connection.getInputStream()));
	                String responseLine;
	                StringBuilder responseContent = new StringBuilder();
	                while ((responseLine = responseReader.readLine()) != null) {
	                    responseContent.append(responseLine);
	                }
	                responseReader.close();
	                System.out.println("Response Content 0: " + responseContent.toString());

	                // Step 6: Extract the ID value from the response
	                String pattern = "\"id\":(\\d+)";
	             // Create a Pattern object
	             Pattern regex = Pattern.compile(pattern);
	             // Create a Matcher object and apply the regex on the response
	             Matcher matcher = regex.matcher(responseContent.toString());
	             int id = 0; // Default value in case the ID is not found
	             // Check if the regex pattern matches the response and extract the ID
	             if (matcher.find()) {
	                 String idString = matcher.group(1);
	                 id = Integer.parseInt(idString);
	             }
	             System.out.println("REQUEST_ID: " + id);          
	                
	                // Step 7: Update TechJsonFile.json with the ID value
	                updateTechJsonFile(String.valueOf(id));
	                // Step 8: Send POST request to technicalskills endpoint
	                sendTechSkillsRequest();
					//updateAddressesJsonFile(String.valueOf(id));
					updateAddressesJsonFile(id);
	                // Step 9: Send POST request to addresses endpoint
	                sendAddressesRequest();
	                sendFinalStudentDetails(String.valueOf(id));
	                

	            } else {
	                System.out.println("Could not complete request with returned code: " + responseCode);
	            }

	            // Step 10: Close the connection
	            connection.disconnect();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

		  		public static void updateTechJsonFile(String id) {
	        try {
	            // Read the content of techDetailsJson.json file
	            String techDetailsJson = new String(Files.readAllBytes(Paths.get(TECH_DETAILS_JSON_FILE)));
	            System.out.println("ID techDetailsJson : " + id);
	            System.out.println("Response Content techDetailsJson before: " + techDetailsJson);
	            // Update the 'st_Id' key with the extracted ID value
		        techDetailsJson = techDetailsJson.replaceAll("\"id\":\\s*\\d+", "\"id\": " + id).replaceAll("\"st_id\":\\s*\"\\d+\"", "\"st_id\": \"" + id + "\"");
  	            // Write the updated content back to addresses.json file
	            Files.write(Paths.get(TECH_DETAILS_JSON_FILE), techDetailsJson.getBytes());
	            System.out.println("Response Content techDetailsJson after: " + techDetailsJson);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private static void sendTechSkillsRequest() {
	        try {
	            // Read the content of TechDetails.json file
	            String techDetailsJson = new String(Files.readAllBytes(Paths.get(TECH_DETAILS_JSON_FILE)));

	            // Send POST request to technicalskills endpoint
	            URL url = new URL(TECH_SKILLS_ENDPOINT);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Accept", "application/json");
	            connection.setDoOutput(true);
	            try (OutputStream outputStream = connection.getOutputStream()) {
	                outputStream.write(techDetailsJson.getBytes());
	                outputStream.flush();
	            }

	            // Get the response code
	            int responseCode = connection.getResponseCode();
	            System.out.println("Response Code TECH_SKILLS: " + responseCode);

	            // Read and print the response content
	            BufferedReader responseReader = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream()));
	            String responseLine;
	            StringBuilder responseContent = new StringBuilder();
	            while ((responseLine = responseReader.readLine()) != null) {
	                responseContent.append(responseLine);
	            }
	            responseReader.close();
	            System.out.println("Response Content TECH_SKILLS: " + responseContent.toString());

	            // Close the connection
	            connection.disconnect();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private static void updateAddressesJsonFile(int id) {
	        try {
	            // Read the content of addresses.json file
	            String addressesJson = new String(Files.readAllBytes(Paths.get(ADDRESSES_JSON_FILE)));
	            System.out.println("ID AddressesJson : " + id);
	            System.out.println("Response Content AddressesJson before: " + addressesJson);
	            // Update the 'st_id' key with the extracted ID value
	            addressesJson = addressesJson.replaceAll("\"st_id\":\\s*\"\\d+\"", "\"st_id\": \"" + id + "\"");
	            // Write the updated content back to addresses.json file
	            Files.write(Paths.get(ADDRESSES_JSON_FILE), addressesJson.getBytes());
	            System.out.println("Response Content AddressesJson after: " + addressesJson);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
	    private static void sendAddressesRequest() {
	        try {
	            // Read the content of addresses.json file
	            String addressesJson = new String(Files.readAllBytes(Paths.get(ADDRESSES_JSON_FILE)));

	            // Send POST request to addresses endpoint
	            URL url = new URL(ADDRESSES_ENDPOINT);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Accept", "application/json");
	            connection.setDoOutput(true);
	            try (OutputStream outputStream = connection.getOutputStream()) {
	                outputStream.write(addressesJson.getBytes());
	                outputStream.flush();
	            }

	            // Get the response code
	            int responseCode = connection.getResponseCode();
	            System.out.println("Response Code for ADDRESSES_ENDPOINT " + responseCode);

	            // Read and print the response content
	            BufferedReader responseReader = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream()));
	            String responseLine;
	            StringBuilder responseContent = new StringBuilder();
	            while ((responseLine = responseReader.readLine()) != null) {
	                responseContent.append(responseLine);
	            }
	            responseReader.close();
	            System.out.println("Response Content requestJson Adress: " + responseContent.toString());

	            // Close the connection
	            connection.disconnect();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	  //  private static void sendFinalStudentDetails() {
	    private static void sendFinalStudentDetails(String id) {
			// TODO Auto-generated method stub
		      try {
	            // Create the URL for the specific student details
	            URL url = new URL(FINAL_STUDENT_DETAILS_ENDPOINT + id);
	            System.out.println("FINAL_STUDENT_DETAILS_ENDPOINT URL : " + url);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setRequestProperty("Accept", "application/json");
	            System.out.println("FINAL_STUDENT_DETAILS_ENDPOINT : " + id);
	            // Get the response code
	            int responseCode = connection.getResponseCode();
	            System.out.println("Response Code: " + responseCode);

	            // Read and print the response content
	            BufferedReader responseReader = new BufferedReader(
	                    new InputStreamReader(connection.getInputStream()));
	            String responseLine;
	            StringBuilder responseContent = new StringBuilder();
	            while ((responseLine = responseReader.readLine()) != null) {
	                responseContent.append(responseLine);
	            }
	            responseReader.close();

	            System.out.println("Response Content: " + responseContent.toString());

	            // Close the connection
	            connection.disconnect();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}   