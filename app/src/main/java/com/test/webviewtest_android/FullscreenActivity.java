package com.test.webviewtest_android;

import com.test.webviewtest_android.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        Environment.getDataDirectory();

        final WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebAppInterface webAppInterface = new WebAppInterface(this);
        webAppInterface.getCommands().put("yesno", new WebAppCmd() {
            @Override
            public void execute(final Object callbackId, Object arguments) {
                System.out.println("executing yesno with cbid: "+callbackId+" and arguments: "+arguments);

                AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
                builder
                        .setTitle("From js:")
                        .setMessage(arguments.toString())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("YES PRESSED");
                                webView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String js = "NativeBridge.handleCallback(\"{\\\"callbackId\\\": \\\"" + callbackId+"\\\", \\\"content\\\": \\\"yes\\\"}\");";
                                        System.out.println("js:"+js);
                                        webView.evaluateJavascript(js, null);
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("NO PRESSED");
                                webView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String js = "NativeBridge.handleCallback(\"{\\\"callbackId\\\": \\\"" + callbackId + "\\\", \\\"content\\\": \\\"no\\\"}\");";
                                        System.out.println("js:" + js);
                                        webView.evaluateJavascript(js, null);
                                    }
                                });
                            }
                        })
                        .show();

            }
        });

        webAppInterface.getCommands().put("images", new WebAppCmd() {
            @Override
            public void execute(Object callbackId, Object arguments) {
                startActivity(createImageChooserIntent("get images", FullscreenActivity.this));
            }
        });



        webView.addJavascriptInterface(webAppInterface, "Android");

        webView.loadUrl("file:///android_asset/web/index.html");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public Intent createImageChooserIntent(String title, Context ctx){
        Intent imagePicker = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePicker.setType("image/*");

        Intent chooser = Intent.createChooser(imagePicker,title);

        return chooser;
    }
}
