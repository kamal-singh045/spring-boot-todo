package springboot_todo.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot_todo.todo.enums.RoleEnum;
import springboot_todo.todo.enums.UserStatusEnum;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty
    private UUID id;

    @Column(nullable = false)
    @JsonProperty
    private String name;

    @Column(nullable = false, unique = true)
    @JsonProperty
    private String email;

    @Column(nullable = false)
    @JsonProperty
    @Enumerated(EnumType.STRING)
    private RoleEnum role = RoleEnum.USER;

    @Column(nullable = false)
    @JsonProperty
    private String password;

    @Column(nullable = false)
    @JsonProperty
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status = UserStatusEnum.PENDING;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // we have a user property in TodoEntity class
    @JsonIgnore
    List<TodoEntity> todos;
}
