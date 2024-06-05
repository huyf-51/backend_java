package com.nam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "id")
@SuperBuilder
public class Employee extends User {

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "salaries")
    private List<Salary> salaries = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "attendance")
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "lunch")
    private List<Lunch> lunches = new ArrayList<>();

    private double monthSalary;

    public Attendance getAttendanceByDay(int day) {
        for (Attendance attendance : attendances) {
            if (attendance.getAttendanceDay().getDayOfMonth() == day) {
                return attendance;
            }
        }
        return null;
    }

    public Salary getSalaryByMonth(int month) {
        for (Salary salary : salaries) {
            if (salary.getPayAt().getDayOfMonth() == month) {
                return salary;
            }
        }
        return null;
    }

    public int getPresentByMonth(int monthValue) {
        int dayPresent = 0;
        for (Attendance attendance : attendances) {
            if (attendance.getAttendanceDay().getMonthValue() == monthValue) {
                dayPresent++;
            }
        }
        return dayPresent;
    }
}