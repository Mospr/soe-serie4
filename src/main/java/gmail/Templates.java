package gmail;

import com.google.actions.api.response.ResponseBuilder;
import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import java.io.IOException;

public class Templates {

    public static void buildMeetingTemplate(ResponseBuilder responseBuilder, String email, String date, String time) throws IOException, MessagingException {
        String body = "";
        if(time!= null && time.length() > 0) {
            body = "Sehr geehrte Damen und Herren,\nich w\u00fcrde mich freuen, wenn Sie am " + date + " um " + time + " Uhr Zeit f\u00fcr mich h\u00e4tten\n"
                    + "Mit freundlichen Gr\u00fc\u00dfen";
        } else {
            if(date != null && date.length() >0 ) {
                body = "Sehr geehrte Damen und Herren,\nich w\u00fcrde mich freuen, wenn Sie am " + date + " Zeit f\u00fcr mich h\u00e4tten\n"
                        + "Mit freundlichen Gr\u00fc\u00dfen";
            } else {
                body = "Sehr geehrte Damen und Herren,\nk\u00f6nnten Sie mir bitte einen Terminvorschlag senden.\n"
                        + "Mit freundlichen Gr\u00fc\u00dfen";
            }
        }
        GMailSender.sendEmail(email, "Terminanfrage", body);

        responseBuilder.add("Sie haben eine Anfrage an " + email + " erfolgreich gesendet.");
    }

    public static void buildMeetingTemplate(ResponseBuilder responseBuilder, String date, String time, Message msg) throws IOException, MessagingException {
        String body = "";
        if(time!= null && time.length() > 0) {
            body = "Sehr geehrte Damen und Herren,\nich w\u00fcrde mich freuen, wenn Sie am " + date + " um " + time + " Uhr Zeit f\u00fcr mich h\u00e4tten\n"
                    + "Mit freundlichen Gr\u00fc\u00dfen";
        } else {
            if(date != null && date.length() >0 ) {
                body = "Sehr geehrte Damen und Herren,\nich w\u00fcrde mich freuen, wenn Sie am " + date + " Zeit f\u00fcr mich h\u00e4tten\n"
                        + "Mit freundlichen Gr\u00fc\u00dfen";
            } else {
                body = "Sehr geehrte Damen und Herren,\nk\u00f6nnten Sie mir bitte einen Terminvorschlag senden.\n"
                        + "Mit freundlichen Gr\u00fc\u00dfen";
            }
        }
        GMailSender.replyToEmail(msg,  body);

        responseBuilder.add("Sie haben  erfolgreich um einen Termin gebeten.");
    }

    public static void buildTerminationTemplate(ResponseBuilder responseBuilder, String email) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nhiermit teile ich Ihnen mit, dass ich zum n\u00e4chstm\u00f6gliche Zeitpunkt k\u00fcndige"
                + "Mit freundlichen Gr\u00fc\u00dfen";
        GMailSender.sendEmail(email, "K\u00fcndigung", body);

        responseBuilder.add("Sie haben die K\u00fcndigung erfolgreich an  " + email + "  gesendet.");
    }

    public static void buildTerminationTemplate(ResponseBuilder responseBuilder, Message msg) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nhiermit teile ich Ihnen mit, dass ich zum n\u00e4chstm\u00f6gliche Zeitpunkt k\u00fcndige"
                + "Mit freundlichen Gr\u00fc\u00dfen";
        GMailSender.replyToEmail(msg, body);

        responseBuilder.add("Sie haben erfolgreich um eine K\u00fcndigung gefordert.");
    }

    public static void buildConfirmTemplate(ResponseBuilder responseBuilder, String email) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nich freue mich ungemein Ihnen mitteilen zu k\u00f6nnen,"
                + " dass ich Ihre Anfrage best\u00e4tige.\n"
                + "Mit freundlichen Gr\u00fc\u00dfen";
        GMailSender.sendEmail(email, "Best\u00e4tigung", body);

        responseBuilder.add("Sie haben die Anfrage von " + email + " erfolgreich best\u00e4tigt.");
    }

    public static void buildConfirmTemplate(ResponseBuilder responseBuilder, Message msg) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nich freue mich ungemein Ihnen mitteilen zu k\u00f6nnen,"
                + " dass ich Ihre Anfrage best\u00e4tige.\n"
                + "Mit freundlichen Gr\u00fc\u00dfen";
        GMailSender.replyToEmail(msg,  body);

        responseBuilder.add("Sie haben die Anfrage erfolgreich best\u00e4tigt.");
    }

    public static void buildApologyTemplate(ResponseBuilder responseBuilder, String email) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nich m\u00f6chte mich von Herzen pers\u00f6nlich bei Ihnen entschuldigen."
                + " Mein Team und ich arbeiten mit Hochdruck Ihr Problem zu l\u00f6sen.\n"
                + "Mit freundlichen Gr\u00fc\u00dfen";
        GMailSender.sendEmail(email, "Entschuldigung", body);

        responseBuilder.add("Sie haben sich erfolgreich bei " + email + " entschuldigt");
    }

    public static void buildApologyTemplate(ResponseBuilder responseBuilder, Message msg) throws IOException, MessagingException {
        String body = "Sehr geehrte Damen und Herren,\nich m\u00f6chte mich von Herzen pers\u00f6nlich bei Ihnen entschuldigen."
                + " Mein Team und ich arbeiten mit Hochdruck Ihr Problem zu l\u00f6sen.\n"
                + "Mit freundlichen Gr\u00fc\u00dfen";
        GMailSender.replyToEmail(msg, body);

        responseBuilder.add("Sie haben sich erfolgreich entschuldigt");
    }

}
