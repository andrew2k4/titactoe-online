package server.controller;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import utils.ApiClient;
import utils.Url;

import java.io.IOException;


public class SubmitMove {
    public static void execute(Byte c,Byte r, byte turnPlayer) throws IOException, ParseException {


        JSONObject requestBody = new JSONObject();

        requestBody.put("c" , c);
        requestBody.put("r", r);
        requestBody.put("turnPlayer", turnPlayer);

        ApiClient.post(Url.SEND_MOVE , requestBody.toJSONString());
    };
}
