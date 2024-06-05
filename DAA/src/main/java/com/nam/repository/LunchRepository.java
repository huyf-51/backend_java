package com.nam.repository;

import com.nam.model.Lunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LunchRepository extends JpaRepository<Lunch, Long> {
    @Query("SELECT COUNT(l) > 0 FROM Lunch l WHERE l.employee.id = :employeeId AND l.month = :month AND l.year = :year")
    boolean existsLunchByEmployeeAndMonthAndYear(@Param("employeeId") Long employeeId, @Param("month") int month, @Param("year") int year);
}
