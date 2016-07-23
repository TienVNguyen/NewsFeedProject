/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.newsfeedproject.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.training.tiennguyen.newsfeedproject.R;
import com.training.tiennguyen.newsfeedproject.adapters.NewsAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * MainActivity
 *
 * @author TienNguyen
 */
public class MainActivity extends AppCompatActivity {
    /**
     * lv_news
     */
    @BindView(R.id.lv_news)
    protected ListView lvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initViews
        initViews();
    }

    /**
     * initViews
     */
    private void initViews() {
        ButterKnife.bind(this);

        if (verifyInternetConnection()) {
            // Set the content
            callTheGuardianAPIForData();
        } else {
            // Message to user to close the app
            new AlertDialog.Builder(this)
                    .setTitle(R.string.message_no_value)
                    .setMessage(R.string.message_no_value)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Exit app
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    /**
     * verifyInternetConnection
     *
     * @return boolean
     */
    private boolean verifyInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * callTheGuardianAPIForData
     */
    private void callTheGuardianAPIForData() {
        // MOCK parameter
        String[] parameter = new String[4];
        parameter[0] = "debate";
        parameter[1] = "politics/politics";
        parameter[2] = "2014-01-01";
        parameter[3] = "test";

        // DownloadWebpageTask
        new DownloadWebpageTask().execute(parameter);
    }

    /**
     * Uses AsyncTask to create a task away from the main UI thread. This task takes a
     * URL string and uses it to create an HttpUrlConnection. Once the connection
     * has been established, the AsyncTask downloads the contents of the webpage as
     * an InputStream. Finally, the InputStream is converted into a string, which is
     * displayed in the UI by the AsyncTask's onPostExecute method.
     */
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        /**
         * List<NewsModel>
         */
        private List<NewsModel> newsModelList = new ArrayList<>();

        @Override
        protected String doInBackground(String... urls) {
            try {
                // Query base on MOCK parameter
                StringBuilder query = new StringBuilder();
                query.append(VariablesConstant.URL_API);
                query.append(VariablesConstant.URL_API_QUERY);
                query.append(urls[0]);
                query.append(VariablesConstant.URL_API_TAG);
                query.append(urls[1]);
                query.append(VariablesConstant.URL_API_FROM_DATE);
                query.append(urls[2]);
                query.append(VariablesConstant.URL_API_KEY);
                query.append(urls[3]);

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
        protected void onPostExecute(String result) {
            if (!result.isEmpty()) {
                lvNews.setVisibility(View.VISIBLE);
                NewsAdapter newsAdapter = new NewsAdapter(getApplicationContext(), R.layout.news_list_item, newsModelList);
                lvNews.setAdapter(newsAdapter);
                lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String url = newsModelList.get(i).getLink();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });
            } else {
                // Message to user
                lvNews.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),
                        getApplicationContext().getString(R.string.message_no_value),
                        Toast.LENGTH_SHORT).show();

                // Exit app
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
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
