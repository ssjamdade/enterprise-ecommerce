package com.ecommerce.address.controller;

import com.ecommerce.address.dto.AddressResponse;
import com.ecommerce.address.dto.CreateAddressRequest;
import com.ecommerce.address.dto.UpdateAddressRequest;
import com.ecommerce.address.service.AddressService;
import com.ecommerce.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> create(
            @Valid @RequestBody CreateAddressRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<AddressResponse>builder()
                        .success(true)
                        .message("Address created successfully.")
                        .data(addressService.create(request))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAddressRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<AddressResponse>builder()
                        .success(true)
                        .message("Address updated successfully.")
                        .data(addressService.update(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id) {

        addressService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Address deleted successfully.")
                        .build()
        );
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse<Void>> setDefault(
            @PathVariable Long id) {

        addressService.setDefault(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Default address updated successfully.")
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses() {

        return ResponseEntity.ok(
                ApiResponse.<List<AddressResponse>>builder()
                        .success(true)
                        .message("Addresses fetched successfully.")
                        .data(addressService.getMyAddresses())
                        .build()
        );
    }
}
