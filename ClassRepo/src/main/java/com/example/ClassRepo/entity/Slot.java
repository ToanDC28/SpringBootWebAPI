package com.example.ClassRepo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name="slot")
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slotId;
    private String classtime;
    // Many-to-one relationship with Schedule

}
