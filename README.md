# QRMailingService-GoogleForms
Given an event registration excel sheet, this service will mail all the registered people with a QR code. You can scan the QR code on the day of the event using any QR scanning app.

The entire app revolves around the application.properties. 

# Steps to Make it work:
1. Open the Application in the desired IDE.
2. Open the AppStart.java class.
3. Before you run the main method, have a look at the application.properties file.

# Explaining the application.properties file
1. Specify the username and password of the account from which you want to send the mail from. Please specify only the username and not the entire email address.
2. Until this process is done, you will have to turn on "Access to Less Secure Apps" in the settings page of your google accout. This feature can be turned off once the process is over.
3. You will have to specify the header name of the column containing the email ID's in the excel.column.mail.name key.
4. The location of the excel file is to be specified in the properties file too.
5. The row number of the excel sheet heading is to be specified, so as to know where the data starts from.
6. app.test can be set to true to send a test mail to the specified test mail id to check your mail. For this a SamplePNG.png will be used. Deleting the file may make the application non functional.
7. Custom mail template can be used which has to be specified in the MailMessage.html file.
8. Other details like the mail subject can also be specified in the application.properties file.

Once you have setup the properties file, run the AppStart.java and wait for the process to complete. Logging will be done in file called Logging.log.

# Note: 
Only google accounts are supported. 
If you want me to send over the mails or need any assistance, please drop a mail at ashsat.vijayan122@gmail.com 
Kindly report any bugs at ashsat.vijayan122@gmail.com.
