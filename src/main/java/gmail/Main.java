package gmail;

import gmail.GMailSearcher;

import java.io.IOException;

public class Main {

    public static void main(String... args) throws IOException {

//        Message lastMessage = gmail.GMailSearcher.getLastUnreadEmail();
//        System.out.println("Last Message Body: " + gmail.GMailUtils.getEncodedMessageBody(lastMessage));

//        gmail.GMailSearcher.getLastEmails(5);


            GMailSearcher.getLastInboxEmails(5).forEach(it -> {
                try {
                    System.out.println(it.toPrettyString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }

}


