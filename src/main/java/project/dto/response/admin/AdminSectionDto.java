package project.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminSectionDto<T> {
    private List<String> headers;
    private List<T> body;
    private boolean visibility;
    private boolean delete;
    private boolean edit;
    private boolean info;
}
