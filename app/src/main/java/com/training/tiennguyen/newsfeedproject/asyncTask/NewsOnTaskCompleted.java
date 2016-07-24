/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.newsfeedproject.asyncTask;

import com.training.tiennguyen.newsfeedproject.models.NewsModel;

import java.util.List;

/**
 * NewsOnTaskCompleted
 *
 * @author TienNguyen
 */
public interface NewsOnTaskCompleted {
    /**
     * onTaskCompleted
     *
     * @param newsModelList List<NewsModel>
     */
    void onTaskCompleted(List<NewsModel> newsModelList);
}
