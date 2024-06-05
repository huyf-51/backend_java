package com.nam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    @JsonIgnore
    private Employee employee;

    private double grossSalary;
    private double netSalary;
    private double incomeTax;
    
    private int numberOfLeaveDays;
    private LocalDateTime payAt;

    private int year;
    private int month;

    private String paymentStatus;
}



