package ru.roskazna.gisgmp.monitoring.activity;


import com.bssys.gisgmp.configuration.SystemSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "EmailSenderBean")
public class EmailSenderBean implements EmailSenderInterface {
    private static  final String SUBJECT_EMAILS = "gisgmp.monitoring.activity.email.subject";
    private static  final String USERS_EMAILS =  "gisgmp.monitoring.activity.mail.users";
    private static final Logger log = LoggerFactory.getLogger(EmailSenderBean.class);
    private static final long serialVersionUID = 3966807367110330202L;
    private static final String jndiName = "EmailSenderBean";
    private String urn;
    @EJB(mappedName = "ejb/SystemSettings", beanInterface = SystemSettings.class)
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

        setMessage(text);
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
     /*try {
            log.info("getSender() init");
            Context ctx = new InitialContext();
            AlertOfEvent alertOfEvent = (AlertOfEvent) ctx
                    .lookup("AlertOfEventBean#ru.megar.dispatcher.AlertOfEvent");
            alertOfEvent.setUrn("face");
            Context ctx1 = new InitialContext();
            EmailSenderInterface emailSenderInterface = (EmailSenderInterface) ctx1
                    .lookup("AlertOfEventBean#ru.megar.dispatcher.AlertOfEvent");
        } catch (NamingException e) {
            log.error("NamingException getSender() " + e.getMessage());

        } catch (RemoteException e){
            log.error("RemoteException getSender() " + e.getMessage());
        }
        final String fromEmail = "alert@megar.ru"; //requires valid gmail id
        final String password = "MegarNew#987"; // correct password for gmail id
        //final String toEmail = "stcon@mail.ru"; // can be any email id

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.megar.ru"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.ssl.trust", "mail.megar.ru");
        log.info("props " + props);

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);

            }
        };
        log.info("fromEmail, password" + fromEmail + " " +  password + auth);
        Session session = Session.getInstance(props, auth);
        log.info(session);
        try {
            EmailSenderUtils.sendAlert(session,  alertOfEvent.getUrn()/*alertOfEvent.getUrn() /*+ " " + name + " " + competence + " "
        + " " + date + " " + type + " " + rule + " " + thresh + " " + critical + " " + fact*///);
       /* try {

        }catch (RemoteException e) {

        }*/
        /*} catch (RemoteException e) {
            log.error(e.getMessage());
        }

    }*/



