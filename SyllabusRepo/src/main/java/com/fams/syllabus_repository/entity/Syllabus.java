package com.fams.syllabus_repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "syllabus")
public class Syllabus extends BaseEntity{

    @Column(name = "syllabusName", nullable = false)
    private String syllabusName; // syllabus name
    @Column(name = "syllabusCode", nullable = false)
    private String syllabusCode;// technical requirement
    @Column(name = "duration", nullable = false)
    private int duration;
    @Column(name = "version", nullable = false)
    private String version;
    @Column(name = "attendeeNumber")
    private int attendeeNumber;
    @Column(name = "technicalRequirement", columnDefinition = "TEXT")
    private String technicalRequirement;
    @Column(name = "courseObjectives", columnDefinition = "TEXT")
    private String courseObjectives;
    @Column(name = "level", nullable = false)
    private String level;
    @Column(name = "status", nullable = false)
    public String publicStatus;

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DaySyllabus> daSyllabus = new ArrayList<>();

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OtherSyllabus> otherSyllabi = new ArrayList<>();

    @OneToOne(mappedBy = "syllabus")
    private TimeAllocation timeAllocation;

    @OneToOne(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private AssignmentSheme assignmentSheme;

}
