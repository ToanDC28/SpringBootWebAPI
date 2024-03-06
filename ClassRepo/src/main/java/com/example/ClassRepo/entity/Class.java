package com.example.ClassRepo.entity;

import com.example.ClassRepo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name="class")

public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer class_id;
    private String className;
    private String createBy;
    private Date createDate;
    private String modifyBy;
    private Date modifyDate;
    private Status status;
    @JoinColumn(name = "location_id")
    @OneToOne
    private Location location;
    private String classCode;
    private Date startDate;
    private Date endDate;
    private String approveBy;
    private Date approveDate;
    private Integer admin_id;
    @JoinColumn(name = "fsu_id")
    @OneToOne
    private FSU fsu;
    @JoinColumn(name = "slot_id")
    @ManyToOne
    private Slot classTime;
    private Integer durationInDays;
    @Column(columnDefinition = "json")
    private String schedule;

}
