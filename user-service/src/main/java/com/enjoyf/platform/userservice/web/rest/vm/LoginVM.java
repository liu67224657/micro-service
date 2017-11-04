package com.enjoyf.platform.userservice.web.rest.vm;

/**
 * Created by shuguangcao on 2017/3/23.
 */

import com.enjoyf.platform.userservice.config.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "登陆账号",required = true,position = 1)
    private String username;

    @NotNull
    @Size(min = 6, max = 20)
    @ApiModelProperty(value = "密码",required = true,position = 2)
    private String password;

    @ApiModelProperty(value = "记住我",allowableValues = "true,false",required = true,position = 3)
    private Boolean rememberMe;

    @ApiModelProperty(value = "标示",allowableValues = "www",required = true,position = 4)
    private String profileKey = "www";

    @ApiModelProperty(value = "登陆方式",allowableValues = "mobile",required = true,position = 5)
    private String loginDomain = "mobile";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(String profileKey) {
        this.profileKey = profileKey;
    }

    public String getLoginDomain() {
        return loginDomain;
    }

    public void setLoginDomain(String loginDomain) {
        this.loginDomain = loginDomain;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", rememberMe=" + rememberMe +
            ", profileKey='" + profileKey + '\'' +
            ", loginDomain='"+ loginDomain + '\'' +
            '}';
    }
}