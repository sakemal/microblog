package com.draft.microblog.service;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by SMALVIYA on 12/10/2017.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserChoice {
    public Integer userId;
    public Integer followUser;
    public Integer unFollowUser;

    public UserChoice() {
    }

    public UserChoice(Integer userId, Integer followUser, Integer unFollowUser) {
        this.userId = userId;
        this.followUser = followUser;
        this.unFollowUser = unFollowUser;
    }
}
