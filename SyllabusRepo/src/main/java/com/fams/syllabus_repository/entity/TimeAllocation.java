package com.fams.syllabus_repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "timeAllocation")
public class TimeAllocation extends BaseEntity{
    @Column(name = "assignment", nullable = false)
    private int assignment;
    @Column(name = "concept", nullable = false)
    private int concept;
    @Column(name = "gulde", nullable = false)
    private int gulde;
    @Column(name = "test", nullable = false)
    private int test;
    @Column(name = "exam", nullable = false)
    private int exam;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "syllabus_id", referencedColumnName = "id")
    private Syllabus syllabus;
}
