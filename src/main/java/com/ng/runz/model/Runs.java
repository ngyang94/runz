package com.ng.runz.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Runs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Double miles;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users user;

    @OneToOne(cascade = CascadeType.ALL)
    Coordinate startPoint;
    @OneToOne(cascade = CascadeType.ALL)
    Coordinate endPoint;

    public Runs(LocalDateTime startTime, LocalDateTime endTime, Double miles, Coordinate startPoint, Coordinate endPoint) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.miles = miles;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
}
