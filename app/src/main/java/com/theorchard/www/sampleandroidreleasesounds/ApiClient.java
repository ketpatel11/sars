package com.theorchard.www.sampleandroidreleasesounds;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient extends Service {
    private String data;

    public String getData() {
        return this.data;
    }

    private void setData(String content) {
        this.data = content;
    }

    public ApiClient(String urlString) {
        HttpURLConnection urlConnection;

        try {
            URL url = new URL("http://192.168.31.128/streams/600353005129/5");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            try {
                this.setData(this.readStream(in));
            } catch (Exception e) {
                in.close();
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            // do something
        }
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

