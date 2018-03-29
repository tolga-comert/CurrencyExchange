package com.example.helm.hw3;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceUtil {

    public static String getRequest(String urlStr) {
        URL url;
        HttpURLConnection conn=null;
        InputStream inputStream = null;
        InputStreamReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); //maximum time to wait for an input stream read
            conn.setConnectTimeout(15000); //maximum time to wait while connecting
            conn.setRequestMethod("GET");
            conn.setDoInput(true); //whether this URLConnection allows receiving data
            conn.connect();

            inputStream = conn.getInputStream();
            reader = new InputStreamReader(inputStream);

            int data = reader.read();
            while(data!=-1){
                response.append( (char)data );
                data = reader.read();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{

            try {
                if(reader!=null) reader.close();
                if(inputStream!=null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(conn!=null) conn.disconnect();

        }

        return response.toString();
    }

    public static String postRequest(String urlStr, String params) {
        URL url;
        HttpURLConnection conn=null;
        InputStream inputStream = null;
        InputStreamReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); //maximum time to wait for an input stream read
            conn.setConnectTimeout(15000); //maximum time to wait while connecting
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true); //whether this URLConnection allows receiving data

            //-- send parameters
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osr = new OutputStreamWriter(os, "UTF-8");
            osr.write(params);
            osr.flush();
            osr.close();
            os.close();

            conn.connect();

            //-- Read response
            inputStream = conn.getInputStream();
            reader = new InputStreamReader(inputStream);
            int data = reader.read();
            while(data!=-1){
                response.append( (char)data );
                data = reader.read();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{

            try {
                if(reader!=null) reader.close();
                if(inputStream!=null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(conn!=null) conn.disconnect();

        }

        return response.toString();
    }


}
