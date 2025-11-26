package com.theworkers.usermicroservice.model;

import com.theworkers.usermicroservice.model.enums.UserStatus;
import com.theworkers.usermicroservice.util.EncryptDecryptConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String lastName;
    private String middleName;
    @Convert(converter = EncryptDecryptConverter.class)
    @Column(name = "employee_number")
    private String employeeNumber;
    private String email;
    private String password;
    private String eccPublicKey;
    private UserStatus status;

    @Column(columnDefinition = "TEXT")
    private String publicKey;

    @Column(columnDefinition = "TEXT")
    private String privateKey;

    @Column(name = "role_id")
    private Long roleId;

    @Transient
    private String token;

}

