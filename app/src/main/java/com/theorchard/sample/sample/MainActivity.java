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
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends Activity {
    String[] tracks = {"", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView l = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tracks);
        l.setAdapter(adapter);


    }

    private String makeRequest(String url) {
        String outData = "";
        try {
            URL url_g = new URL(url);
            URLConnection ukr = url_g.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(ukr.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                outData += inputLine;
            }
            in.close();
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        }
        return outData;
    }

    public void clickRefreshButton(View v) {
        TextView tv = (TextView) findViewById(R.id.textView);

        String responseText = makeRequest("http://192.168.31.128/tracks/600353005129");
        JSONObject beautifiedData = responseDecorator(responseText);
        tv.setText(beautifiedData.toString());
    }

    private JSONObject responseDecorator(String responseText) {
        JSONObject newRelease = new JSONObject();
        JSONArray newTracks = new JSONArray();

        try {
            JSONObject jsonObject = new JSONObject(responseText);

            if (jsonObject.has("tracks")) {
                JSONObject tracksObj = (JSONObject) jsonObject.get("tracks");

                Iterator<?> keys = tracksObj.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();

                    JSONObject trackObj = (JSONObject) tracksObj.get(key);
                    newRelease.put("release_name", trackObj.get("release_name"));
                    newRelease.put("upc", trackObj.get("upc"));
                    newRelease.put("artist_name", trackObj.get("artist_name"));
                    newRelease.put("physical_location", 5);

                    JSONObject newTrack = new JSONObject();
                    newTrack.put("track_name", trackObj.get("track_name"));
                    newTrack.put("track_volume", trackObj.get("cd"));
                    newTrack.put("track_number", trackObj.get("track_id"));
                    newTrack.put("unique_track_id", trackObj.get("id"));
                    newTrack.put("track_name", trackObj.get("track_name"));

                    String md5Hash;
                    if (trackObj.get("releaseStatus").equals("in_content")) {
                        md5Hash = new String(Hex.encodeHex(DigestUtils.md5(trackObj.get("upc") + "_" + trackObj.get("cd") + "_" + trackObj.get("track_id"))));
                    } else {
                        md5Hash = new String(Hex.encodeHex(DigestUtils.md5(trackObj.get("upc") + "_" + trackObj.get("id"))));
                    }
                    newTrack.put("md5_hash", md5Hash);

                    newTracks.put(newTrack);
                }
                newRelease.put("tracks", newTracks);
            }
        } catch (org.json.JSONException e) {
            System.out.println(e.getMessage());
        }
        return newRelease;
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
