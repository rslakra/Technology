package com.rslakra.thymeleafsidebarsversion.setting.controller;

import com.rslakra.thymeleafsidebarsversion.framework.controller.AbstractController;
import com.rslakra.thymeleafsidebarsversion.setting.service.ProfileService;
import com.rslakra.thymeleafsidebarsversion.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/{version}/settings")
public class SettingController extends AbstractController {

    private final SettingService settingService;
    private final ProfileService profileService;

    /**
     * @param settingService
     * @param profileService
     */
    @Autowired
    public SettingController(final SettingService settingService, final ProfileService profileService) {
        this.settingService = settingService;
        this.profileService = profileService;
    }

    /**
     * @param version the version path variable
     * @param principal
     * @return
     */
    @GetMapping
    public String accountSettingsIndex(@PathVariable("version") String version, Principal principal) {
//        return (Objects.nonNull(principal) ? version + "/home/signedIn" : version + "/home/notSignedIn");
        return version + "/setting/index";
    }

}
