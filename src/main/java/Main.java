import com.google.api.services.gmail.model.Message;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException {

//        Message lastMessage = GMailSearcher.getLastUnreadEmail();
//        System.out.println("Last Message Body: " + GMailUtils.getEncodedMessageBody(lastMessage));

//        GMailSearcher.getLastEmails(5);


            GMailSearcher.getLastInboxEmails(5).forEach(it -> {
                try {
                    System.out.println(it.toPrettyString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

}


