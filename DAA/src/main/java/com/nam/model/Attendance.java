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
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    @JsonIgnore
    private Employee employee;

    private LocalDateTime attendanceDay;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EAttend attendanceStatus;
}