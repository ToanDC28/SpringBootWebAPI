package com.example.UserRepo.entity;

//import com.ctc.wstx.shaded.msv_core.datatype.xsd.TokenType;
import com.example.UserRepo.enums.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;
    private String email;
    @Column(length = 2000, nullable = false)
    private String token;
    @Column(nullable = false)
    private Integer duration;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeToken type;
}
