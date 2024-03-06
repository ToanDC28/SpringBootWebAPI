package com.fams.syllabus_repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignmentSheme")
public class AssignmentSheme extends BaseEntity{
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @OneToOne
    @JoinColumn(name = "syllabus_id", referencedColumnName = "id")
    private Syllabus syllabus;
}
