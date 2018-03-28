package ru.roskazna.gisgmp.monitoring.activity;

public class TestSenderEmailSecond {
    public static void main(String[] args) {
        String emails = "stcon@mail.ru ,  stcon45@gmail.com; shahrmt@yandex.ru";
        MailUtility mailUtility = new MailUtility();
        try {
            for (String email: emails.split("[\\s+,\";-]")) { //"[\\s+.,\";-]"
                email.trim();
                if(email.contains("@"))
                    mailUtility.sendMail("mail.megar.ru", 465,
                            "********", "*******",
                            email, "********", "body test", "test send email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
