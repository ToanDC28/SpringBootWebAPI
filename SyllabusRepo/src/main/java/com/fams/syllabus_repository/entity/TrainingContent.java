package com.fams.syllabus_repository.entity;

import com.fams.syllabus_repository.enums.DeliveryType;
import com.fams.syllabus_repository.enums.OutputStandard;
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
@Table(name = "TrainingContent")
public class TrainingContent extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "outputStandard")
    @ElementCollection(targetClass = OutputStandard.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "training_content_output_standards", joinColumns = @JoinColumn(name = "content_id"))
    private List<OutputStandard> outputStandard;
    @Column(name = "trainingTime")
    private int trainingTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "deliveryType")
    private DeliveryType deliveryType;
    @Column(name = "method", nullable = false)
    private String method;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private TrainingUnit trainingUnit;

    @OneToMany(mappedBy = "trainingContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingMaterials> trainingMaterials = new ArrayList<>();

    @Override
    public String toString() {
        return "TrainingContent{" +
                "name='" + name + '\'' +
                ", outputStandard=" + outputStandard +
                ", trainingTime=" + trainingTime +
                ", deliveryType=" + deliveryType +
                ", method='" + method + '\'' +
                ", trainingUnit=" + (trainingUnit != null ? trainingUnit.getId() : "null") + // Example: Avoid circular reference
                ", trainingMaterials=" + trainingMaterials.size() + // Example: Avoid calling toString on the whole list
                '}';
    }
}
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;

//private String unitCode;
/*    @OneToMany(mappedBy = "trainingContent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningObjective> learningObjectives = new ArrayList<>();*/


