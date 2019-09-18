package com.develop.web_server.io.repository;

import com.develop.web_server.io.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    UserEntity findByUserID(String userId);
}

