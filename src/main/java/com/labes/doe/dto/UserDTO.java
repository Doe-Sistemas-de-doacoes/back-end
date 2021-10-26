package com.labes.doe.dto;

import com.labes.doe.model.enumeration.Profile;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String user;
    private String name;
    private String profile;
    private List<AddressDTO> address;

    public void setProfile(Integer profile) {
        this.profile = Profile.toEnum(profile).getRole();
    }
}
