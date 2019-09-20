package com.develop.web_server.io.repository;


import com.develop.web_server.io.entity.PasswordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordRepository extends CrudRepository<PasswordEntity, Long> {

    PasswordEntity findByToken(String token);
}
