package com.nam.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nam.exception.UserException;
import com.nam.model.Employee;
import com.nam.model.Salary;
import com.nam.payload.response.ApiResponse;
import com.nam.service.EmployeeService;
import com.nam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UserService userService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Employee>> getAllNotice(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {

        Page<Employee> employees = employeeService.getEmployeeListPage(pageNumber, pageSize);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> deleteUser(@PathVariable Long employeeId) throws UserException {
        List<Employee> employees = userService.deleteUser(employeeId);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping("/attend")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> attend(@RequestHeader("Authorization") String jwt) throws UserException {
        employeeService.attend(userService.findUserProfileByJwt(jwt).getId());

        ApiResponse res = ApiResponse.builder().
                message("Attend employee with id: ").status(true).build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/create/salary/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createSalary(@PathVariable Long employeeId, @RequestBody Salary salary) throws UserException {
        employeeService.createSalary(salary, employeeId);

        ApiResponse res = ApiResponse.builder().
                message("Create salary for employee with id: " + employeeId).status(true).build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/create/salary/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createSalaryForAll() throws UserException {
        employeeService.createSalaryForAll();

        ApiResponse res = ApiResponse.builder().
                message("Create salary for all employee").status(true).build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/update/salary/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateSalary(@PathVariable Long employeeId, @RequestParam double monthSalary) throws UserException {
        employeeService.updateSalary(employeeId, monthSalary);

        ApiResponse res = ApiResponse.builder().
                message("Update salary for employee").status(true).build();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            List<Employee> entities = readJsonAndMapToEntities(file);

            employeeService.saveAllEmployee(entities);

            return "File uploaded successfully";
        } catch (Exception e) {
            return "Error uploading file: " + e.getMessage();
        }
    }

    private List<Employee> readJsonAndMapToEntities(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Employee>> typeReference = new TypeReference<>() {
        };
        return objectMapper.readValue(file.getInputStream(), typeReference);
    }
}