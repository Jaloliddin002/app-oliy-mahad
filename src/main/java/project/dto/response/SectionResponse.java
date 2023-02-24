package project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SectionResponse {

    private Long id;
    private String sectionName;
    private List<ContentDto> content;

}