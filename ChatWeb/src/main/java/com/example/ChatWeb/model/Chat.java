package com.example.ChatWeb.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Chat {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Integer id;
    private String chatname;
    private String chatimage;
    @ManyToMany
    private Set<User> admins=new HashSet<>();
    @Column(name="is_group")
    private boolean isGroup;
    @JoinColumn(name="created_by")
    @ManyToOne
    private User createdBy;
    @ManyToMany
    private Set<User> users=new HashSet<>();
    @OneToMany
    private List<Message> messages=new ArrayList<>();
}
