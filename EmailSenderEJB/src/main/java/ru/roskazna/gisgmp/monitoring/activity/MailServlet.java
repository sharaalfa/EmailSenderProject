package ru.roskazna.gisgmp.monitoring.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MailServlet extends HttpServlet {
    private static  final String SUBJECT_EMAILS = "gisgmp.monitoring.activity.email.subject";
    private static  final String USERS_EMAILS =  "gisgmp.monitoring.activity.mail.users";
    private static final Logger log = LoggerFactory.getLogger(MailUtility.class);
    // @EJB(mappedName = "ejb/SystemSettings", beanInterface =  SystemSettings.class)
    // private SystemSettings systemSettings;

    private static final long serialVersionUID = 1L;
    private String encoding;

    /*protected void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null) encoding = "UTF-8";
    }
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain next)
            throws IOException, ServletException {
        // Respect the client-specified character encoding
        // (see HTTP specification section 3.4.1)
        if (null == request.getCharacterEncoding()) {
            request.setCharacterEncoding(encoding);
        }

        // Set the default response content type and encoding
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        next.doFilter(request, response);
    }*/


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doWork( request, response );
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doWork( request, response );
    }


    protected void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setContentType("text/html; charset=UTF-8");
        //response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        MailUtility simpleMail = new MailUtility();

        //if the user posts the form, then use their params
        if( request.getParameter("smtpHost") != null )
        {
            try {
                simpleMail.sendMail( request.getParameter("smtpHost"),
                        Integer.valueOf( request.getParameter("smtpPort") ),
                        request.getParameter("smtpAuthUser"),
                        request.getParameter("smtpAuthPwd"),
                        request.getParameter("to"),
                        request.getParameter("from"),
                        request.getParameter("body"),
                        request.getParameter("subject")
                );
                writer.print( "test email sent with submitted values" );
            } catch (Exception e) {
                writer.print( "test email failed\n" );
                writer.print(e.getMessage());
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        //otherwise use the default env and hardcoded values
        else
        {
            try {
                simpleMail.sendMailWithEnvValues();
                writer.print( "test email sent with default values" );
            } catch (Exception e) {
                writer.print( "test email failed" );
                writer.print(e.getMessage());
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }

        writer.close();
    }
}
