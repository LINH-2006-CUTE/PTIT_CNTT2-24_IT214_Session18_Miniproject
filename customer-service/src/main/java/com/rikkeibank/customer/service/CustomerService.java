package com.rikkeibank.customer.service;

import com.rikkeibank.customer.dto.CustomerRequest;
import com.rikkeibank.customer.dto.CustomerResponse;
import com.rikkeibank.customer.entity.CommonStatus;
import com.rikkeibank.customer.entity.Customer;
import com.rikkeibank.customer.exception.ApiException;
import com.rikkeibank.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByIdentityCard(request.getIdentityCard())) {
            throw new ApiException(HttpStatus.CONFLICT, "Can cuoc cong dan da ton tai trong he thong");
        }

        String generatedCif = "CIF" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Customer customer = Customer.builder()
                .userId(request.getUserId())
                .cifCode(generatedCif)
                .fullName(request.getFullName())
                .identityCard(request.getIdentityCard())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .status(request.getStatus() != null ? request.getStatus() : CommonStatus.ACTIVE)
                .build();

        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang voi ID: " + id));
        return mapToResponse(customer);
    }

    public CustomerResponse getCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang voi User ID: " + userId));
        return mapToResponse(customer);
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang voi ID: " + id));

        customer.setFullName(request.getFullName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        if (request.getStatus() != null) {
            customer.setStatus(request.getStatus());
        }

        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay khach hang voi ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    public CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .userId(customer.getUserId())
                .cifCode(customer.getCifCode())
                .fullName(customer.getFullName())
                .identityCard(customer.getIdentityCard())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
