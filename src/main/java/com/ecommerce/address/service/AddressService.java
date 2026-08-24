package com.ecommerce.address.service;

import com.ecommerce.address.dto.AddressResponse;
import com.ecommerce.address.dto.CreateAddressRequest;
import com.ecommerce.address.dto.UpdateAddressRequest;

import java.util.List;

public interface AddressService {

    AddressResponse create(CreateAddressRequest request);

    AddressResponse update(Long id, UpdateAddressRequest request);

    void delete(Long id);

    void setDefault(Long id);

    List<AddressResponse> getMyAddresses();
}