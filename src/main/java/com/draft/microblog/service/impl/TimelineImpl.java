package com.draft.microblog.service.impl;

import com.draft.microblog.domain.TweetsRepo;
import com.draft.microblog.domain.impl.TweetsImpl;
import com.draft.microblog.service.Timeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by SMALVIYA on 12/10/2017.
 */

/**
 * Maintains User's Timeline Cache and persists it in the d/b
 */
@Component
public class TimelineImpl implements Timeline {
    private HashMap<Integer, PriorityQueue<TweetsImpl>> userHomeCache = new HashMap<>();
    @Autowired
    private TweetsRepo tweetsRepo;

    /**
     * Returns the latest Tweets by the user from the cache. In case of the failover builds the cache as well.
     * @param userId :: user Id to ge fetch the latest tweets
     * @return :: list of latest tweets
     */
    public List<TweetsImpl> getUserTweets(Integer userId){
        PriorityQueue<TweetsImpl> userTweets = userHomeCache.get(userId);
        PriorityQueue<TweetsImpl> userTweetsCopy = null;
        List<TweetsImpl> feeds = new ArrayList<>();
        synchronized (userTweets) {
            /**
             * Construct the Cache. Failover Scenario
             */
            if (userTweets == null) {
                List<TweetsImpl> tweetList = tweetsRepo.getByUserId(userId);
                userTweets = new PriorityQueue<>(TwitterServiceImpl.ScrollSize, (t1, t2) -> (t2.getCreationTime().compareTo(t1.getCreationTime())));
                for (int i = 0; i < Math.min(tweetList.size(), TwitterServiceImpl.ScrollSize); i++) {
                    userTweets.add(tweetList.get(i));
                }
                userHomeCache.put(userId, userTweets);
            }
            userTweetsCopy = new PriorityQueue<>(userTweets);
        }
        if(userTweetsCopy != null){
            while(!userTweetsCopy.isEmpty()){
                feeds.add(userTweetsCopy.poll());
            }
        }
        return feeds;
    }

    /**
     * Updates the d/b and the user timeline cache with the new tweet
     * @param tweet :: new tweet
     * @return :: returns the saved tweet.
     */
    public TweetsImpl saveUserTweet(TweetsImpl tweet){
        TweetsImpl tweetCreated = tweetsRepo.save(tweet);
        PriorityQueue<TweetsImpl> userHome = userHomeCache.get(tweetCreated.getUserId());
        if(userHome == null){
            userHome = new PriorityQueue<TweetsImpl>(TwitterServiceImpl.ScrollSize, (t1, t2)->(t2.getCreationTime().compareTo(t1.getCreationTime())));
        }
        synchronized (userHome){
            if(userHome.size()>= TwitterServiceImpl.ScrollSize){
                userHome.remove(userHome.peek());
            }
            if(!userHome.contains(tweetCreated))
            {
                userHome.add(tweetCreated);
            }
            userHomeCache.put(tweetCreated.getUserId(), userHome);
        }
        return tweetCreated;
    }
}
