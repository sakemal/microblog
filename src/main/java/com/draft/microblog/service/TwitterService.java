package com.draft.microblog.service;

import com.draft.microblog.domain.impl.TweetsImpl;
import com.draft.microblog.domain.impl.UsersImpl;

import java.util.List;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
public interface TwitterService {
    /**
     * Creates user in the repo
     *
     * @param user :: user to be created
     * @return :: created user
     */
    public UsersImpl createUser(UsersImpl user);

    /**
     * Fetches the list of Followers for the the user.
     *
     * @param userId :: userId for the user.
     * @return :: List of Users the user is following
     */
    public List<Integer> getUserFollowers(Integer userId);
    /**
     * Updateds user Choice in terms of following or unFollowing another user.
     * @param userChoice :: User followed or Unfollowed
     * @return:: updated User record
     */
    public UsersImpl updateUserChoice(UserChoice userChoice);
    /**
     * Saves tweet in the user timeline cache and d/b. Starts a thread to update news feed of all the user he is following.
     *
     * @param tweet :: tweet posted
     * @return
     */
    public TweetsImpl postTweet(TweetsImpl tweet);

    /**
     * Fetches the list of users given user is following.
     * @param userId :: userId for the user.
     * @return :: List of users given user is following.
     */
    public List<Integer> getUserFollowees(Integer userId);

    /**
     * Presents the latest tweets from the users the given user is following.
     * @param userId :: userId of  the user.
     * @return:: List of the latest tweets from the users the given user is following.
     */
    public List<TweetsImpl> getNewsFeed(Integer userId);

    /**
     *Presents the latest tweets of the user on his timeline
     * @param userID userId of  the user.
     * @return: List of the latest tweets of the user on his timeline.
     */
    public List<TweetsImpl> getTimeline(Integer userID);

    /**
     *Presents the Paginated latest tweets of the user on his timeline
     * @param userID userId of  the user.
     * @param size : number of tweets per page
     * @return: Paginated List of the latest tweets of the user on his timeline.
     */
    public List<List<TweetsImpl>> getTimelinePaginated(Integer userID, int size);
}
