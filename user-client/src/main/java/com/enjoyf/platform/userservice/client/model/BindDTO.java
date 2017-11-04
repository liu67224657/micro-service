/*
 * userservice API
 * userservice API documentation
 *
 * OpenAPI spec version: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.enjoyf.platform.userservice.client.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BindDTO
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-04-08T10:52:24.374+08:00")
public class BindDTO {
  @SerializedName("accountNo")
  private String accountNo = null;

  @SerializedName("appKey")
  private String appKey = null;

  @SerializedName("createdIp")
  private String createdIp = null;

  @SerializedName("extraParams")
  private Map<String, String> extraParams = new HashMap<String, String>();

  @SerializedName("icon")
  private String icon = null;

  @SerializedName("login")
  private String login = null;

  @SerializedName("loginDomain")
  private String loginDomain = null;

  @SerializedName("nick")
  private String nick = null;

  @SerializedName("password")
  private String password = null;

  @SerializedName("profileKey")
  private String profileKey = null;

  @SerializedName("profileNo")
  private String profileNo = null;

  public BindDTO accountNo(String accountNo) {
    this.accountNo = accountNo;
    return this;
  }

   /**
   * Get accountNo
   * @return accountNo
  **/
  @ApiModelProperty(required = true, value = "")
  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public BindDTO appKey(String appKey) {
    this.appKey = appKey;
    return this;
  }

   /**
   * Get appKey
   * @return appKey
  **/
  @ApiModelProperty(value = "")
  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }

  public BindDTO createdIp(String createdIp) {
    this.createdIp = createdIp;
    return this;
  }

   /**
   * Get createdIp
   * @return createdIp
  **/
  @ApiModelProperty(value = "")
  public String getCreatedIp() {
    return createdIp;
  }

  public void setCreatedIp(String createdIp) {
    this.createdIp = createdIp;
  }

  public BindDTO extraParams(Map<String, String> extraParams) {
    this.extraParams = extraParams;
    return this;
  }

  public BindDTO putExtraParamsItem(String key, String extraParamsItem) {
    this.extraParams.put(key, extraParamsItem);
    return this;
  }

   /**
   * Get extraParams
   * @return extraParams
  **/
  @ApiModelProperty(value = "")
  public Map<String, String> getExtraParams() {
    return extraParams;
  }

  public void setExtraParams(Map<String, String> extraParams) {
    this.extraParams = extraParams;
  }

  public BindDTO icon(String icon) {
    this.icon = icon;
    return this;
  }

   /**
   * Get icon
   * @return icon
  **/
  @ApiModelProperty(value = "")
  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public BindDTO login(String login) {
    this.login = login;
    return this;
  }

   /**
   * Get login
   * @return login
  **/
  @ApiModelProperty(value = "")
  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public BindDTO loginDomain(String loginDomain) {
    this.loginDomain = loginDomain;
    return this;
  }

   /**
   * Get loginDomain
   * @return loginDomain
  **/
  @ApiModelProperty(value = "")
  public String getLoginDomain() {
    return loginDomain;
  }

  public void setLoginDomain(String loginDomain) {
    this.loginDomain = loginDomain;
  }

  public BindDTO nick(String nick) {
    this.nick = nick;
    return this;
  }

   /**
   * Get nick
   * @return nick
  **/
  @ApiModelProperty(value = "")
  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public BindDTO password(String password) {
    this.password = password;
    return this;
  }

   /**
   * Get password
   * @return password
  **/
  @ApiModelProperty(value = "")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public BindDTO profileKey(String profileKey) {
    this.profileKey = profileKey;
    return this;
  }

   /**
   * Get profileKey
   * @return profileKey
  **/
  @ApiModelProperty(required = true, value = "")
  public String getProfileKey() {
    return profileKey;
  }

  public void setProfileKey(String profileKey) {
    this.profileKey = profileKey;
  }

  public BindDTO profileNo(String profileNo) {
    this.profileNo = profileNo;
    return this;
  }

   /**
   * Get profileNo
   * @return profileNo
  **/
  @ApiModelProperty(value = "")
  public String getProfileNo() {
    return profileNo;
  }

  public void setProfileNo(String profileNo) {
    this.profileNo = profileNo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BindDTO bindDTO = (BindDTO) o;
    return Objects.equals(this.accountNo, bindDTO.accountNo) &&
        Objects.equals(this.appKey, bindDTO.appKey) &&
        Objects.equals(this.createdIp, bindDTO.createdIp) &&
        Objects.equals(this.extraParams, bindDTO.extraParams) &&
        Objects.equals(this.icon, bindDTO.icon) &&
        Objects.equals(this.login, bindDTO.login) &&
        Objects.equals(this.loginDomain, bindDTO.loginDomain) &&
        Objects.equals(this.nick, bindDTO.nick) &&
        Objects.equals(this.password, bindDTO.password) &&
        Objects.equals(this.profileKey, bindDTO.profileKey) &&
        Objects.equals(this.profileNo, bindDTO.profileNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountNo, appKey, createdIp, extraParams, icon, login, loginDomain, nick, password, profileKey, profileNo);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BindDTO {\n");
    
    sb.append("    accountNo: ").append(toIndentedString(accountNo)).append("\n");
    sb.append("    appKey: ").append(toIndentedString(appKey)).append("\n");
    sb.append("    createdIp: ").append(toIndentedString(createdIp)).append("\n");
    sb.append("    extraParams: ").append(toIndentedString(extraParams)).append("\n");
    sb.append("    icon: ").append(toIndentedString(icon)).append("\n");
    sb.append("    login: ").append(toIndentedString(login)).append("\n");
    sb.append("    loginDomain: ").append(toIndentedString(loginDomain)).append("\n");
    sb.append("    nick: ").append(toIndentedString(nick)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    profileKey: ").append(toIndentedString(profileKey)).append("\n");
    sb.append("    profileNo: ").append(toIndentedString(profileNo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

