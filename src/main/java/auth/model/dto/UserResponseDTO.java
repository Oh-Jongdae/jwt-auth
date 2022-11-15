package auth.model.dto;

import auth.model.UserEntityRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {

  @ApiModelProperty(position = 3)
  List<UserEntityRole> userEntityRoles;
  @ApiModelProperty
  private Integer id;
  @ApiModelProperty(position = 1)
  private String username;
  @ApiModelProperty(position = 2)
  private String email;

}
