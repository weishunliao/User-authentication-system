package com.develop.web_server.io.repository;


import com.develop.web_server.io.entity.AddressEntity;
import com.develop.web_server.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

    AddressEntity findByAddressId(String addressID);
    AddressEntity findById(long addressID);

}
