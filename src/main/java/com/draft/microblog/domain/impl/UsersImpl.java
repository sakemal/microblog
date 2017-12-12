package com.draft.microblog.domain.impl;

import com.draft.microblog.domain.Users;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity(name = "UsersImpl")
public class UsersImpl implements Users {
    @Id
    @GeneratedValue
    private Integer userid;
    private String userName;
    @ElementCollection(targetClass=Integer.class)
    private List<Integer> follows = new ArrayList<>();

    @ElementCollection(targetClass=Integer.class)
    private List<Integer> followedBy = new ArrayList<>();

    public UsersImpl() {
    }
    public UsersImpl(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFollows(List<Integer> follows) {
        this.follows = follows;
    }

    public List<Integer> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(List<Integer> followedBy) {
        this.followedBy = followedBy;
    }

    public Integer getUserid() {
        return userid;
    }

    public List<Integer> getFollows() {
        return follows;
    }
    public void follow(Integer userId) {
        this.follows.add(userId);
    }
    public void unFollow(Integer userId) {
        this.follows.remove(userId);
    }
    public List<Integer> getFollowers() {
        return followedBy;
    }
    public void addFollower(Integer userId) {
        this.followedBy.add(userId);
    }
    public void removeFollower(Integer userId) {
        this.followedBy.remove(userId);
    }


}
