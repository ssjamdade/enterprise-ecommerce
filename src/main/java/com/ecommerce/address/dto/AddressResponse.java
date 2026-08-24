package com.ecommerce.address.dto;

import com.ecommerce.address.entity.AddressEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private AddressEntity.AddressType addressType;
    private Boolean defaultAddress;
}