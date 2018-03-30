package ru.roskazna.gisgmp.monitoring.activity;


import com.bssys.gisgmp.configuration.SystemSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.UnsupportedEncodingException;

@Stateless(mappedName = "EmailSenderBean")
public class EmailSenderBean implements EmailSenderInterface {
    private static  final String SUBJECT_EMAILS = "gisgmp.monitoring.activity.email.subject";
    private static  final String USERS_EMAILS =  "gisgmp.monitoring.activity.mail.users";
    private static final Logger log = LoggerFactory.getLogger(EmailSenderBean.class);
    private static final long serialVersionUID = 3966807367110330202L;
    private static final String jndiName = "EmailSenderBean";
    private String urn;
    @EJB(mappedName = "ejb.SystemSettings", beanInterface = com.bssys.gisgmp.configuration.SystemSettings.class)
    private SystemSettings systemSettings;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void getSender(String text) {
        log.info("actived getSender()");


        log.info("getting text for send " + text);
        log.info("systemSettings " + systemSettings);
        try {
            String txt = new String(text.getBytes("UTF-8"), "UTF-8");
            log.info("getting text and encoding UTF-8 " + txt);
            setMessage(txt);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        log.info("getMessage() " + getMessage());
        try {
            String emails = systemSettings.getProperty(USERS_EMAILS);
            log.info("systemSettings.getProperty(USERS_EMAILS) " + emails);
            for (final String email : emails.split("[\\s+,\";-]")) { //"[\\s+.,\";-]"
                email.trim();
                if (email.contains("@")) {
                    Thread thread = new Thread(new Runnable() {
                        MailUtility mailUtility = null;
                        @Override
                        public void run() {
                            try {
                                log.info("send text on email" + email);
                                mailUtility = new MailUtility();
                                //String msg = new String(message.getBytes("UTF-8"),
                                //        "UTF-8");
                                log.info("getting text " + getMessage());
                                mailUtility.setText(getMessage());
                                mailUtility.setSubject(systemSettings.getProperty(SUBJECT_EMAILS));

                                mailUtility.setEmail(email);
                                mailUtility.sendMailWithEnvValues();

                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        }
                    });
                    thread.start();

                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}



