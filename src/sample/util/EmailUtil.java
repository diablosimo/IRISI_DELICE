package sample.util;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author HP
 */
public class EmailUtil {

    public static boolean sendMail(String message, String to, String subject, String fileAttachment) throws MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                String from = "irisi.delice@gmail.com";
                String password = "IrisiDelice2020";
                return new PasswordAuthentication(from, password);
            }
        });
        session.setDebug(true);
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message, "UTF-8", "html");
            if (fileAttachment != null) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(message);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                messageBodyPart = new MimeBodyPart();

                DataSource source = new FileDataSource(fileAttachment);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(fileAttachment);
                multipart.addBodyPart(messageBodyPart);
                mimeMessage.setContent(multipart);
            }
            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException me) {
            me.printStackTrace();
        }

        return false;
    }
}
