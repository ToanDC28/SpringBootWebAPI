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
@Table(name = "trainingUnit")
public class TrainingUnit extends BaseEntity{

    @Column(name = "unitName", nullable = false)
    private String unitName;
    @Column(name = "unitNumber", nullable = false)
    private String unitNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id")
    private DaySyllabus daySyllabus;

    @OneToMany(mappedBy = "trainingUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingContent> trainingContents = new ArrayList<>();
}
