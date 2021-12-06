package guru.springfamework.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerDTO {

    @ApiModelProperty(value = "This is the first name",
                        required = true) //This removes the note "optional" at the documentation
    private String firstname;
    private String lastname;

    private String customerUrl;
}
