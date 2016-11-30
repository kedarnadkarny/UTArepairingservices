package edu.uta.utarepairingservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;

public class ViewAppointment extends AppCompatActivity {

    TextView txtTitle, txtDescription, txtPosted, txtStatus;
    Button btnCancel, btnRate;
    UserInfo ui;
    int requestID;
    InputStream is=null;
    String line=null;
    String result=null;
    String s;
    String title, description, status, datetime, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtPosted = (TextView) findViewById(R.id.txtPosted);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnRate = (Button) findViewById(R.id.btnRate);
        btnRate.setVisibility(View.GONE);
        s = getIntent().getStringExtra("view");
        ui = new UserInfo();
        requestID = ui.getRequestID();

        getData();
        txtTitle.setText(title);
        txtDescription.setText(description);
        txtStatus.setText(status);
        txtPosted.setText(datetime);

        if (status.equals("Accept") || status.equals("Cancelled") || status.equals("Reject")) {
            //btnCancel.setVisibility(View.GONE);
        }
        else if(status.equals("Complete")) {
            //btnRate.setVisibility(View.VISIBLE);
            //btnCancel.setVisibility(View.GONE);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAppointment();
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewAppointment.this, ProvideRatingActivity.class);
                i.putExtra("fullname", fullname);
                startActivity(i);
            }
        });

    }

    public void cancelAppointment() {
        try {
            URL url = new URL("http://kedarnadkarny.com/utarepair/cancel_appointment.php");
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = URLEncoder.encode("request_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(requestID), "UTF-8");
            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            is=new BufferedInputStream(con.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();

            while((line=br.readLine())!=null){
                sb.append(line);

            }
            is.close();
            result=sb.toString();
            if(result.equals("UPDATED")) {
                btnCancel.setVisibility(View.GONE);
                txtStatus.setText("Cancelled!");
                Toast.makeText(getBaseContext(),"Appointment Cancelled!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    public void getData() {
        try {
            URL url = new URL("http://kedarnadkarny.com/utarepair/single_appointment.php");
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = "";

            data_string = URLEncoder.encode("request_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(requestID), "UTF-8");


            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            is=new BufferedInputStream(con.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();

            while((line=br.readLine())!=null){
                sb.append(line +"\n");

            }
            is.close();
            result=sb.toString();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        //parse json data
        try{
            JSONObject jo = new JSONObject(result);
            int length = jo.length();
            if(s.equals("view_cu")) {
            int reqID=0;
                for(int i=0;i<length;i++){
                    reqID = Integer.parseInt(jo.getString("request_id"));
                    title = jo.getString("title");
                    description = jo.getString("description");
                    status = jo.getString("status");
                    datetime = jo.getString("datetime");
                    fullname = jo.getString("firstname") + jo.getString("lastname");
                }
                Log.d("response", reqID + " " + title + " " + description + " " + status + " " + datetime);
            }
            else if(s.equals("view_sp")) {
                for(int i=0;i<length;i++){
                    title = jo.getString("title");
                    description = jo.getString("description");
                    status = jo.getString("status");
                    datetime = jo.getString("datetime");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
