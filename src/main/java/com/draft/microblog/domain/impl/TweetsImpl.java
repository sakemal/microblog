package com.draft.microblog.domain.impl;

import com.draft.microblog.domain.Tweets;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TweetsImpl implements Tweets{
    @Id
    @GeneratedValue
    private Integer tweetId;

    private String tweet;

    private Date creationTime;

    private Integer userId;

    public TweetsImpl(String tweet, Integer userId) {
        this.tweet = tweet;
        this.userId = userId;
    }

    public TweetsImpl() {
    }

    public void setTweetId(Integer tweetId) {
        this.tweetId = tweetId;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Integer getTweetId() {
        return tweetId;
    }

    public Integer getUserId() {
        return userId;
    }

    @PrePersist
    void setCreationTime(){
        this.creationTime = new Date();
    }
}
