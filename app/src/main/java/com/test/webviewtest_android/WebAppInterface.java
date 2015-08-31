package com.test.webviewtest_android;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by antti.kolio on 31/08/15.
 */
public class WebAppInterface {
    Context mContext;

    private Map<String, WebAppCmd> commands = new HashMap<String, WebAppCmd>();

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void nativelog(String message) {
        System.out.println(message);
    }

    @JavascriptInterface
    public void nativeapp(String message) {
        try {
            JSONObject json = new JSONObject(message);
            //{"callbackId":1,"command":"yesno","arguments":"Hello from JS!"}
            String command = json.getString("command");

            Object callbackId = json.get("callbackId");
            Object arguments = json.get("arguments");

            System.out.println("search for: "+command);
            WebAppCmd webAppCmd = getCommands().get(command);
            if(webAppCmd != null) {
                System.out.println("found cmd");
                webAppCmd.execute(callbackId, arguments);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Map<String, WebAppCmd> getCommands() {
        return commands;
    }
}
