package com.draft.microblog.service.impl;

import com.draft.microblog.domain.UserRepo;
import com.draft.microblog.domain.impl.TweetsImpl;
import com.draft.microblog.domain.impl.UsersImpl;
import com.draft.microblog.service.NewsFeedUpdateService;
import com.draft.microblog.service.TwitterService;
import com.draft.microblog.service.UserChoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
@Component
public class TwitterServiceImpl implements TwitterService{
    public static final int StepSize = 5;
    public static final int ScrollSize = 100;
    private List<String> updatedAccount;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NewsFeedImpl newsFeed;
    @Autowired
    private TimelineImpl userTimeline;

    HashMap<Integer, List<Integer>> userFollowers = new HashMap<>();
    HashMap<Integer, List<Integer>> userFollowees = new HashMap<>();

    /**
     * Creates user in the repo
     * @param user :: user to be created
     * @return :: created user
     */
    public UsersImpl createUser(UsersImpl user){
        return userRepo.save(user);
    }

    /**
     * Updateds user Choice in terms of following or unFollowing another user.
     * @param userChoice :: User followed or Unfollowed
     * @return:: updated User record
     */

    public UsersImpl updateUserChoice(UserChoice userChoice){
        UsersImpl user = userRepo.findOne(userChoice.userId);
        UsersImpl updatedUser = null;
        boolean update = false;
        if(userChoice.followUser !=0 ){
            user.follow(userChoice.followUser);
            UsersImpl userFollowed = userRepo.findOne(userChoice.followUser);
            userFollowed.addFollower(userChoice.userId);
            userRepo.save(userFollowed);
            //updateCache
            List<Integer> followers = userFollowers.get(userChoice.followUser);
            if(followers==null){
                followers = new ArrayList<>();
            }
            followers.add(userChoice.userId);
            userFollowers.put(userChoice.followUser,followers );
            List<Integer> followees = userFollowees.get(userChoice.userId);
            if(followees==null){
                followees = new ArrayList<>();
            }
            followees.add(userChoice.followUser);
            userFollowees.put(userChoice.userId,followees );
            update = true;
        }
        if(userChoice.unFollowUser !=0 ){
            user.unFollow(userChoice.unFollowUser);
            UsersImpl userUnFollowed = userRepo.findOne(userChoice.unFollowUser);
            userUnFollowed.removeFollower(userChoice.userId);
            userRepo.save(userUnFollowed);
            //updateCache
            List<Integer> followers = userFollowers.get(userChoice.followUser);
            followers.remove(userChoice.userId);
            List<Integer> followees = userFollowees.get(userChoice.userId);
            followees.remove(userChoice.followUser);
            update = true;
        }
        if(update){
            updatedUser = userRepo.save(user);
            //updateCache

        }
        return updatedUser;
    }

    /**
     * Fetches the list of Followers for the the user.
     * @param userId :: userId for the user.
     * @return :: List of Users the user is following
     */
    public List<Integer> getUserFollowers(Integer userId){
        return userFollowers.get(userId);
    }

    /**
     * Fetches the list of users given user is following.
     * @param userId :: userId for the user.
     * @return :: List of users given user is following.
     */
    public List<Integer> getUserFollowees(Integer userId){
        return userFollowees.get(userId);
    }

    /**
     * Saves tweet in the user timeline cache and d/b. Starts a thread to update news feed of all the user he is following.
     * @param tweet :: tweet posted
     * @return
     */
    public TweetsImpl postTweet(TweetsImpl tweet){
        TweetsImpl updatedTweet = userTimeline.saveUserTweet(tweet);

        NewsFeedUpdateService updateService =  new NewsFeedUpdateService(updatedTweet,updatedTweet.getUserId(),this);
        Thread updateFollowers = new Thread(updateService);
        updateFollowers.start();
        return updatedTweet;
    }

    /**
     * Presents the latest tweets from the users the given user is following.
     * @param userId :: userId of  the user.
     * @return:: List of the latest tweets from the users the given user is following.
     */
    public List<TweetsImpl> getNewsFeed(Integer userId){
        return newsFeed.getNewsFeed(userId);
    }

    /**
     *Presents the latest tweets of the user on his timeline
     * @param userID userId of  the user.
     * @return: List of the latest tweets of the user on his timeline.
     */
    public List<TweetsImpl> getTimeline(Integer userID){
        return userTimeline.getUserTweets(userID);
    }

    /**
     *Presents the Paginated latest tweets of the user on his timeline
     * @param userID userId of  the user.
     * @param size : number of tweets per page
     * @return: Paginated List of the latest tweets of the user on his timeline.
     */
    public List<List<TweetsImpl>> getTimelinePaginated(Integer userID, int size){
        List<TweetsImpl> userTweets = userTimeline.getUserTweets(userID);
        int noOfTweets = userTweets.size(), index= 0;
        List<List<TweetsImpl>> timelinePages = new ArrayList<>();
        while(index< noOfTweets) {
            List<TweetsImpl> timelinePage = new ArrayList<>();
            for (int pageIndex = 0; pageIndex < Math.min(size,noOfTweets) && (index< noOfTweets); pageIndex++) {
                timelinePage.add(userTweets.get(index));
                index++;
            }
            timelinePages.add(timelinePage);
        }
        return timelinePages;
    }

    /**
     *
     * @return:: handle to the news feed component.
     */
    public NewsFeedImpl getNewsFeedService(){
        return newsFeed;
    }
    public List<UsersImpl> findAllUser(){
        return userRepo.findAll();
    }
}
