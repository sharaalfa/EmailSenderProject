package ru.roskazna.gisgmp.monitoring.activity;

public class TestSenderEmailFour {
    public static void main(String[] args) {
        String emails = "stcon@mail.ru,stcon45@gmail.com;shahrmt@yandex.ru";
        MailUtility mailUtility = new MailUtility();
        try {
            for (String email: emails.split("[\\s+,\";-]")) { //"[\\s+.,\";-]"
                mailUtility.sendMail("mail.megar.ru", 465,
                        "********", "********",
                        email, "********", new String("Привет".getBytes("KOI8-R")), "test send email");
                System.out.println(email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
