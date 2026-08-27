package com.ecommerce.address.service.impl;

import com.ecommerce.address.dto.AddressResponse;
import com.ecommerce.address.dto.CreateAddressRequest;
import com.ecommerce.address.dto.UpdateAddressRequest;
import com.ecommerce.address.entity.AddressEntity;
import com.ecommerce.address.repository.AddressRepo;
import com.ecommerce.address.service.AddressService;
import com.ecommerce.auth.entity.UserEntity;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.mapper.AddressMapper;
import com.ecommerce.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepository;
    private final AddressMapper addressMapper;
    private final SecurityUtils securityUtils;

    @Override
    public AddressResponse create(CreateAddressRequest request) {

        UserEntity user = securityUtils.getCurrentUser();

        AddressEntity address = addressMapper.toEntity(request);
        address.setUser(user);

        boolean makeDefault =
                Boolean.TRUE.equals(request.getIsDefault())
                        || !addressRepository.existsByUserId(user.getId());

        if (makeDefault) {
            clearDefaultAddress(user.getId());
        }

        address.setIsDefault(makeDefault);

        return addressMapper.toResponse(
                addressRepository.save(address)
        );
    }

    @Override
    public AddressResponse update(
            Long id,
            UpdateAddressRequest request) {

        UserEntity user = securityUtils.getCurrentUser();

        AddressEntity address = addressRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found."));

        addressMapper.updateEntity(request, address);

        return addressMapper.toResponse(address);
    }

    @Override
    public void delete(Long id) {

        UserEntity user = securityUtils.getCurrentUser();

        AddressEntity address = addressRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found."));

        addressRepository.delete(address);
    }

    @Override
    public void setDefault(Long id) {

        UserEntity user = securityUtils.getCurrentUser();

        AddressEntity address = addressRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found."));

        clearDefaultAddress(user.getId());

        address.setIsDefault(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getMyAddresses() {

        UserEntity user = securityUtils.getCurrentUser();

        return addressRepository
                .findByUserIdOrderByIsDefaultDescCreatedAtDesc(user.getId())
                .stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    private void clearDefaultAddress(Long userId) {

        addressRepository
                .findByUserIdOrderByIsDefaultDescCreatedAtDesc(userId)
                .forEach(address -> address.setIsDefault(false));
    }
}