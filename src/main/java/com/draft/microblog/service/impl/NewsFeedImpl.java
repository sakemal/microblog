package com.draft.microblog.service.impl;

import com.draft.microblog.domain.impl.TweetsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
@Component
public class NewsFeedImpl {

    @Autowired
    private TwitterServiceImpl twitterService;
    private HashMap<Integer, PriorityQueue<TweetsImpl>> newsFeedCache = new HashMap<>();

    /**
     * Updates News feed cache of the user when any user he is following tweets.
     * @param tweet :: tweets added by the followee.
     * @param userId :: userId of the user.
     */
    public void updateNewsFeedCache(TweetsImpl tweet, Integer userId){
        PriorityQueue<TweetsImpl> userTimeline = newsFeedCache.get(userId);
        if(userTimeline == null){
            userTimeline = new PriorityQueue<>(TwitterServiceImpl.ScrollSize, (t1,t2)->(t2.getCreationTime().compareTo(t1.getCreationTime())));
        }
        synchronized (userTimeline){
            if(userTimeline.size()>= TwitterServiceImpl.ScrollSize){
                userTimeline.remove(userTimeline.peek());
            }
            if(!userTimeline.contains(tweet))
            {
                userTimeline.add(tweet);
            }
            newsFeedCache.put(userId, userTimeline);
        }
    }

    /**
     * Presents latest news feed for the user with the latest tweets from all the users he is following.
     * In the failover scenario also re-builds the cache.
     * @param userId:: user ID of the user.
     * @return ::List of the latest tweets from all the users followed by the given user.
     */
    public List<TweetsImpl> getNewsFeed(Integer userId){
        PriorityQueue<TweetsImpl> userTimelineCopy = getUserNewsFeedCache(userId);
        List<TweetsImpl> feeds = new ArrayList<>();
        if(userTimelineCopy != null){
            while(!userTimelineCopy.isEmpty()){
                feeds.add(userTimelineCopy.poll());
            }
        }
        return feeds;
    }

    /**
     * Rebuilds News feed cache for the user in the failover scenario.
     * @param userId
     */
    private void constructUserFeedCache(Integer userId){
        List<Integer> followeeList = twitterService.getUserFollowees(userId);

        List<List<TweetsImpl>> tweetsForFeed = new ArrayList<>();
        int totalTweets =0, tweetsAdded = 0, index = 0;
        /**
         * Prepare the list of available tweets from all the Followee's
         */
        if(followeeList != null && followeeList.size() >0) {
            for (Integer user : followeeList) {
                List<TweetsImpl> tweetsOfUser = twitterService.getNewsFeedService().getNewsFeed(user);
                tweetsForFeed.add(tweetsOfUser);
                totalTweets = totalTweets + tweetsOfUser.size();
            }
            PriorityQueue<TweetsImpl> newsQueue = new PriorityQueue<>(Math.min(totalTweets, TwitterServiceImpl.ScrollSize), (t1, t2) -> (t2.getCreationTime().compareTo(t1.getCreationTime())));
            /**
             * Iterate over the users and add tweets for the newsFeed.
             * It can start from a pre populated
             */
            while (newsQueue.size() < TwitterServiceImpl.ScrollSize && totalTweets >= tweetsAdded) {
                for (List<TweetsImpl> userTweet : tweetsForFeed) {
                    if (userTweet.size() > index) {
                        if (!newsQueue.contains(userTweet.get(index))) {
                            newsQueue.add(userTweet.get(index));
                            tweetsAdded++;
                        }
                    } else {
                        tweetsForFeed.remove(userTweet);
                        break;
                    }
                }
                index++;
            }
            newsFeedCache.put(userId, newsQueue);
        }
    }

    /**
     * returns news feed cache for a user.
     * @param userId
     * @return
     */
    private PriorityQueue<TweetsImpl> getUserNewsFeedCache(Integer userId){
        PriorityQueue<TweetsImpl> userTimeline = newsFeedCache.get(userId);
        PriorityQueue<TweetsImpl> userTimelineCopy = null;
        if(userTimeline == null){
            constructUserFeedCache(userId);
            userTimeline = newsFeedCache.get(userId);
        }
        if(userTimeline != null) {
            synchronized (userTimeline) {
                if (userTimelineCopy == null) {
                    userTimelineCopy = new PriorityQueue<>(userTimeline);
                }
            }
        }
        return userTimelineCopy;
    }
}
