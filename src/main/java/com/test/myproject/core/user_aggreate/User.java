package com.test.myproject.core.user_aggreate;

import com.test.myproject.core.role_aggreate.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grastore_user", indexes = {
        @Index(columnList = "fullname", name = "user_fullname_idx")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull()
    @Column(length = 100)
    private String fullname;
    @NotNull()
    @Column()
    private String email;
    @NotNull()
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
}
