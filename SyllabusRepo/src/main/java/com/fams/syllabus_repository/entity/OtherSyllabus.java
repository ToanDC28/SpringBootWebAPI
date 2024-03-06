package com.fams.syllabus_repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OtherSyllabus")
public class OtherSyllabus extends BaseEntity{

    private String training;
    private String reTest;
    private String marking;
    private String waiverCriteria;
    private String other;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabusId")
    private Syllabus syllabus;
}
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
