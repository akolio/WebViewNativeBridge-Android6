package com.test.webviewtest_android;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by antti.kolio on 31/08/15.
 */
public class WebAppInterface {
    Context mContext;

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
            Object command = json.get("command");
            System.out.println("command: "+command);

            Object callbackId = json.get("callbackId");
            System.out.println("cback id: "+callbackId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
