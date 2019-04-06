package com.main.app.mail;
import com.main.app.constants.AppKeys;
import com.utils.FileUtils;
import com.utils.LoggingUtils;
import com.utils.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class MailUtils {
    private static Properties properties = new Properties();

    private static final String QRSECTION = "<QRSECTION>";

    private static final String QR_BASE_LOCATION = FileUtils.QR_BASE_LOCATION;

    static {
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    public static Session getSession() {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(System.getProperty(AppKeys.EMAIL_USERNAME), System.getProperty(AppKeys.EMAIL_PASSWORD));
            }
        });
    }

    public static void sendTestMail(String recipientMail, boolean isCustomMail) throws MessagingException, IOException {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        MimeMultipart content = new MimeMultipart("related");
        message.setFrom(new InternetAddress(System.getProperty(AppKeys.EMAIL_USERNAME)));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(recipientMail));
        message.setSubject("[TEST] Test Mail From " + System.getProperty(AppKeys.EMAIL_USERNAME));
        StringBuilder mailBody = null;
        if (isCustomMail) {
            mailBody = FileUtils.readFileAsStringBuilder("./MailMessage.html");

        }
        else {
            mailBody = new StringBuilder("Thank you for registering, please show the mail during the entry of the event! ");
        }
        String cid = StringUtils.getRandomString();
        int qrIndex = mailBody.indexOf(QRSECTION);
        boolean addQr = true;
        if(qrIndex < 0) {
            addQr = false;
        }
        if(addQr) {
            mailBody.replace(qrIndex, qrIndex + QRSECTION.length(), "<br><h3>QR: </h3><br><img src=\"cid:" + cid + "\">\n");
        }
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(mailBody.toString(), "text/html");
        content.addBodyPart(mimeBodyPart);

        if(addQr) {
            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.attachFile("./SamplePNG.png");
            imagePart.setContentID("<" + cid + ">");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            content.addBodyPart(imagePart);
        }
        message.setContent(content);
        Transport.send(message);
    }


    public static boolean sendMail(String recipientMail, String qrFileName, boolean isCustomMail) {
        try {
            Session session = getSession();
            MimeMessage message = new MimeMessage(session);
            MimeMultipart content = new MimeMultipart("related");
            message.setFrom(new InternetAddress(System.getProperty(AppKeys.EMAIL_USERNAME)));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(recipientMail));
            message.setSubject(System.getProperty(AppKeys.APP_MAIL_SUBJECT));
            StringBuilder mailBody = null;
            if (isCustomMail) {
                mailBody = FileUtils.readFileAsStringBuilder("./MailMessage.html");
            }
            else {
                mailBody = new StringBuilder("Thank you for registering, please show the mail during the entry of the event! ");
            }
            String cid = StringUtils.getRandomString();
            int qrIndex = mailBody.indexOf(QRSECTION);
            boolean addQr = true;
            if(qrIndex < 0) {
                addQr = false;
            }
            if(addQr) {
                mailBody.replace(qrIndex, qrIndex + QRSECTION.length(), "<br><h3>Please keep the QR confidential: </h3><br><img src=\"cid:" + cid + "\">\n");
            }
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailBody.toString(), "text/html");
            content.addBodyPart(mimeBodyPart);

            if(addQr) {
                if(!qrFileName.contains(".png")) {
                    qrFileName += ".png";
                }
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.attachFile(QR_BASE_LOCATION + qrFileName);
                imagePart.setContentID("<" + cid + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                content.addBodyPart(imagePart);
            }
            message.setContent(content);
            Transport.send(message);
            return true;
        }
        catch (Exception ex) {
            LoggingUtils.log(Level.SEVERE, "Error while sending mail. Please turn on access for less secure apps while doing this operation. You can turn this option once you're done.");
            return false;
        }

    }

}
