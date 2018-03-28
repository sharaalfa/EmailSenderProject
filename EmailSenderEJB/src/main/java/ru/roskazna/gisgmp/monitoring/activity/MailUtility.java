package ru.roskazna.gisgmp.monitoring.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MailUtility {


   private static final Logger log = LoggerFactory.getLogger(MailUtility.class);
    private String text;
    private String subject;
    private String email;
    private static String smtpHost = "mail.megar.ru";
    private static int smtpPort = 587;
    private static String defaultUser = "********";
    private static String defaultPassword  = "******";
    static {
        if( System.getenv("SMTP_HOST") != null )
            smtpHost = System.getenv("SMTP_HOST");
        if( System.getenv("SMTP_PORT") != null )
            smtpPort = Integer.valueOf( System.getenv("SMTP_PORT") );
        if( System.getenv("SMTP_AUTH_USER") != null )
            defaultUser = System.getenv("SMTP_AUTH_USER");
        if( System.getenv("SMTP_AUTH_PWD") != null )
            defaultPassword = System.getenv("SMTP_AUTH_PWD");
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void sendMail(String smtpHost, int smtpPort, String smtpAuthUser, String smtpAuthPwd, String to, String from, String body, String subject) throws Exception{
        log.info("push text for body and subject");
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");

        if( smtpPort == 465 )
        {
            props.put("mail.smtp.socketFactory.port", smtpPort);
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
        }
        else props.put("mail.smtp.port", smtpPort);


        Authenticator auth = new SMTPAuthenticator( smtpAuthUser, smtpAuthPwd );
        Session mailSession = Session.getInstance(props, auth);

        // uncomment for debugging infos to stdout
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);

        message.setText(body);
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(to));

        transport.connect();
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();

    }



    public  void sendMailWithEnvValues() throws Exception{
        log.info("push text for body and subject sendMailWithValues");
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpHost);

        if(smtpPort == 465)
        {
            props.put("mail.smtp.socketFactory.port", smtpPort);
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
        }
        else props.put("mail.smtp.port", smtpPort);

        props.put("mail.smtp.auth", "true");

        Authenticator auth = new SMTPAuthenticator( defaultUser, defaultPassword );
        Session mailSession = Session.getInstance(props, auth);
        // uncomment for debugging infos to stdout
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);

        message.setText(getText());
        message.setFrom(new InternetAddress("********"));
        message.setSubject(getSubject());
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(getEmail()));


        transport.connect();
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    private class SMTPAuthenticator extends Authenticator {

        private String username;
        private String password;

        public SMTPAuthenticator(String username, String password)
        {
            this.username = username;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
}