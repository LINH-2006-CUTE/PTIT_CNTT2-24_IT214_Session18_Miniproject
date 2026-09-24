package com.rikkeibank.customer.service;

import com.rikkeibank.customer.dto.StaffRequest;
import com.rikkeibank.customer.dto.StaffResponse;
import com.rikkeibank.customer.entity.CommonStatus;
import com.rikkeibank.customer.entity.Staff;
import com.rikkeibank.customer.exception.ApiException;
import com.rikkeibank.customer.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffResponse createStaff(StaffRequest request) {
        String generatedCode = "STF" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Staff staff = Staff.builder()
                .userId(request.getUserId())
                .staffCode(generatedCode)
                .fullName(request.getFullName())
                .department(request.getDepartment())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .status(request.getStatus() != null ? request.getStatus() : CommonStatus.ACTIVE)
                .build();

        Staff saved = staffRepository.save(staff);
        return mapToResponse(saved);
    }

    public List<StaffResponse> getAllStaffs() {
        return staffRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public StaffResponse getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay nhan vien voi ID: " + id));
        return mapToResponse(staff);
    }

    public StaffResponse updateStaff(Long id, StaffRequest request) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay nhan vien voi ID: " + id));

        staff.setFullName(request.getFullName());
        staff.setDepartment(request.getDepartment());
        staff.setPhoneNumber(request.getPhoneNumber());
        staff.setEmail(request.getEmail());
        if (request.getStatus() != null) {
            staff.setStatus(request.getStatus());
        }

        Staff saved = staffRepository.save(staff);
        return mapToResponse(saved);
    }

    public void deleteStaff(Long id) {
        if (!staffRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Khong tim thay nhan vien voi ID: " + id);
        }
        staffRepository.deleteById(id);
    }

    public StaffResponse mapToResponse(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .userId(staff.getUserId())
                .staffCode(staff.getStaffCode())
                .fullName(staff.getFullName())
                .department(staff.getDepartment())
                .phoneNumber(staff.getPhoneNumber())
                .email(staff.getEmail())
                .status(staff.getStatus())
                .createdAt(staff.getCreatedAt())
                .build();
    }
}
