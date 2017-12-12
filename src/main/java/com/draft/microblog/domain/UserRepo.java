package com.draft.microblog.domain;

import com.draft.microblog.domain.impl.UsersImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by SMALVIYA on 12/8/2017.
 */
@Repository
public interface UserRepo extends JpaRepository<UsersImpl, Integer> {
}
