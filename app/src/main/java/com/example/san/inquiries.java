package com.example.san;



import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class inquiries extends AppCompatActivity {
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView  textView2;
    @BindView(R.id.textView3)
    TextView  textView3;
    @BindView(R.id.textView4)
    TextView  textView4;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.mail)
    EditText mail;
    @BindView(R.id.que)
    EditText que;
    @BindView(R.id.buttonSub)
    Button buttonSub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiries);
        ButterKnife.bind(this);


        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnClick(R.id.buttonSub)
            public void onClick(View view) {
                String id = name.getText().toString();
                String num=number.getText().toString();
                String email=mail.getText().toString();
                String question=que.getText().toString();
                new UploadDataAsyncTask().execute(id, num,email, question);


            }
        });
    }

    private static class UploadDataAsyncTask extends AsyncTask<String, Void, Void> {
        /**
         * sends data to a web server using an HTTP GET request in an asynchronous manner
         * @param params The parameters of the task.
         *
         * @return
         */

        @Override
        protected Void doInBackground(String... params) {
            try {
                // Construct the URL with the required parameters
                String urlParameters = "name=" + URLEncoder.encode(params[0], "UTF-8")
                        + "&number=" + URLEncoder.encode(params[1], "UTF-8")
                        + "&email=" + URLEncoder.encode(params[2], "UTF-8")
                        + "&question=" + URLEncoder.encode(params[3], "UTF-8");


                URL url = new URL("http://sanbot.free.fr/ise/adddata.php?name=%s&number=%s&email=%s&question=%s" + urlParameters);

                // Open the connection and send the data
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    throw new IOException("HTTP error code " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}