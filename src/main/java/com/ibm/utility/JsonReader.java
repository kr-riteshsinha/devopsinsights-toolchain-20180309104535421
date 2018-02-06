package com.ibm.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class JsonReader {

	public static void main(String[] args) throws FileNotFoundException, IOException {
//		JSONParser parser = new JSONParser();
//
//        try {
//
//            Object obj = parser.parse(new FileReader("C:\\Users\\IBM_ADMIN\\git\\orchestration\\src\\main\\resources\\sttResponse_short.json"));
//
//            JSONObject jsonObject = (JSONObject) obj;
//            System.out.println(jsonObject);
//
//            String name = (String) jsonObject.get("name");
//            System.out.println(name);
//
//            long age = (Long) jsonObject.get("age");
//            System.out.println(age);
//
//            // loop array
//            JSONArray msg = (JSONArray) jsonObject.get("messages");
//            Iterator<String> iterator = msg.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//	}
		String speechrestText = "{  \"result_index\": 1,   \"results\": [ {       \"final\": false,       \"alternatives\": [         {           \"timestamps\": [             [               \"hi\",               6.83,               7.0             ],             [               \"yes\",               7.0,               7.37             ],             [               \"I\",               7.37,               7.48             ],             [               \"was\",               7.48,               7.76             ],             [               \"trying\",               7.76,               8.11             ],             [               \"to\",               8.16,               8.3             ]           ],           \"transcript\": \"hi yes I was trying to \"         }       ]     }   ] }"		;
		
		ObjectMapper mapper  = new ObjectMapper();
		 JsonFactory jsonFactory = new JsonFactory();
	     Iterator<SpeechResults> value = mapper.readValues( jsonFactory.createParser(speechrestText), SpeechResults.class);
	     value.forEachRemaining((u)->{System.out.println(u);});
	}
}
