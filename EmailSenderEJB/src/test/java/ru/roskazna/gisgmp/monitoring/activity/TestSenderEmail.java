package ru.roskazna.gisgmp.monitoring.activity;

public class TestSenderEmail {
    public static void main(String[] args) {
        String emails = "stcon@mail.ru,stcon45@gmail.com;shahrmt@yandex.ru";
        MailUtility mailUtility = new MailUtility();
        try {
            for (String email: emails.split("[\\s+,\";-]")) { //"[\\s+.,\";-]"
                mailUtility.sendMail("mail.megar.ru", 465,
                        "********", "*******",
                        email, "********", "body test", "test send email");
                System.out.println(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}