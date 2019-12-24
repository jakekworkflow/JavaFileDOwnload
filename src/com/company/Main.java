package com.company;


import
import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import org.json.*;


public class Main {

    public static void main(String[] args) throws IOException {
        String[] sa = getAPIContents("jakekworkflow","WFCGitRepoSync");

        //String s = downloadFile("icd.aef");

    }

    public static String[] getAPIContents(String sGitUser, String sGitRepo) throws IOException, JSONException {
        int BUFFER_SIZE = 1024;
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        // And as before now you can use URL and URLConnection
        URL url = new URL("https://api.github.com/repos/"+sGitUser+"/"+sGitRepo+"/contents");
        URLConnection connection = url.openConnection();
        HttpURLConnection httpCon = (HttpURLConnection) connection;
        System.out.println(httpCon.getResponseCode());

        if(httpCon.getResponseCode()==200)
        {
            InputStream is = connection.getInputStream();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];

            while((nRead = is.read(data, 0, data.length)) != -1)
            {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            String sFullOutput = new String(buffer.toByteArray(), StandardCharsets.UTF_8);

            org.json.JSONObject jo = new JSONObject(sFullOutput);

            is.close();
        }
        return new String[]{"done", "done1"};
    }


    public static String downloadFile(String sFileName) throws IOException
    {
        int BUFFER_SIZE = 1024;
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

// Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

// And as before now you can use URL and URLConnection
        URL url = new URL("https://github.com/jakekworkflow/WFCGitRepoSync/raw/master/" + sFileName);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpCon = (HttpURLConnection) connection;
        System.out.println(httpCon.getResponseCode());

        if(httpCon.getResponseCode()==200)
        {
            InputStream is = connection.getInputStream();
            String disposition = httpCon.getHeaderField("Content-Disposition");
            System.out.println(disposition);



            FileOutputStream outputStream = new FileOutputStream(sFileName);

            int bytesread = -1;
            byte[] buffer = new byte[1024];
            while ((bytesread = is.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, bytesread);
            }

            outputStream.close();
            is.close();
        }
        return "done";
      }
}
