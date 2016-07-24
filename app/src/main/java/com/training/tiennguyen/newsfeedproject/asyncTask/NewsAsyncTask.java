/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.newsfeedproject.asyncTask;

import android.os.AsyncTask;

import com.training.tiennguyen.newsfeedproject.constants.VariablesConstant;
import com.training.tiennguyen.newsfeedproject.models.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * NewsAsyncTask
 *
 * @author TienNguyen
 */
public class NewsAsyncTask extends AsyncTask<Object, Object, Object> {
    /**
     * NewsOnTaskCompleted
     */
    private NewsOnTaskCompleted listener;
    /**
     * List<NewsModel>
     */
    private List<NewsModel> newsModelList = new ArrayList<>();

    /**
     * Constructor
     *
     * @param listener NewsOnTaskCompleted
     */
    public NewsAsyncTask(NewsOnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected Object doInBackground(Object... objects) {

        try {
            // Query base on MOCK parameter
            StringBuilder query = new StringBuilder();
            query.append(VariablesConstant.URL_API);
            query.append(VariablesConstant.URL_API_QUERY);
            query.append(objects[0].toString());
            query.append(VariablesConstant.URL_API_TAG);
            query.append(objects[1].toString());
            query.append(VariablesConstant.URL_API_FROM_DATE);
            query.append(objects[2].toString());
            query.append(VariablesConstant.URL_API_KEY);
            query.append(objects[3].toString());

            // Execute
            String resultQuery = downloadUrl(query.toString());
            if (!resultQuery.isEmpty()) {
                // Converting
                JSONObject reader = new JSONObject(resultQuery);
                if (reader.length() > 0) {
                    JSONArray results = reader.optJSONObject(VariablesConstant.WEB_RESPONSE)
                            .optJSONArray(VariablesConstant.WEB_RESULTS);

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        NewsModel newsModel = new NewsModel();
                        newsModel.setTitle(result.optString(VariablesConstant.WEB_TITLE));
                        newsModel.setSectionName(result.optString(VariablesConstant.SECTION_NAME));
                        newsModel.setPublishDate(result.optString(VariablesConstant.WEB_PUBLICATION_DATE));
                        newsModel.setLink(result.optString(VariablesConstant.WEB_URL));
                        newsModelList.add(newsModel);
                    }

                    return VariablesConstant.EXISTED_STRING_PROVIDED;
                }
            }
        } catch (IOException e) {
            return VariablesConstant.EMPTY_STRING_PROVIDED;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return VariablesConstant.EMPTY_STRING_PROVIDED;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o.toString().isEmpty()) {
            listener.onTaskCompleted(null);
        } else {
            listener.onTaskCompleted(newsModelList);
        }
    }

    /**
     * Given a URL, establishes an HttpUrlConnection and retrieves
     * the web page content as a InputStream, which it returns as a string.
     *
     * @param myurl String
     * @return String
     * @throws IOException
     */
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(VariablesConstant.REQUEST_METHOD_GET);
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            if (response == VariablesConstant.REQUEST_SUCCESS_CODE) {
                is = conn.getInputStream();
                return readIt(is);
            } else {
                return VariablesConstant.EMPTY_STRING_PROVIDED;
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream InputStream
     * @return String
     * @throws IOException
     */
    public String readIt(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream, VariablesConstant.UTF_8));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append(VariablesConstant.ENTER_LINE);
        }

        return total.toString();
    }
}
