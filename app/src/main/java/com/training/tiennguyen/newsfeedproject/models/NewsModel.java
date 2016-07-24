/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.newsfeedproject.models;

import java.io.Serializable;

/**
 * NewsModel
 *
 * @author TienNguyen
 */
public class NewsModel implements Serializable {
    /**
     * title
     */
    private String title;

    /**
     * sectionName
     */
    private String sectionName;

    /**
     * publishDate
     */
    private String publishDate;

    /**
     * link
     */
    private String link;

    /**
     * Constructor
     */
    public NewsModel() {
    }

    /**
     * Constructor
     *
     * @param title       String
     * @param sectionName String
     * @param publishDate String
     * @param link        String
     */
    public NewsModel(String title, String sectionName, String publishDate, String link) {
        this.title = title;
        this.sectionName = sectionName;
        this.publishDate = publishDate;
        this.link = link;
    }

    /**
     * gets method
     *
     * @return title String
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets method
     *
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets method
     *
     * @return sectionName String
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * sets method
     *
     * @param sectionName String
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * gets method
     *
     * @return publishDate String
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * sets method
     *
     * @param publishDate String
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * gets method
     *
     * @return link String
     */
    public String getLink() {
        return link;
    }

    /**
     * sets method
     *
     * @param link String
     */
    public void setLink(String link) {
        this.link = link;
    }
}
