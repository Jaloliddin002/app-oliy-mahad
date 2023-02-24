package project.model.entity.group;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.audit.Auditable;
import project.model.entity.course.CourseEntity;
import project.model.entity.user.UserEntity;
import project.model.enums.GroupStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "group_entity")
public class GroupEntity extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int memberCount;

    @Enumerated(EnumType.STRING)
    private GroupStatusEnum groupStatus;

    private LocalDateTime startDate;

    @ManyToOne
    private CourseEntity course;

    @OneToMany(fetch = FetchType.EAGER)
    private List<UserEntity> userEntities;
}
