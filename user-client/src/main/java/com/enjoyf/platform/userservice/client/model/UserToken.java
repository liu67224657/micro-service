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
 * UserToken
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-04-08T10:52:24.374+08:00")
public class UserToken {
  @SerializedName("accountNo")
  private String accountNo = null;

  @SerializedName("createdIp")
  private String createdIp = null;

  @SerializedName("createdTime")
  private DateTime createdTime = null;

  @SerializedName("expires")
  private Integer expires = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("profileKey")
  private String profileKey = null;

  @SerializedName("profileNo")
  private String profileNo = null;

  @SerializedName("requestParam")
  private String requestParam = null;

  @SerializedName("tokenType")
  private Integer tokenType = null;

  public UserToken accountNo(String accountNo) {
    this.accountNo = accountNo;
    return this;
  }

   /**
   * Get accountNo
   * @return accountNo
  **/
  @ApiModelProperty(value = "")
  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public UserToken createdIp(String createdIp) {
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

  public UserToken createdTime(DateTime createdTime) {
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

  public UserToken expires(Integer expires) {
    this.expires = expires;
    return this;
  }

   /**
   * Get expires
   * @return expires
  **/
  @ApiModelProperty(value = "")
  public Integer getExpires() {
    return expires;
  }

  public void setExpires(Integer expires) {
    this.expires = expires;
  }

  public UserToken id(Long id) {
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

  public UserToken profileKey(String profileKey) {
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

  public UserToken profileNo(String profileNo) {
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

  public UserToken requestParam(String requestParam) {
    this.requestParam = requestParam;
    return this;
  }

   /**
   * Get requestParam
   * @return requestParam
  **/
  @ApiModelProperty(value = "")
  public String getRequestParam() {
    return requestParam;
  }

  public void setRequestParam(String requestParam) {
    this.requestParam = requestParam;
  }

  public UserToken tokenType(Integer tokenType) {
    this.tokenType = tokenType;
    return this;
  }

   /**
   * Get tokenType
   * @return tokenType
  **/
  @ApiModelProperty(required = true, value = "")
  public Integer getTokenType() {
    return tokenType;
  }

  public void setTokenType(Integer tokenType) {
    this.tokenType = tokenType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserToken userToken = (UserToken) o;
    return Objects.equals(this.accountNo, userToken.accountNo) &&
        Objects.equals(this.createdIp, userToken.createdIp) &&
        Objects.equals(this.createdTime, userToken.createdTime) &&
        Objects.equals(this.expires, userToken.expires) &&
        Objects.equals(this.id, userToken.id) &&
        Objects.equals(this.profileKey, userToken.profileKey) &&
        Objects.equals(this.profileNo, userToken.profileNo) &&
        Objects.equals(this.requestParam, userToken.requestParam) &&
        Objects.equals(this.tokenType, userToken.tokenType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountNo, createdIp, createdTime, expires, id, profileKey, profileNo, requestParam, tokenType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserToken {\n");
    
    sb.append("    accountNo: ").append(toIndentedString(accountNo)).append("\n");
    sb.append("    createdIp: ").append(toIndentedString(createdIp)).append("\n");
    sb.append("    createdTime: ").append(toIndentedString(createdTime)).append("\n");
    sb.append("    expires: ").append(toIndentedString(expires)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    profileKey: ").append(toIndentedString(profileKey)).append("\n");
    sb.append("    profileNo: ").append(toIndentedString(profileNo)).append("\n");
    sb.append("    requestParam: ").append(toIndentedString(requestParam)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
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
