package com.develop.web_server.service.impl;

import com.develop.web_server.io.entity.AddressEntity;
import com.develop.web_server.io.repository.AddressRepository;
import com.develop.web_server.service.AddressService;
import com.develop.web_server.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Override
    public AddressDto getAddressByAddressId(String id) {
        AddressEntity addressEntity = addressRepository.findByAddressId(id);
        if (addressEntity != null) {
            ModelMapper modelMapper = new ModelMapper();
            AddressDto addressDto = modelMapper.map(addressEntity, AddressDto.class);
            return addressDto;
        }
        return null;
    }
}
