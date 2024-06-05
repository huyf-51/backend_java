package com.nam.controller;

import com.nam.exception.TuitionException;
import com.nam.exception.UserException;
import com.nam.repository.EmployeeRepository;
import com.nam.repository.LunchRepository;
import com.nam.service.EmployeeService;
import com.nam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lunch")
@RequiredArgsConstructor
public class LunchController {

    private final LunchRepository lunchRepository;

    private final EmployeeRepository employeeRepository;

    private final EmployeeService employeeService;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createTuition(@RequestHeader("Authorization") String jwt) throws UserException, TuitionException {
        employeeService.registerLunch(userService.findUserProfileByJwt(jwt).getId());

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

}