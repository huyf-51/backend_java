package com.nam.service;

import com.nam.exception.UserException;
import com.nam.model.Employee;
import com.nam.model.Salary;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    public Page<Employee> getEmployeeListPage(Integer pageNumber, Integer pageSize);

    public List<Employee> saveAllEmployee(List<Employee> employees);

    public void createSalaryForAll();

    public Employee createSalary(Salary salary, Long employeeId);

    public Employee updateSalary(Long employeeId, double monthSalary) throws UserException;

    public void attend(Long employeeId) throws UserException;

    public void setAllAbsent();

    public void registerLunch(Long employeeId) throws UserException;
}
