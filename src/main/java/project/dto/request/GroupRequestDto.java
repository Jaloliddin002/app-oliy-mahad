package project.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GroupRequestDto {

    private String name ;

    private int membersCount ;

    private String gender;

    private LocalDate startDate;

    private long courseId;


}
