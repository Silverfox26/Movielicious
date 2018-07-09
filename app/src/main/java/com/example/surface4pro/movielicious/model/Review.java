/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious.model;

/**
 * Class for custom Review objects.
 */
public class Review {

    private String author;
    private String content;

    /**
     * Review constructor
     *
     * @param author  Name of the review's author.
     * @param content Content of the Review.
     */
    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
