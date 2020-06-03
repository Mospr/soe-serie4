import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.ModifyMessageRequest;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class GMailUtils {

    public static MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText, String references, String inReplyTo)
            throws MessagingException {
        MimeMessage email = createEmail(to,from,subject,bodyText);
        email.setHeader("References", references);
        email.setHeader("In-Reply-To", inReplyTo);
        return email;
    }
    public static MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }



    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    public static Message createMessageWithEmail(MimeMessage emailContent) throws IOException, MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = new String(Base64.getUrlEncoder().encode(bytes));
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }


    public static Map<String, String> parseMessageHeadersToMap(Message m){
        List<MessagePartHeader> headers = m.getPayload().getHeaders();

        Map<String, String> headersHashMap = new HashMap<>();
        for (MessagePartHeader header: headers) {
            headersHashMap.put(header.getName(),header.getValue());
        }

        return headersHashMap;
    }


    public static String getEncodedMessageBody(Message m){
        if(m == null || m.getPayload() == null) return null;

        String content = m.getPayload().getParts().stream()
                .filter(messagePart -> "text/plain".equals(messagePart.getMimeType()))
                .map(messagePart -> messagePart.getBody().getData())
                .collect(Collectors.toList()).get(0);

      return new String(Base64.getUrlDecoder().decode(content), StandardCharsets.UTF_8);
    }


  public static Message getEntireMessageFromID(String messageId) throws IOException {
        Gmail service = GMailServiceSingleTon.getInstance();

        return service.users().messages().get(Constants.currentUserID,messageId).execute();
  }


  public static void addLabelsToMessage(Message message, List<String> labelsToAdd) throws IOException {
      Gmail service = GMailServiceSingleTon.getInstance();

      service.users().messages().modify(Constants.currentUserID,message.getId(),
              new ModifyMessageRequest().setAddLabelIds(labelsToAdd)).execute();
  }

  public static void removeLabelsFromMessage(Message message, List<String> labelsToRemove) throws IOException {
        Gmail service = GMailServiceSingleTon.getInstance();

        service.users().messages().modify(Constants.currentUserID,message.getId(),
                new ModifyMessageRequest().setRemoveLabelIds(labelsToRemove)).execute();
    }


    public static void trashEmail(String messageId){
        trashEmails(List.of(messageId));
    }


    public static void trashEmails(List<String> messagesIds) {
        Gmail service = GMailServiceSingleTon.getInstance();
        if(messagesIds == null) return;

        messagesIds.forEach(it -> {
            try {
                service.users().messages().trash(Constants.currentUserID,it);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
