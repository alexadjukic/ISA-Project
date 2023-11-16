package com.team65.isaproject.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "admin_name")
    private String adminName;
    @Column(name = "admin_lastname")
    private String adminLastname;
    @Column(name = "appointment_date_time")
    private LocalDateTime dateTime;
    @Column(name = "duration")
    private double duration;
    @Column(name = "status")
    private AppointmentStatus status;

    public Appointment() {
    }

    public Appointment(Integer id, String adminName, String adminLastname, LocalDateTime dateTime, double duration) {
        this.id = id;
        this.adminName = adminName;
        this.adminLastname = adminLastname;
        this.dateTime = dateTime;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminLastname() {
        return adminLastname;
    }

    public void setAdminLastname(String adminLastname) {
        this.adminLastname = adminLastname;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}