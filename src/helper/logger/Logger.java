package helper.logger;

import helper.Constants.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Logger class that holds the logger method
 */
public class Logger {
	/**
	 * Logger method used to create and write the login_activity.txt file.
	 * Uses the BufferedWriter api to append and write a single text line in the file
	 * @param userName
	 * @param success
	 * @throws IOException
	 */
	public static void loginLog(String userName, Boolean success) throws IOException{
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.LOGIN_TXT_PATH, true));
			bw.append(ZonedDateTime.now(ZoneOffset.UTC).toString())
					.append(" UTC")
					.append(Constants.LOGIN_ATTEMPT)
					.append(userName)
					.append(Constants.LOGIN_SUCCESS_STRING)
					.append(success.toString())
					.append("\n");
			bw.flush();
			bw.close();
		} catch (IOException e){
			e.printStackTrace();
		}

	}




}
