package org.example.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Username must be nique, not null

    private String displayName;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> friendIds = new ArrayList<>();
    // List with ID-er for friends
}
