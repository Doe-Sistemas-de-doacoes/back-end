package com.labes.doe.dto;

import com.labes.doe.model.enumeration.Profile;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserTokenDTO {
    private Integer id;
    private String user;
    private String name;
    private String profile;
    private String token;

    public void setProfile(Integer profile) {
        this.profile = Profile.toEnum(profile).getRole();
    }
}
