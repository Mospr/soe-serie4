import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;

public class GMailSender {

    public static void replyToEmail(Message messageToReply,String replyTextMessage) throws IOException, MessagingException {
        MimeMessage email;

        Map<String, String> headersMap = GMailUtils.parseMessageHeadersToMap(messageToReply);

        //create reply email by reversing "From" and "To", adding the replyTextMessage and set the same MessageID
        email = GMailUtils.createEmail(headersMap.get("From"), headersMap.get("To"), headersMap.get("Subject"),
                replyTextMessage,
                headersMap.get("Message-ID"),headersMap.get("Message-ID")
        );

        Message newMessage = GMailUtils.createMessageWithEmail(email);

        newMessage.setThreadId(messageToReply.getThreadId());

        GMailServiceSingleTon.getInstance().users().messages().send(Constants.currentUserID,newMessage).execute();
    }



    public static void sendEmail(String to, String subject, String messageBody) throws IOException, MessagingException {
        MimeMessage email;

        //create reply email by reversing "From" and "To", adding the messageBody and set the same MessageID
        email = GMailUtils.createEmail("FROM" , to , subject,
                messageBody);

        Message newMessage = GMailUtils.createMessageWithEmail(email);

        GMailServiceSingleTon.getInstance().users().messages().send(Constants.currentUserID,newMessage).execute();
    }

}
