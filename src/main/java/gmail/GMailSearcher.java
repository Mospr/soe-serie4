package gmail;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GMailSearcher {
    public static final String UNREAD_MESSAGES_QUERY = "label:unread";
    public static final String INBOX_MESSAGES_QUERY = "in:inbox";


    public static Message getLastUnreadEmail() throws IOException {
        List<Message> unreadMessages = findMessagesWithQuery(UNREAD_MESSAGES_QUERY);
        //check if unread messages exist and return last unread message
        Message messageID = unreadMessages != null && unreadMessages.size() > 0 ? unreadMessages.get(0) : null;

        return messageID != null ? GMailUtils.getEntireMessageFromID(messageID.getId()) : null;
    }


    public static List<Message> findMessagesWithQuery(String query) throws IOException {

        Gmail service = GMailServiceSingleTon.getInstance();

        ListMessagesResponse emailsPreview = service.users().messages().list(Constants.currentUserID).setQ(query).execute();


        //List of Messages  only with MessageID and ThreadID
        return emailsPreview.getMessages();
    }

    public static List<Message> getLastEmailsWithQuery(int numberOfEmails, String query) throws IOException {
        List<Message> m = findMessagesWithQuery(query);
        List<Message> messageIDs = m != null && m.size() > 0 ? m.subList(0, numberOfEmails) : null;

        return GMailUtils.getMessages(messageIDs);
    }
    public static List<Message> getLastInboxEmails(int numberOfEmails) throws IOException {
        return getLastEmailsWithQuery(numberOfEmails,INBOX_MESSAGES_QUERY);
    }

    public static int countMessagesWithQuery(String query) throws IOException {
        return findMessagesWithQuery(query).size();
    }

}
