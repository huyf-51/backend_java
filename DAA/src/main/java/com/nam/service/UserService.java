package com.nam.service;

import com.nam.exception.UserException;
import com.nam.model.Employee;
import com.nam.model.User;
import com.nam.payload.request.SignupEmployeeRequest;

import java.util.List;

public interface UserService {
    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;

    public List<Employee> deleteUser(Long id) throws UserException;

    public Employee createEmployee(SignupEmployeeRequest employeeRequest) throws UserException;

}