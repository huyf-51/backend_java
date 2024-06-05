package com.nam.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @PostMapping("/report")
    public void receiveViolationReport(@RequestBody String violationReport) {
        // Log the violation report
        System.out.println("Received violation report:\n" + violationReport);
        // You can perform additional processing or logging here
    }
}