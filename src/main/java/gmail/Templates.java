package gmail;

import com.google.actions.api.response.ResponseBuilder;

import javax.mail.MessagingException;
import java.io.IOException;

public class Templates {

    public static void buildMeetingTemplate(ResponseBuilder responseBuilder, String email, String date, String time) throws IOException, MessagingException {
        String body = "";
        if(time!= null && time.length() > 0) {
            body = "Sehr geehrte Damen und Herren,\nich würde mich freuen, wenn Sie am " + date + " um " + time + " Uhr Zeit für mich hätten\n"
                    + "Mit freundlichen Grüßen";
        } else {
            if(date != null && date.length() >0 ) {
                body = "Sehr geehrte Damen und Herren,\nich würde mich freuen, wenn Sie am " + date + " Zeit für mich hätten\n"
                        + "Mit freundlichen Grüßen";
            } else {
                body = "Sehr geehrte Damen und Herren,\nkönnten Sie mir bitte einen Terminvorschlag senden.\n"
                        + "Mit freundlichen Grüßen";
            }
        }
        GMailSender.sendEmail(email, "Terminanfrage", body);

        responseBuilder.add("Sie haben eine Anfrage an " + email + " erfolgreich gesendet.");
    }

    public static void buildTerminationTemplate(ResponseBuilder responseBuilder, String email) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nhiermit teile ich Ihnen mit, dass ich zum nächstmögliche Zeitpunkt kündige"
                + "Mit freundlichen Grüßen";
        GMailSender.sendEmail(email, "Kündigung", body);

        responseBuilder.add("Sie haben die Kündigung erfolgreich an  " + email + "  gesendet.");
    }

    public static void buildConfirmTemplate(ResponseBuilder responseBuilder, String email) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nich freue mich ungemein Ihnen mitteilen zu können,"
                + " dass ich Ihre Anfrage bestätige.\n"
                + "Mit freundlichen Grüßen";
        GMailSender.sendEmail(email, "Bestätigung", body);

        responseBuilder.add("Sie haben die Anfrage von " + email + " erfolgreich bestätigt.");
    }

    public static void buildApologyTemplate(ResponseBuilder responseBuilder, String email) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nich möchte mich von Herzen persönlich bei Ihnen entschuldigen."
                + " Mein Team und ich arbeiten mit Hochdruck Ihr Problem zu lösen.\n"
                + "Mit freundlichen Grüßen";
        GMailSender.sendEmail(email, "Entschuldigung", body);

        responseBuilder.add("Sie haben sich erfolgreich bei " + email + " entschuldigt");
    }


}
