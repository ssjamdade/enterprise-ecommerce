package com.ecommerce.mapper;

import com.ecommerce.address.dto.AddressResponse;
import com.ecommerce.address.dto.CreateAddressRequest;
import com.ecommerce.address.dto.UpdateAddressRequest;
import com.ecommerce.address.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AddressEntity toEntity(CreateAddressRequest request);

    AddressResponse toResponse(AddressEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "defaultAddress", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            UpdateAddressRequest request,
            @MappingTarget AddressEntity entity
    );
}