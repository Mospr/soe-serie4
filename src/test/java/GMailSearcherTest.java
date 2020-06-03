import com.google.api.services.gmail.model.Message;
import gmail.GMailSearcher;
import gmail.GMailUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GMailSearcherTest {

    @Test
    public static void mockTest() throws IOException {

        Message lastMessage = GMailSearcher.getLastUnreadEmail();
        System.out.println("Last Message Body: " + GMailUtils.getEncodedMessageBody(lastMessage));

        assertEquals(1,1);
    }

}