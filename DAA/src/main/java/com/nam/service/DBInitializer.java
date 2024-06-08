package com.nam.service;

import com.nam.model.*;
import com.nam.repository.AttendanceRepository;
import com.nam.repository.NoticeRepository;
import com.nam.repository.RoleRepository;
import com.nam.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DBInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    private final NoticeRepository noticeRepository;

    @Value("${admin.pass}")
    String adminPass;

    public static LocalDateTime generateRandomDateTime(int startYear, int endYear) {
        Random random = new Random();
        long minDay = LocalDateTime.of(startYear, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long maxDay = LocalDateTime.of(endYear, 12, 31, 23, 59).toEpochSecond(ZoneOffset.UTC);
        long randomDay = minDay + random.nextInt((int) (maxDay - minDay));

        return LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
    }

    @PostConstruct
    @Transactional
    public void init() throws IOException {
        Role roleEmployee = new Role(1L, ERole.ROLE_EMPLOYEE);
        Role roleAdmin = new Role(2L, ERole.ROLE_ADMIN);

        roleRepository.saveAll(Arrays.asList(roleEmployee, roleAdmin));

        Set<Role> roles = new HashSet<>();
        roles.add(roleAdmin);
        User admin = User.builder()
                .email("admin@gmail.com")
                .password(adminPass)
                .lastName("admin")
                .roles(roles)
                .build();

        userRepository.save(admin);

        roles = new HashSet<>();
        roles.add(roleEmployee);

        List<Employee> employees = new ArrayList<>();
        List<Attendance> attendances = new ArrayList<>();
        List<Salary> salaries = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Employee employee = Employee.builder()
                    .email(i + "@gmail.com")
                    .password("$2a$10$1pZ71ThzFMamlhh.R3VEKuE3HranTpiTJePMAYLoX7TzEk4FTEPo6")
                    .firstName(Integer.toString(i))
                    .lastName(Integer.toString(i))
                    .roles(roles)
                    .monthSalary(20_000_000L)
                    .build();

            Employee savedEmployee = userRepository.save(employee);
            employees.add(savedEmployee);

            for (int j = 0; j < 2; j++) {
                Attendance attendance = Attendance.builder()
                        .attendanceDay(generateRandomDateTime(2022, 2024))
                        .attendanceStatus(EAttend.PRESENT)
                        .employee(savedEmployee)
                        .build();

                attendances.add(attendance);
            }
        }

        attendanceRepository.saveAll(attendances);

        Notice notice = Notice.builder()
                .title("Chính Sách Làm Việc Từ Xa Mới")
                .description("Bắt đầu từ ngày 01/07/2024, công ty sẽ áp dụng chính sách làm việc từ xa cho các bộ phận phù hợp. Nhân viên có thể đăng ký làm việc từ xa thông qua hệ thống quản lý nhân sự. Chi tiết về chính sách mới có thể xem tại trang nội bộ.")
                .createdAt(LocalDateTime.now())
                .build();

        noticeRepository.save(notice);

        Notice notice1 = Notice.builder()
                .title("Cập Nhật Phần Mềm Quản Lý Nhân Sự")
                .description("Hệ thống phần mềm quản lý nhân sự sẽ được nâng cấp vào ngày 01/06/2024. Việc nâng cấp sẽ kéo dài từ 22h00 ngày 01/06 đến 06h00 ngày 02/06. Trong thời gian này, hệ thống sẽ không khả dụng. Mong mọi người thông cảm và chuẩn bị trước.")
                .createdAt(LocalDateTime.now())
                .build();

        noticeRepository.save(notice1);
    }
}
