package com.company;

import com.company.dto.profile.ProfileDTO;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YouTubeApplicationTests {
    @Autowired
    private ProfileService profileService;

    @Test
    void createProfile() {
        ProfileDTO profile = new ProfileDTO();
        profile.setName("Adminjon");
        profile.setSurname("Adminjonov");
        profile.setEmail("admin@mail.tu");
        profile.setPhone("+998914640908");
        profile.setPassword(MD5Util.getMd5("123"));
        profile.setAttachId("64c24aa1-eddd-4417-9c1e-2c2f38489fd1");
        profile.setRole(ProfileRole.ROLE_ADMIN);
        profileService.create(profile);
    }

}
