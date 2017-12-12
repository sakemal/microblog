package com.draft.microblog.domain;

import com.draft.microblog.domain.impl.UsersImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
@JsonDeserialize(as= UsersImpl.class)
public interface Users {
    public Integer getUserid();
    public List<Integer> getFollows();
    public void follow(Integer userId);
    public void unFollow(Integer userId);
    public List<Integer> getFollowers();
    public void addFollower(Integer userId);
    public void removeFollower(Integer userId);

}
