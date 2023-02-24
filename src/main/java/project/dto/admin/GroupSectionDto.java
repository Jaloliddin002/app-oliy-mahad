package project.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupSectionDto {


    private Long id;

    private String name;

    private long membersCount;

    private LocalDate startDate;

    private Long courseId;
}
