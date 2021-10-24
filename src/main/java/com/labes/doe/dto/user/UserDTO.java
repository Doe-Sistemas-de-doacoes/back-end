package com.labes.doe.dto.user;

import com.labes.doe.model.enumeration.Profile;
import lombok.*;

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

    public void setProfile(Integer profile) {
        this.profile = Profile.toEnum(profile).getRole();
    }
}
