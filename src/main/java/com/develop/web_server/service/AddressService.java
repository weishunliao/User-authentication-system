package com.develop.web_server.service;

import com.develop.web_server.shared.dto.AddressDto;

public interface AddressService {
    AddressDto getAddressByAddressId(String id);
}
