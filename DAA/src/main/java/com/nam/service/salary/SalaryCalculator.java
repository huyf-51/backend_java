package com.nam.service.salary;

import org.springframework.stereotype.Component;

@Component
public class SalaryCalculator {
    public static double calculateNetSalary(double grossSalary) {
        double bhxh = grossSalary * 0.08;
        double bhyt = grossSalary * 0.015;
        double bhtn = grossSalary * 0.01;
        double totalInsurance = bhxh + bhyt + bhtn;

        double taxableIncome = grossSalary - totalInsurance - 11000000; // Giảm trừ gia cảnh cá nhân 11 triệu

        double tncn = calculateTNCN(taxableIncome);

        return grossSalary - totalInsurance - tncn;
    }

    public static double calculateTNCN(double taxableIncome) {
        double tncn = 0;

        if (taxableIncome <= 0) return 0;

        if (taxableIncome <= 5000000) {
            tncn = taxableIncome * 0.05;
        } else if (taxableIncome <= 10000000) {
            tncn = 5000000 * 0.05 + (taxableIncome - 5000000) * 0.1;
        } else if (taxableIncome <= 18000000) {
            tncn = 5000000 * 0.05 + 5000000 * 0.1 + (taxableIncome - 10000000) * 0.15;
        } else if (taxableIncome <= 32000000) {
            tncn = 5000000 * 0.05 + 5000000 * 0.1 + 8000000 * 0.15 + (taxableIncome - 18000000) * 0.2;
        } else if (taxableIncome <= 52000000) {
            tncn = 5000000 * 0.05 + 5000000 * 0.1 + 8000000 * 0.15 + 14000000 * 0.2 + (taxableIncome - 32000000) * 0.25;
        } else if (taxableIncome <= 80000000) {
            tncn = 5000000 * 0.05 + 5000000 * 0.1 + 8000000 * 0.15 + 14000000 * 0.2 + 20000000 * 0.25 + (taxableIncome - 52000000) * 0.3;
        } else {
            tncn = 5000000 * 0.05 + 5000000 * 0.1 + 8000000 * 0.15 + 14000000 * 0.2 + 20000000 * 0.25 + 28000000 * 0.3 + (taxableIncome - 80000000) * 0.35;
        }

        return tncn;
    }
}
