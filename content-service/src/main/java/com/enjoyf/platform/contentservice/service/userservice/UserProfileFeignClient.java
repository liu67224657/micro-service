package com.enjoyf.platform.contentservice.service.userservice;

import com.enjoyf.platform.contentservice.service.userservice.domain.UserProfile;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ericliu on 2017/6/12.
 */
@FeignClient("user-service")
public interface UserProfileFeignClient {

    @GetMapping("/api/user-profiles/{id}")
    UserProfile findOne(@RequestParam(value = "id") Long uid);

    @GetMapping("/api/user-profiles/ids")
    List<UserProfile> findUserProfilesByUids(@RequestParam(value = "ids") Long[] uids);
}
