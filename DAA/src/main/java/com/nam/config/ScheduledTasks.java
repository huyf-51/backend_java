package com.nam.config;

import com.nam.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final EmployeeService employeeService;

    @Scheduled(cron = "0 0 20 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void runDailyAt8PM() {
        // Your code here
        System.out.println("This method runs every day at 8 PM to check for all employee absent.");

        employeeService.setAllAbsent();
    }
}
