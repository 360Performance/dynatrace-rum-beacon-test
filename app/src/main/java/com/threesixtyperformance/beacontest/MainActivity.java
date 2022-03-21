package com.threesixtyperformance.beacontest;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dynatrace.android.agent.DTXAction;
import com.dynatrace.android.agent.Dynatrace;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /** Called when the user taps the Send button */
    public void createUserAction(View view) throws IOException, JSONException {

        EditText userActionName = (EditText) findViewById(R.id.userActionName);

        DTXAction action = Dynatrace.enterAction(String.valueOf(userActionName.getText()));

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://icanhazdadjoke.com/")
                .header("Accept", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject responseJson = new JSONObject(response.body().string());

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.responseText);
        textView.setText(responseJson.getString("joke"));

        action.leaveAction();

    }
}