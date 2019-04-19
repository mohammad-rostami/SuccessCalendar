package ir.co.ahs.app.mob.success;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import ir.co.persiancalendar.core.G;


public class Webservice {
    EditText txt;
    TextView part1;
    TextView part2;
    String data;
    String[] separated;
    private ProgressDialog progress;

    public static class sessionConnection extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String oneSignalUserId = urls[1];
            String oneSignalRegistrationId = urls[2];
            String uniqueId = urls[3];
            String osVersion = urls[4];
            String appVersion = urls[5];
            String deviceName = urls[6];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("platform", G.CONTEXT.getResources().getString(R.string.platform));
                params1.put("oneSignalUserID", oneSignalUserId);
                params1.put("oneSignalRegistrationID", oneSignalRegistrationId);
                params1.put("uniqueID", uniqueId);
                params1.put("osVersion", osVersion);
                params1.put("appVersion", appVersion);
                params1.put("deviceName", deviceName);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);


                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            return post_result;
        }


    }

    public static class postClass extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String name = urls[3];
            String lastName = urls[4];
            String phoneNumber = urls[5];
            String birthDate = urls[6];
            int gender = Integer.parseInt(urls[7]);
            int cityId = Integer.parseInt(urls[8]);
            String email = urls[9];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("firstName", name);
                params1.put("lastName", lastName);
                params1.put("cellPhone", phoneNumber);
                params1.put("birthDate", birthDate);
                params1.put("gender", gender);
                params1.put("cityId", cityId);
                if (email.length() > 1) {
                    params1.put("email", email);
                }

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);

                    RegisterActivity.showToast(response);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class verifyClass extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerId = urls[3];
            String phone = urls[4];
            String code = urls[5];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerId);
                params1.put("cellPhone", phone);
                params1.put("activationCode", code);


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getCustomer extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String phone = urls[3];
            String memberCode = urls[4];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("cellPhone", phone);
                params1.put("activationCode", memberCode);


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class login extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;
        private boolean type;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String phone = urls[3];
            String memberCode = urls[4];
            String loginType = urls[5];
            if (loginType.equals("true")){
                type = true;
            }else {
                type = false;
            }
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("cellPhone", phone);
                params1.put("loginCode", memberCode);
                params1.put("isLoginBySMS", type);


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class resend extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerId = urls[3];
            String cellPhone = urls[4];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerId);
                params1.put("cellPhone", cellPhone);


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getNews extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String date = urls[3];
            String time = urls[4];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("lastLocalDate", date);
                params1.put("lastLocalTime", time);
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getInfo extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class weekSpecificStory extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String id = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("storyGroupId", id);
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getTestGroup extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String type = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("testType", Integer.parseInt(type));
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getWeekGroups extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }



                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getMeditation extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class aboutUs extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getProductGroup extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String latitude = urls[3];
            String longtitude = urls[4];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("locationLat", latitude);
                params1.put("locationLong", longtitude);
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getTargetGroup extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String latitude = urls[3];
            String longtitude = urls[4];
            String year = urls[5];
            String month = urls[6];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("locationLat", latitude);
                params1.put("locationLong", longtitude);
                params1.put("targetYear", year);
                params1.put("targetMonth", month);
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getOmen extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String latitude = urls[3];
            String longtitude = urls[4];
            String year = urls[5];
            String month = urls[6];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("locationLat", latitude);
                params1.put("locationLong", longtitude);
                params1.put("omenYear", year);
                params1.put("omenMonth", month);
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getTokenPayment extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;
        private boolean factor;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String AppVersion = urls[1];
            String Authorization = urls[2];
            String amount = urls[3];
            String mobileNo = urls[4];
            String paymentType = urls[5];
            String orderId = urls[6];
            String forceFactor = urls[7];
            if (forceFactor.equals("false")) {
                factor = false;
            } else {
                factor = true;

            }
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
//                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("Amount", amount);
                params1.put("MobileNo", mobileNo);
                params1.put("PaymentType", paymentType);
                params1.put("OrderId", orderId);
                params1.put("Force4Factor", factor);


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AppVersion", AppVersion);
                connection.setRequestProperty("Authorization", Authorization);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getProducts extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String groupId = urls[3];
            String date = urls[4];
            String time = urls[5];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("productGroupId", groupId);
                params1.put("locationLat", G.LATITUDE);
                params1.put("locationLong", G.LONGTITUDE);
                params1.put("lastLocalDate", date);
                params1.put("lastLocalTime", time);
                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
                    params1.put("customerId", customerId);
                }

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getTarget extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerId = urls[3];
            String groupId = urls[4];
            String year = urls[5];
            String month = urls[6];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerId);
                params1.put("groupId", groupId);
                params1.put("targetYear", year);
                params1.put("targetMonth", month);
//                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
//                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//                    params1.put("customerId", customerId);
//                }

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class addTarget extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerId = urls[3];
            String groupId = urls[4];
            String year = urls[5];
            String month = urls[6];
            String title = urls[7];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerId);
                params1.put("groupId", groupId);
                params1.put("targetYear", year);
                params1.put("targetMonth", month);
                params1.put("title", title);
//                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
//                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//                    params1.put("customerId", customerId);
//                }

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class editTarget extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerId = urls[3];
            String groupId = urls[4];
            String year = urls[5];
            String month = urls[6];
            String title = urls[7];
            String state = urls[8];
            String targetId = urls[9];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerId);
                params1.put("groupId", groupId);
                params1.put("targetYear", year);
                params1.put("targetMonth", month);
                params1.put("title", title);
                params1.put("isDone", Boolean.parseBoolean(state));
                params1.put("targetId", targetId);
//                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
//                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//                    params1.put("customerId", customerId);
//                }

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class editData extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;
        private boolean done;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerId = urls[3];
            String groupId = urls[4];
            String year = urls[5];
            String month = urls[6];
            String title = urls[7];
            String targetId = urls[8];
            String isDone = urls[9];
            if (isDone.equals("false")) {
                done = false;
            } else {
                done = true;

            }
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerId);
                params1.put("groupId", groupId);
                params1.put("targetYear", year);
                params1.put("targetMonth", month);
                params1.put("title", title);
                params1.put("targetId", targetId);
                params1.put("isDone", done);
//                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
//                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//                    params1.put("customerId", customerId);
//                }

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class deleteTarget extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerId = urls[3];
            String groupId = urls[4];
            String year = urls[5];
            String month = urls[6];
            String title = urls[7];
            String targetId = urls[8];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerId);
                params1.put("groupId", groupId);
                params1.put("targetYear", year);
                params1.put("targetMonth", month);
                params1.put("title", title);
                params1.put("targetId", targetId);
//                if (G.IS_REGISTERED.getBoolean("IS_REGISTERED", false)) {
//                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//                    params1.put("customerId", customerId);
//                }

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class sendOrders extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            JSONArray array = null;
            try {
                array = new JSONArray(urls[4]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            String orderItemList = array;
//            String orderItemList = urls[4];
            String orderComment = urls[5];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);
                params1.put("OrderItemList", array);
//                params1.put("orderComment", orderComment);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class sendPoll extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            JSONArray array = null;
            try {
                array = new JSONArray(urls[4]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            String orderItemList = array;
//            String orderItemList = urls[4];
//            String orderComment = urls[5];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);
                params1.put("pollItemList", array);
//                params1.put("orderComment", orderComment);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class sendTestAnswer extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String headerID = urls[4];
            JSONArray array = null;
            try {
                array = new JSONArray(urls[5]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            String orderItemList = array;
//            String orderItemList = urls[4];
//            String orderComment = urls[5];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);
                params1.put("headerId", headerID);
                params1.put("questionList", array);
//                params1.put("orderComment", orderComment);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getOrders extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getPoll extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getTestItem extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String HeaderID = urls[4];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);
                params1.put("headerId", HeaderID);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getScore extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getCode extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String cellPhone = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("cellPhone", cellPhone);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getRepresentative extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class sendMessage extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String title = urls[4];
            String message = urls[5];
            String state = urls[6];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);
                if (state.equals("title")) {
                    params1.put("title", title);
                } else {
                    params1.put("conversationId", title);
                }
                params1.put("message", message);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class Buy extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String Amount = urls[4];
            String PriceUnitId = urls[5];
            String orderId = urls[6];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);
                params1.put("Amount", Integer.parseInt(Amount));
                params1.put("PriceUnitId", Integer.parseInt(PriceUnitId));
                params1.put("orderId", orderId);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class sendRepresentative extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String tell = urls[4];
            String address = urls[5];
            String cityId = urls[6];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);
                params1.put("tel", tell);
                params1.put("address", address);
                params1.put("cityId", cityId);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getConversation extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String conversationId = urls[4];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);
                params1.put("conversationId", conversationId);


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getConversationList extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class logOutSession extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String customerID = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("customerId", customerID);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    static class Notification extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String firstPart = urls[1];
            String secondPart = urls[2];
//            String secondPart = urls[2];
//            String thirdPart = urls[3];
//            String Url = null;
//            if (firstPart.equals("notif")) {
//                Url = "http://www.20decor.com/index.php?route=apiv1/notification";
//            } else if (firstPart != "notif") {
////                    Url = Splash_Screen.routs.getString(linkType);
//            }
//            String link = linkType + firstPart;


//            Integer thirdConvert = Integer.parseInt(thirdPart);
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("fcm_token", firstPart);
                params1.put("device_id", secondPart);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return post_result;
        }


    }

    static class FilterPostClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String link = urls[0];
            String strLocation = urls[1];
            String strSaleType = urls[2];
            String strHouseType = urls[3];
            String strHouseTools = urls[4];
            String strColdTools = urls[5];
            String strHeatTools = urls[6];
            String strPrice = urls[7];
            String strSize = urls[8];

            String post_result = null;

            try {
                URL url = new URL(link);
                post_result = "";
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                try {
                    if (strLocation.length() > 0) {
                        if (!strLocation.equals("null")) {
                            params1.put("ads[location_area]", strLocation);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strSaleType.length() > 0) {
                        if (!strSaleType.equals("null")) {
                            params1.put("ads[house_sale_type]", strSaleType);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strHouseType.length() > 0) {
                        if (!strHouseType.equals("null")) {
                            params1.put("ads[house_type]", strHouseType);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strHouseTools.length() > 0) {
                        if (!strHouseTools.equals("null")) {
                            params1.put("ads[house_tools]", strHouseTools);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strColdTools.length() > 0) {
                        if (!strColdTools.equals("null")) {
                            params1.put("filter[][house_temperature_cold]", strColdTools);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strHeatTools.length() > 0) {
                        if (!strHeatTools.equals("null")) {
                            params1.put("filter[][house_temperature_heat]", strHeatTools);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strPrice.length() > 0) {
                        params1.put("ads[price]", strPrice);
                    }
                } catch (Exception v) {
                }
                try {
                    if (strSize.length() > 0) {
                        params1.put("ads[house_total_size]", strSize);
                    }
                } catch (Exception v) {
                }
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return post_result;
        }
    }

    static class TourFilterPostClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String link = urls[0];
            String strSource = urls[1];
            String strTarget = urls[2];
            String strHouseType = urls[3];
            String strHouseTools = urls[4];
            String strColdTools = urls[5];
            String strHeatTools = urls[6];
            String strPrice = urls[7];
            String strSize = urls[8];

            String post_result = null;

            try {
                URL url = new URL(link);
                post_result = "";
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                try {
                    params1.put("ads[ads_type]", "3");
                } catch (Exception v) {
                }
                try {
                    if (strSource.length() > 0) {
                        if (!strSource.equals("null")) {
                            params1.put("ads[tour_source_city]", strSource);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strTarget.length() > 0) {
                        if (!strTarget.equals("null")) {
                            params1.put("ads[tour_target_city]", strTarget);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strHouseType.length() > 0) {
                        if (!strHouseType.equals("null")) {
                            params1.put("ads[house_type]", strHouseType);
                        }
                    }
                } catch (Exception v) {
                }
                try {
                    if (strHouseTools.length() > 0) {
                        params1.put("ads[house_tools]", strHouseTools);
                    }
                } catch (Exception v) {
                }
                try {
                    if (strColdTools.length() > 0) {
                        params1.put("filter[][house_temperature_cold]", strColdTools);
                    }
                } catch (Exception v) {
                }
                try {
                    if (strHeatTools.length() > 0) {
                        params1.put("filter[][house_temperature_heat]", strHeatTools);
                    }
                } catch (Exception v) {
                }
                try {
                    if (strPrice.length() > 0) {
                        params1.put("ads[price]", strPrice);
                    }
                } catch (Exception v) {
                }
                try {
                    if (strSize.length() > 0) {
                        params1.put("ads[house_total_size]", strSize);
                    }
                } catch (Exception v) {
                }
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return post_result;
        }


    }

    static class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(linkType);
                urlConnection = (HttpURLConnection) url.openConnection();

                int code = urlConnection.getResponseCode();

                if (code == 200) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    if (in != null) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null)
                            result += line;
                    }
                    in.close();
                }

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result;
        }
    }

    public static class getToken extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
//            String sessionId = urls[1];
//            String token = urls[2];
//            String customerID = urls[3];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
//                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
//                params1.put("customerId", customerID);


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                JSONObject x = new JSONObject(params1);

                String urlParameters = x.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("AppVersion", "1.0.0");

                String text = "rs_movafaghyat@yahoo.com | Movafaghyat@3";
                byte[] data = text.getBytes(StandardCharsets.UTF_8);
                String userName = Base64.encodeToString(data, Base64.DEFAULT);


                connection.setRequestProperty("Authorization", "Basic " + userName);
//                connection.setDoOutput(true);
//                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
//                dStream.writeBytes(urlParameters.toString());
//                dStream.flush();
//                dStream.close();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

    public static class getUpdates extends AsyncTask<String, Void, String> {

        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... urls) {
            String linkType = urls[0];
            String sessionId = urls[1];
            String token = urls[2];
            String versionName = urls[3];
            String versionCode = urls[4];
            String osType = urls[5];
            String post_result = null;

            try {
                URL url = new URL(linkType);
                post_result = "";
                connection = (HttpURLConnection) url.openConnection();

                Map<String, Object> params1 = new LinkedHashMap<>();
                params1.put("ownerId", G.CONTEXT.getResources().getString(R.string.owner_id));
                params1.put("versionName", versionName);
                params1.put("versionCode", versionCode);
                params1.put("osType", osType);
                params1.put("locationLat", String.valueOf(G.LATITUDE));
                params1.put("locationLong", String.valueOf(G.LONGTITUDE));
//                Boolean isRegistered = G.RULES_ACCEPTED.getBoolean("RULES_ACCEPTED", false);
//                if (isRegistered) {
//                    String customerId = G.CUSTOMER_ID.getString("CUSTOMER_ID", "x");
//                    params1.put("customerId", customerId);
//                }


                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params1.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                String urlParameters = postData.toString();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("AHS_SessionId", sessionId);
                connection.setRequestProperty("AHS_UserToken", token);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters.toString());
                bw.flush();
                bw.close();
                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters.toString());
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters.toString());
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                    post_result += line;
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //Print Error Body
                InputStream _is;
                try {
                    if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        _is = connection.getInputStream();
                    } else {
                        /* error from server */
                        _is = connection.getErrorStream();
                    }
                    String response = "";
                    String line1;
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(_is));
                    while ((line1 = br1.readLine()) != null) {
                        response += line1;
                    }
                    Log.d("error", "Response: " + response);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return post_result;
        }


    }

}
