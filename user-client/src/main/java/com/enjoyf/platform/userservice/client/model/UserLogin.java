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
import org.joda.time.DateTime;

/**
 * UserLogin
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-04-08T10:52:24.374+08:00")
public class UserLogin {
  @SerializedName("accountNo")
  private String accountNo = null;

  @SerializedName("activated")
  private Boolean activated = null;

  @SerializedName("createdTime")
  private DateTime createdTime = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("login")
  private String login = null;

  @SerializedName("loginDomain")
  private String loginDomain = null;

  @SerializedName("loginName")
  private String loginName = null;

  @SerializedName("password")
  private String password = null;

  @SerializedName("passwordTime")
  private String passwordTime = null;

  public UserLogin accountNo(String accountNo) {
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

  public UserLogin activated(Boolean activated) {
    this.activated = activated;
    return this;
  }

   /**
   * Get activated
   * @return activated
  **/
  @ApiModelProperty(value = "")
  public Boolean getActivated() {
    return activated;
  }

  public void setActivated(Boolean activated) {
    this.activated = activated;
  }

  public UserLogin createdTime(DateTime createdTime) {
    this.createdTime = createdTime;
    return this;
  }

   /**
   * Get createdTime
   * @return createdTime
  **/
  @ApiModelProperty(required = true, value = "")
  public DateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(DateTime createdTime) {
    this.createdTime = createdTime;
  }

  public UserLogin id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserLogin login(String login) {
    this.login = login;
    return this;
  }

   /**
   * Get login
   * @return login
  **/
  @ApiModelProperty(required = true, value = "")
  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public UserLogin loginDomain(String loginDomain) {
    this.loginDomain = loginDomain;
    return this;
  }

   /**
   * Get loginDomain
   * @return loginDomain
  **/
  @ApiModelProperty(required = true, value = "")
  public String getLoginDomain() {
    return loginDomain;
  }

  public void setLoginDomain(String loginDomain) {
    this.loginDomain = loginDomain;
  }

  public UserLogin loginName(String loginName) {
    this.loginName = loginName;
    return this;
  }

   /**
   * Get loginName
   * @return loginName
  **/
  @ApiModelProperty(value = "")
  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public UserLogin password(String password) {
    this.password = password;
    return this;
  }

   /**
   * Get password
   * @return password
  **/
  @ApiModelProperty(required = true, value = "")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserLogin passwordTime(String passwordTime) {
    this.passwordTime = passwordTime;
    return this;
  }

   /**
   * Get passwordTime
   * @return passwordTime
  **/
  @ApiModelProperty(value = "")
  public String getPasswordTime() {
    return passwordTime;
  }

  public void setPasswordTime(String passwordTime) {
    this.passwordTime = passwordTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserLogin userLogin = (UserLogin) o;
    return Objects.equals(this.accountNo, userLogin.accountNo) &&
        Objects.equals(this.activated, userLogin.activated) &&
        Objects.equals(this.createdTime, userLogin.createdTime) &&
        Objects.equals(this.id, userLogin.id) &&
        Objects.equals(this.login, userLogin.login) &&
        Objects.equals(this.loginDomain, userLogin.loginDomain) &&
        Objects.equals(this.loginName, userLogin.loginName) &&
        Objects.equals(this.password, userLogin.password) &&
        Objects.equals(this.passwordTime, userLogin.passwordTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountNo, activated, createdTime, id, login, loginDomain, loginName, password, passwordTime);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserLogin {\n");
    
    sb.append("    accountNo: ").append(toIndentedString(accountNo)).append("\n");
    sb.append("    activated: ").append(toIndentedString(activated)).append("\n");
    sb.append("    createdTime: ").append(toIndentedString(createdTime)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    login: ").append(toIndentedString(login)).append("\n");
    sb.append("    loginDomain: ").append(toIndentedString(loginDomain)).append("\n");
    sb.append("    loginName: ").append(toIndentedString(loginName)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    passwordTime: ").append(toIndentedString(passwordTime)).append("\n");
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

