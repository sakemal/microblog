package com.draft.microblog.service;

import com.draft.microblog.domain.impl.TweetsImpl;
import com.draft.microblog.service.impl.TwitterServiceImpl;

import java.util.List;

/**
 * Created by SMALVIYA on 12/9/2017.
 */

/**
 * Runs in thread and updates news feed of all the Followers of the user.
 */
public class NewsFeedUpdateService implements Runnable {
    TweetsImpl tweet;
    Integer userId;
    private TwitterServiceImpl twitterService;

    public NewsFeedUpdateService(TweetsImpl tweet, Integer userId, TwitterServiceImpl twitterService) {
        this.tweet = tweet;
        this.userId = userId;
        this.twitterService = twitterService;
    }

    @Override
    public void run() {
        List<Integer> followers = twitterService.getUserFollowers(userId);
        if(followers != null && followers.size() != 0) {
            for (Integer user : followers) {
                twitterService.getNewsFeedService().updateNewsFeedCache(tweet, user);
            }
        }
    }
}
