package com.fams.syllabus_repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TrainingMaterials")
public class TrainingMaterials extends BaseEntity{

    @Column(name = "title")
    private String title;

    private String s3Location;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentId")
    private TrainingContent trainingContent;

    @Override
    public String toString() {
        return "TrainingMaterials{" +
                "title='" + title + '\'' +
                ", s3Location='" + s3Location + '\'' +
                ", data=" + Arrays.toString(data) +
                ", trainingContent=" + (trainingContent != null ? trainingContent.getId() : "null") + // Example: Avoid circular reference
                '}';
    }
}

//@Column(name = "createdBy",nullable = false, length = 500)
//private String createdBy;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "materialsId")
//    private Long materialsId;

