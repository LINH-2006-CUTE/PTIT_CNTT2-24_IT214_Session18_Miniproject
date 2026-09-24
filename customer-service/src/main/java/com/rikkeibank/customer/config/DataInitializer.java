package com.rikkeibank.customer.config;

import com.rikkeibank.customer.entity.CommonStatus;
import com.rikkeibank.customer.entity.Customer;
import com.rikkeibank.customer.entity.Staff;
import com.rikkeibank.customer.repository.CustomerRepository;
import com.rikkeibank.customer.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0) {
            customerRepository.save(Customer.builder()
                    .userId(3L)
                    .cifCode("CIF00000001")
                    .fullName("Nguyen Van A")
                    .identityCard("001200000001")
                    .phoneNumber("0988000333")
                    .email("nguyenvana@gmail.com")
                    .address("123 Duong Le Duan, Quan 1, TP Ho Chi Minh")
                    .status(CommonStatus.ACTIVE)
                    .build());

            customerRepository.save(Customer.builder()
                    .userId(4L)
                    .cifCode("CIF00000002")
                    .fullName("Tran Thi B")
                    .identityCard("001200000002")
                    .phoneNumber("0988000444")
                    .email("tranthib@gmail.com")
                    .address("456 Duong Nguyen Hue, Quan 1, TP Ho Chi Minh")
                    .status(CommonStatus.ACTIVE)
                    .build());
        }

        if (staffRepository.count() == 0) {
            staffRepository.save(Staff.builder()
                    .userId(2L)
                    .staffCode("STF00001")
                    .fullName("Giao Dich Vien")
                    .department("Quay giao dich")
                    .phoneNumber("0988000222")
                    .email("teller@rikkeibank.com")
                    .status(CommonStatus.ACTIVE)
                    .build());
        }
    }
}
