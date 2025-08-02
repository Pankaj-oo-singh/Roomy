package roomy.dto;


import lombok.Data;
import roomy.entities.enums.Role;

import java.util.Set;

@Data
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private Set<Role> roles;
}
