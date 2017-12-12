package com.draft.microblog.service;

import com.draft.microblog.domain.impl.TweetsImpl;

import java.util.List;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
public interface NewsFeed {
    /**
     * Prepares a feed for a User. Iterates over all the Followes of the user and get their
     *  latest tweets and arrange them in the order of their posting.
     * @param userId :: UserId of the user for the feed
     * @return:: returns list of the Tweets.
     */
    public List<TweetsImpl> getNewsFeed(Integer userId);
    /**
     * Updates News feed cache of the user when any user he is following tweets.
     * @param tweet :: tweets added by the followee.
     * @param userId :: userId of the user.
     */
    public void updateNewsFeedCache(TweetsImpl tweet, Integer userId);

}
