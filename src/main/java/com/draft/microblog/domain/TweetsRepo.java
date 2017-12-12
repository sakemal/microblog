package com.draft.microblog.domain;

import com.draft.microblog.domain.impl.TweetsImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
@Repository
public interface TweetsRepo extends JpaRepository<TweetsImpl, Integer>{
    @Query("SELECT t FROM TweetsImpl t WHERE t.tweetId = :userId ORDER BY creationTime DESC")
    List<TweetsImpl> getByUserId(@Param("userId") Integer userId);
}
