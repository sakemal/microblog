package com.draft.microblog.controller;

import com.draft.microblog.domain.impl.TweetsImpl;
import com.draft.microblog.domain.impl.UsersImpl;
import com.draft.microblog.service.UserChoice;
import com.draft.microblog.service.impl.TwitterServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

/**
 * Created by SMALVIYA on 12/10/2017.
 */

/**
 * Main REST Controller for the Application
 */
@CrossOrigin
@RestController
public class MicroblogController {
    @Autowired
    private TwitterServiceImpl twitterService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MicroblogController.class);

    /**
     * REST end point for fetching feeds for a user
     * @param userId :: userId for the User
     * @return :: List of the latest feeds from all the users the given user is following.
     */
    @RequestMapping(path = "/feed/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<TweetsImpl>> getNewsFeed(@PathVariable final Integer userId) {
        LOGGER.info("Feed requested for user :: "+userId);
        List<TweetsImpl> newsFeed = twitterService.getNewsFeed(userId);
        if (newsFeed == null) {
            return status(HttpStatus.NOT_FOUND).body(null);
        }
        return ok(newsFeed);
    }

    /**
     * REST end point for creating new Users in the system.
     * @param user
     * @param ucb
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<UsersImpl> store(@RequestBody final UsersImpl user,
                                           final UriComponentsBuilder ucb) {
        LOGGER.info("New User Create request");

        final UsersImpl userCreated = twitterService.createUser(user);
        final UriComponents uriComponents = ucb.path(userCreated.getUserid().toString()).build();
        final URI location = uriComponents.encode().toUri();

        return created(location).body(userCreated);
    }
    @ResponseBody
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<UsersImpl> listPersons() {
        return twitterService.findAllUser();
    }

    /**
     * REST End point for posting new tweets.
     * @param userChoice
     * @param ucb
     * @return
     */
    @RequestMapping(path = "/user", method = RequestMethod.PUT)
    public ResponseEntity<UsersImpl> store(@RequestBody final UserChoice userChoice,
                                           final UriComponentsBuilder ucb) {
        UsersImpl updatedUser = twitterService.updateUserChoice(userChoice);
        if (updatedUser == null) {
            return status(HttpStatus.NOT_FOUND).body(null);
        }
        return ok(updatedUser);
    }
    @RequestMapping(path = "/tweet", method = RequestMethod.POST)
    public ResponseEntity<TweetsImpl> store(@RequestBody final TweetsImpl tweet,
                                           final UriComponentsBuilder ucb) {
        LOGGER.info("New Tweet Post request");
        final TweetsImpl tweetPosted = twitterService.postTweet(tweet);
        final UriComponents uriComponents = ucb.path(tweetPosted.getTweetId().toString()).build();
        final URI location = uriComponents.encode().toUri();

        return created(location).body(tweetPosted);
    }
    /**
     * REST end point for fetching timeline for a user
     * @param userId :: userId for the User
     * @return :: Timeline with his latest feeds.
     */
    @RequestMapping(path = "/timeline/{userId}", params = { "page", "size" }, method = RequestMethod.GET)
    public ResponseEntity<List<TweetsImpl>> getTimeline(@PathVariable final Integer userId, @RequestParam( "page" ) int page, @RequestParam( "size" ) int size) {
        LOGGER.info("Timeline requested for user :: "+userId);
        if(size > 0){
            List<List<TweetsImpl>> timelinePages = twitterService.getTimelinePaginated(userId,size);
            if(page > timelinePages.size()){
                return status(HttpStatus.NOT_FOUND).body(null);
            }
            else{
                return ok(timelinePages.get(page - 1));
            }
        }
        else {
            List<TweetsImpl> timeline = twitterService.getTimeline(userId);
            if (timeline == null) {
                return status(HttpStatus.NOT_FOUND).body(null);
            }
            return ok(timeline);
        }
    }

}
