package com.theorchard.sample.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;


public class MainActivity extends Activity {
    private ListView l;
    private CheckBox check1;
    private Button button1;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    String[] days = {"", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        l = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days);
        l.setAdapter(adapter);


    }

    private String makeRequest(String url) {
        String outdata = "";
        try {
            URL url_g = new URL(url);
            URLConnection ukr = url_g.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(ukr.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                outdata += inputLine;
            in.close();
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        }
        return outdata;
    }


    public void addListenerOnButton() {
        button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*StringBuffer result = new StringBuffer();
                        result.append("Dog: ").append(check1.isChecked());
                        Toast.makeText(MainActivity.this, result.toString(),
                                Toast.LENGTH_LONG).show();*/
                        TextView tv = (TextView) findViewById(R.id.textView);
                        String responseText = makeRequest("http://192.168.31.128/streams/600353005129/5");
                        tv.setText(responseText);
                    }
                }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
