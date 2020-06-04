/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package webhookDialogFlow;

import com.google.actions.api.*;
import com.google.actions.api.response.ResponseBuilder;
import com.google.api.services.gmail.model.Message;
import gmail.GMailSearcher;
import gmail.GMailSender;
import gmail.GMailUtils;
import gmail.Templates;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class IntentsHandler extends DialogflowApp {

  @ForIntent("DeleteMail")
  public ActionResponse handleDeleteMailIntent(ActionRequest request) throws IOException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    System.out.println("DeleteMail");
    String queryLabel = (String) request.getParameter("query");
    String value = (String) request.getParameter("any");
    System.out.println("Query " + queryLabel + " Value: " + value);
    System.out.println("Rawtext: " + request.getRawText());

    List<Message> messagesIds = GMailSearcher.findMessagesWithQuery(queryLabel + ":" + value);
    GMailUtils.trashEmails(messagesIds.stream().map(Message::getId).collect(Collectors.toList()));

    responseBuilder.add("Es wurden " + messagesIds.size() + " Emails gel\u00f6scht.");
//  responseBuilder.getConversationData().get("")
    return responseBuilder.build();
  }


  @ForIntent("HowManyUnseenMails")
  public ActionResponse handleHowManyUnseenMailsIntent(ActionRequest request) throws IOException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);

   int count = GMailSearcher.countMessagesWithQuery(GMailSearcher.UNREAD_MESSAGES_QUERY);

    responseBuilder.add("Sie haben " + count + " ungelesene Emails");
//  responseBuilder.getConversationData().get("")
    return responseBuilder.build();
  }


  @ForIntent("LastUnseenMail")
  public ActionResponse handleLastUnseenMailIntent(ActionRequest request) throws IOException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);

    List<Message> message = GMailSearcher.getLastEmailsWithQuery(1, GMailSearcher.UNREAD_MESSAGES_QUERY);

    if(message.size() > 0) {
      Message msg = message.get(0);
      Map<String, String> headers = GMailUtils.parseMessageHeadersToMap(msg);
      responseBuilder.add("Sie haben eine Mail von " + headers.get("From") + " mit dem Betreff " + headers.get("Subject"));
      responseBuilder.getConversationData().put("currentMsgId", msg.getId());
    } else {
      responseBuilder.add("Sie haben keine ungelesene Mail");
    }
    return responseBuilder.build();
  }


  @ForIntent("LastUnseenMail - read")
  public ActionResponse handleLastUnseenMail_readIntent(ActionRequest request) throws IOException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    String currentMsgId = (String) responseBuilder.getConversationData().get("currentMsgId");
    Message msg = GMailUtils.getEntireMessageFromID(currentMsgId);
    responseBuilder.add(GMailUtils.getEncodedMessageBody(msg));

    GMailUtils.removeLabelsFromMessage(msg,List.of("UNREAD"));
    return responseBuilder.build();
  }

  @ForIntent("LastUnseenMail - delete")
  public ActionResponse handleLastUnseenMail_deleteIntent(ActionRequest request) throws IOException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    String currentMsgId = (String) responseBuilder.getConversationData().get("currentMsgId");
    GMailUtils.trashEmail(currentMsgId);

    responseBuilder.add("Mail erfolgreich gel\u00f6scht");
    return responseBuilder.build();
  }


  @ForIntent("LastUnseenMail - reply")
  public ActionResponse handleLastUnseenMail_replyIntent(ActionRequest request) throws IOException, MessagingException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    String currentMsgId = (String) responseBuilder.getConversationData().get("currentMsgId");
    Message msg = GMailUtils.getEntireMessageFromID(currentMsgId);
    String templateType = (String) request.getParameter("templatetype");
    switch (templateType) {
      case "K\u00fcndigungstemplate": Templates.buildTerminationTemplate(responseBuilder, msg);break;
      case "Entschuldigungstemplate": Templates.buildApologyTemplate(responseBuilder, msg); break;
      case "Best\u00e4tigungstemplate": Templates.buildConfirmTemplate(responseBuilder, msg); break;
      case "Termintemplate": {
        String date = GMailUtils.dateToString((Date) request.getParameter("date"), "dd.MM.yyyy");
        Templates.buildMeetingTemplate(responseBuilder, date, null, msg);
      } break;
    }

    return responseBuilder.build();
  }


  @ForIntent("ListMails")
  public ActionResponse handleListMailsIntent(ActionRequest request) throws IOException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    Double number = (Double) request.getParameter("number");

    List<Message> message = GMailSearcher.getLastInboxEmails(number.intValue());


    if(message.size() > 0) {
      int counter = 1;
      List<String> mailList = message.stream().map(Message::getId).collect(Collectors.toList());

      String response = "";
      for(Message msg: message) {
        Map<String, String> headers = GMailUtils.parseMessageHeadersToMap(msg);
        response += counter + ". Mail von " + headers.get("From") + " mit dem Betreff " + headers.get("Subject") + "\n";
        counter++;
      }
      responseBuilder.add(response);
      responseBuilder.getConversationData().put("mailList", mailList);
    } else {
      responseBuilder.add("Sie haben keine Mails");
    }
    return responseBuilder.build();
  }


  @ForIntent("ListMails - select")
  public ActionResponse handleListMails_selectIntent(ActionRequest request) throws IOException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    Double number = Math.abs((Double) request.getParameter("number"));
    List<?> mailList = (List<?>) responseBuilder.getConversationData().get("mailList");
    if(mailList != null && mailList.size() >= number) {
      responseBuilder.getConversationData().put("currentMsgId", mailList.get(number.intValue() - 1));
      Message message = GMailUtils.getEntireMessageFromID((String) mailList.get(number.intValue() - 1));

      Map<String, String> headers = GMailUtils.parseMessageHeadersToMap(message);
      responseBuilder.add("Sie haben die Mail von " + headers.get("From") + " mit dem Betreff " + headers.get("Subject") + " ausgew\u00e4hlt");
    } else {
      responseBuilder.add("Sie konnten keine Mail ausw\u00e4hlen");
    }

    return responseBuilder.build();
  }

  @ForIntent("SearchMail")
  public ActionResponse handleSearchMailIntent(ActionRequest request) throws IOException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);

    String queryLabel = (String) request.getParameter("query");
    String value = (String) request.getParameter("any");


    List<Message> messages = GMailSearcher.findMessagesWithQuery(queryLabel + ":" + value);
    messages = GMailUtils.getMessages(messages);


    if(messages.size() > 0) {
      int counter = 1;
      List<String> mailList = messages.stream().map(Message::getId).collect(Collectors.toList());

      String response = "";
      for(Message msg: messages) {
        Map<String, String> headers = GMailUtils.parseMessageHeadersToMap(msg);
        response += counter + ". Mail von " + headers.get("From") + " mit dem Betreff " + headers.get("Subject") + "\n";
        counter++;
      }
      responseBuilder.add(response);
      responseBuilder.getConversationData().put("mailList", mailList);
    } else {
      responseBuilder.add("Sie haben keine Mails unter ihrem Suchbegriff");
    }
    return responseBuilder.build();
  }


  @ForIntent("SendApologyTemplate")
  public ActionResponse handleSendApologyTemplateIntent(ActionRequest request) throws IOException, MessagingException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);

    String email = (String) request.getParameter("email");

    Templates.buildApologyTemplate(responseBuilder, email);
    return responseBuilder.build();
  }


  @ForIntent("SendConfirmTemplate")
  public ActionResponse handleSendConfirmTemplateIntent(ActionRequest request) throws IOException, MessagingException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);

    String email = (String) request.getParameter("email");

  Templates.buildConfirmTemplate(responseBuilder, email);
    return responseBuilder.build();
  }

  @ForIntent("SendMeetingTemplate")
  public ActionResponse handleSendMeetingTemplateIntent(ActionRequest request) throws IOException, MessagingException, ParseException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);

    String email = (String) request.getParameter("email");
    String date = GMailUtils.dateToString(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse((String) request.getParameter("date")) , "dd.MM.yyyy");
    String time = GMailUtils.dateToString(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse((String) request.getParameter("time")) , "HH:mm");

    Templates.buildMeetingTemplate(responseBuilder, email, date, time);
    return responseBuilder.build();
  }

  @ForIntent("SendTerminationTemplate")
  public ActionResponse handleSendTerminationTemplateIntent(ActionRequest request) throws IOException, MessagingException {
    ResponseBuilder responseBuilder = getResponseBuilder(request);

    String email = (String) request.getParameter("email");

    Templates.buildTerminationTemplate(responseBuilder, email);
    return responseBuilder.build();
  }
}
