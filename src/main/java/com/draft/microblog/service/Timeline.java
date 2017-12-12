package com.draft.microblog.service;

import com.draft.microblog.domain.impl.TweetsImpl;

import java.util.List;

/**
 * Created by SMALVIYA on 12/10/2017.
 */
public interface Timeline {

    /**
     * Returns the latest Tweets by the user from the cache. In case of the failover builds the cache as well.
     * @param userId :: user Id to ge fetch the latest tweets
     * @return :: list of latest tweets
     */
    public List<TweetsImpl> getUserTweets(Integer userId);

    /**
     * Updates the d/b and the user timeline cache with the new tweet
     * @param tweet :: new tweet
     * @return :: returns the saved tweet.
     */
    public TweetsImpl saveUserTweet(TweetsImpl tweet);
}
