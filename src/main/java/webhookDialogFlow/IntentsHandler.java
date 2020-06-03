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

import java.io.IOException;


public class IntentsHandler extends DialogflowApp {

  @ForIntent("ListMails")
  public ActionResponse handleIntentName(ActionRequest request) {
    ResponseBuilder responseBuilder = getResponseBuilder(request);
  System.out.println("ListMails");
    String parameter1 = (String) request.getParameter("ParameterName");

    ///... stuff

    //endConversation is optional
    responseBuilder.add("Response Stuff").endConversation();
//    responseBuilder.getConversationData().get("")
    return responseBuilder.build();
  }


  @ForIntent("DeleteMail")
  public ActionResponse handleDeleteMailIntent(ActionRequest request) {
    ResponseBuilder responseBuilder = getResponseBuilder(request);
    System.out.println("DeleteMail");
    String query = (String) request.getParameter("query");
    String value = (String) request.getParameter("any");
    System.out.println("Query " + query + " Value: " + value);
    System.out.println("Rawtext: " + request.getRawText());

    ///... stuff

    //endConversation is optional
    responseBuilder.add("Response Stuff").endConversation();
//    responseBuilder.getConversationData().get("")
    return responseBuilder.build();
  }


}
