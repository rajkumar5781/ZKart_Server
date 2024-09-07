package fetcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

import constants.Constants;

public class CSVFetcher {

	public void activityCSVWriter(String[] writeContent) {
	    try {
	        String filePath = Constants.XML_ROOT_PATH + Constants.ACTIVITY_LOG_FILE_NAME_STRING + ".csv";
	        FileWriter fileWriter = new FileWriter(filePath, true);
	        CSVWriter writer = new CSVWriter(fileWriter);
	        writer.writeNext(writeContent);
	        writer.close(); // Close the writer to release resources
	        System.out.println("Content written to CSV file successfully.");
	    } catch (IOException e) {
	        // Handle file I/O errors
	        e.printStackTrace();
	        // Log the error message
	        System.out.println("Failed to write content to CSV file: " + e.getMessage());
	    } catch (Exception e) {
	        // Handle other exceptions
	        e.printStackTrace();
	        // Log the error message
	        System.out.println("An error occurred: " + e.getMessage());
	    }
	}
}
