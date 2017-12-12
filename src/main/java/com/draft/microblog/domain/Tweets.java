package com.draft.microblog.domain;

import com.draft.microblog.domain.impl.TweetsImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
@JsonDeserialize(as= TweetsImpl.class)
public interface Tweets {
}
