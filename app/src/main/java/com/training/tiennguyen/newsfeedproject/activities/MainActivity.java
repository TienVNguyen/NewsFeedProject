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
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.training.tiennguyen.newsfeedproject.R;
import com.training.tiennguyen.newsfeedproject.adapters.NewsAdapter;
import com.training.tiennguyen.newsfeedproject.asyncTask.NewsAsyncTask;
import com.training.tiennguyen.newsfeedproject.asyncTask.NewsOnTaskCompleted;
import com.training.tiennguyen.newsfeedproject.constants.VariablesConstant;
import com.training.tiennguyen.newsfeedproject.models.NewsModel;

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

        // NewsAsyncTask
        NewsAsyncTask asyncTask = new NewsAsyncTask(new NewsOnTaskCompleted() {
            @Override
            public void onTaskCompleted(final List<NewsModel> newsModelList) {
                if (newsModelList.size() > VariablesConstant.ZERO) {
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

        });
        asyncTask.execute(parameter);
    }
}
