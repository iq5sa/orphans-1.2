package net.alkafeel.gmushriq;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class Utiles {

    static public String Cnd(String File){

        return "http://192.168.0.102/" + File;

    }

    static public String Udid(Context context){

       return Settings.Secure.getString(context.getContentResolver(),
               Settings.Secure.ANDROID_ID);

    }
    
    static public SharedPreferences Settings(Context cont){
        SharedPreferences sharedpreferences = cont.getSharedPreferences("private_settings", Context.MODE_PRIVATE);
        return sharedpreferences;
    }

    static public String POST(String ApiKey , String Data){

        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url  = new URL("http://aljiachi.net/apps/orphans/apiDirect/" + ApiKey);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(Data.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (Data);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

    }

    static public String GET(String ApiKey){

        HttpURLConnection urlConnection = null;

        URL url = null;
        try {
            url = new URL("http://aljiachi.net/apps/orphans/apiDirect/" + ApiKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        try {
            urlConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br= null;
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonString = sb.toString();


        return jsonString;

    }
}
