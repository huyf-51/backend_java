package com.nam.service;

import com.nam.exception.UserException;
import com.nam.model.*;
import com.nam.repository.*;
import com.nam.service.salary.SalaryCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final SalaryRepository salaryRepository;
    private final LunchRepository lunchRepository;

    @Override
    public Page<Employee> getEmployeeListPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findAllWithPagination(pageable);
    }

    @Override
    public List<Employee> saveAllEmployee(List<Employee> employees) {
        return employeeRepository.saveAll(employees);
    }


    private boolean checkYear(int year) {

        return (((year % 4 == 0) && (year % 100 != 0)) ||
                (year % 400 == 0));
    }

    @Override
    public void createSalaryForAll() {

        //List<Employee> employees = new ArrayList<>();


        for (Employee employee : employeeRepository.findAll()) {
            LocalDateTime time = LocalDateTime.now();
            Salary existSalary = employee.getSalaryByMonth(time.getMonthValue());


            if (existSalary == null) {
                int dayPresent = employee.getPresentByMonth(time.getMonthValue());
                int lengthOfMonth = time.getMonth().length(checkYear(time.getYear()));
                int numberOfLeaveDays = lengthOfMonth - dayPresent;

                Salary salary = Salary.builder()
                        .payAt(time)
                        .month(time.getMonthValue())
                        .year(time.getYear())
                        .grossSalary(employee.getMonthSalary())
                        .netSalary(SalaryCalculator.calculateNetSalary(employee.getMonthSalary() - numberOfLeaveDays * 200000.0))
                        .incomeTax(SalaryCalculator.calculateTNCN((double) ((employee.getMonthSalary() - numberOfLeaveDays * 200000.0)) * 0.895 - 11000000))
                        .numberOfLeaveDays(numberOfLeaveDays)
                        .employee(employee)
                        .build();

                salaryRepository.save(salary);
            }
        }
    }

    @Override
    public Employee createSalary(Salary salary, Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            salary.setEmployee(employee.get());
            // salary.setTotalSalary(employee.get().getBaseSalary());
            salaryRepository.save(salary);
        }
        return employee.get();
    }

    @Override
    public Employee updateSalary(Long employeeId, double monthSalary) throws UserException {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isEmpty()) {
            throw new UserException("Not found employee");
        } else {
            employee.get().setMonthSalary(monthSalary);
        }
        return userRepository.save(employee.get());
    }

    @Override
    public void attend(Long employeeId) throws UserException {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isEmpty()) {
            throw new UserException("Not found employee");
        } else {
            employee.ifPresent(emp -> {
                boolean isTodayAttendance = emp.getAttendances().stream()
                        .map(Attendance::getAttendanceDay)
                        .anyMatch(day -> day.getDayOfYear() == LocalDateTime.now().getDayOfYear());

                if (isTodayAttendance) {
                    try {
                        throw new UserException("You already attend today");
                    } catch (UserException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            EAttend status = null;
            if (LocalDateTime.now().getHour() > 8) {
                status = EAttend.LATE;
            } else {
                status = EAttend.PRESENT;
            }
            Attendance attendance = Attendance.builder()
                    .attendanceDay(LocalDateTime.now())
                    .attendanceStatus(status)
                    .employee(employee.get())
                    .build();

            attendanceRepository.save(attendance);
        }
    }

    @Override
    public void setAllAbsent() {
        List<Employee> employeeList = employeeRepository.findAll();
        int day = LocalDateTime.now().getDayOfMonth();
        for (Employee employee : employeeList) {
            Attendance attendance = employee.getAttendanceByDay(day);
            if (attendance == null) {
                attendance.setAttendanceStatus(EAttend.ABSENT);
                attendanceRepository.save(attendance);
            }
        }
    }

    @Override
    public void registerLunch(Long employeeId) throws UserException {
        if (lunchRepository.existsLunchByEmployeeAndMonthAndYear(employeeId, LocalDateTime.now().getMonthValue(), LocalDateTime.now().getYear())) {
            throw new UserException("You already registerd lunch this month");
        }
        Lunch lunch = Lunch.builder()
                .employee(employeeRepository.findById(employeeId).get())
                .totalPay(5000)
                .month(LocalDateTime.now().getMonthValue())
                .year(LocalDateTime.now().getYear())
                .build();


        lunchRepository.save(lunch);
    }

}
