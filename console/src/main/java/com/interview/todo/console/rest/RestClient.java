package com.interview.todo.console.rest;

import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestClient {
    private Logger mLogger = Logger.getLogger("RestCleint");

    private static final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public RestResponse Post(String url, Map<String, String> headers, String body){
        try {
            //mLogger.log(Level.INFO, "Making the request with url: "+url+ " and body: "+body);
            RequestBody reqBody = RequestBody.create(JSON, body);
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .post(reqBody);

            if (null != headers) {
                for (Map.Entry<String,String> entry : headers.entrySet()) {
                    if (null != entry.getKey() && null != entry.getValue()) {
                        //mLogger.log(Level.INFO, "Adding Header: "+entry.getKey()+" and Value: "+entry.getValue());
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }

            Request request = builder.build();

            Response response = client.newCall(request).execute();
            //mLogger.log(Level.INFO,"NetworkResponse: "+response.networkResponse());

            RestResponse restResp = new RestResponse();
            restResp.setStatusCode(response.code());
            if (null != response.body()) {
                restResp.setResponse(response.body().string());
            }
            return restResp;

        } catch (IOException e) {
            mLogger.log(Level.SEVERE, "IOException while making the rest api post call",e);
            return null;
        }
    }

    public RestResponse Get(String url, Map<String, String> headers) {
        try {
            //mLogger.log(Level.INFO, "Making the GET request with url: " + url);
            Request.Builder builder = new Request.Builder().url(url).get();

            if (null != headers) {
                for (Map.Entry<String,String> entry : headers.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }

            Request request = builder.build();

            Response response = client.newCall(request).execute();
            //mLogger.log(Level.INFO, "NetworkResponse: "+response.networkResponse());

            RestResponse restResp = new RestResponse();
            restResp.setStatusCode(response.code());
            if (null != response.body()) {
                restResp.setResponse(response.body().string());
            }
            return restResp;

        } catch (IOException ex) {
            mLogger.log(Level.SEVERE, "IOException while making the rest api get call",ex);
            return null;
        }
    }

    public RestResponse Delete(String url, Map<String, String> headers, Map<String, String> queryParams, String body) {
        try {
            //mLogger.log(Level.INFO, "Making Delete request with url: " + url);
            HttpUrl.Builder httpBuider = HttpUrl.parse(url).newBuilder();

            if (null != queryParams) {
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    httpBuider.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }

            Request.Builder builder = new Request.Builder().url(httpBuider.build().toString()).delete();
            if (null != headers) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }

            Request request = builder.build();

            Response response = client.newCall(request).execute();
            //mLogger.log(Level.INFO, "NetworkResponse: " + response.networkResponse());

            RestResponse restResp = new RestResponse();
            restResp.setStatusCode(response.code());
            if (null != response.body()) {
                restResp.setResponse(response.body().string());
            }
            return restResp;
        } catch (IOException ex) {
            mLogger.log(Level.SEVERE, "IOException while making the rest api Delete call",ex);
            return null;
        }
    }
}


