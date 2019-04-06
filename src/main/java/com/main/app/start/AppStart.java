package com.main.app.start;

import com.main.app.constants.AppKeys;
import com.main.app.mail.MailUtils;
import com.utils.FileUtils;
import com.utils.LoggingUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class AppStart {

    private static Properties properties = new Properties();

    private static boolean loadPropsToSytem() {
        try {
            properties.load(new FileInputStream("./src/main/resources/application.properties"));
            Set<String> keys = properties.stringPropertyNames();
            for (String property : keys) {
                System.setProperty(property, properties.getProperty(property));
            }
            return true;
        }
        catch (FileNotFoundException exception) {
            System.out.println("Error getting application properties file. Make sure the application.properties file exists");
        }
        catch (IOException exception) {
            System.out.println("Error loading application properties file.");
        }
        return false;
    }

    public static void main(String[] args) {
        if(loadPropsToSytem() && LoggingUtils.setupLogging() && FileUtils.setupQRLocation()) {
            try {
                boolean testMail = Boolean.parseBoolean(System.getProperty(AppKeys.APP_TEST));
                boolean isCustomBody = Boolean.parseBoolean(System.getProperty(AppKeys.APP_TEST_CUSTOM_MAIL_BODY));
                if (testMail) {
                    MailUtils.sendTestMail(System.getProperty(AppKeys.APP_TEST_MAIL), isCustomBody);
                    System.out.println("Test mail sent successfully....");
                } else {
                    QRRunner.triggerQRJob(FileUtils.loadFile(System.getProperty(AppKeys.EXCEL_FILE_LOCATION)));
                    System.out.println("Email have been sent out. Kindly check the logs for more info..");
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                LoggingUtils.logError(ex);
                System.out.println("Exception occurred, please check the properties file values and try again....");
            }
            FileUtils.deleteQRDir();
        }
        else {
            System.out.println("Exiting....");
        }
    }

}


