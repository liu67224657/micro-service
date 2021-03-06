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
 * UserAccount
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-04-08T10:52:24.374+08:00")
public class UserAccount {
  @SerializedName("accountNo")
  private String accountNo = null;

  @SerializedName("address")
  private String address = null;

  @SerializedName("createdTime")
  private DateTime createdTime = null;

  @SerializedName("flag")
  private Integer flag = null;

  @SerializedName("id")
  private Long id = null;

  public UserAccount accountNo(String accountNo) {
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

  public UserAccount address(String address) {
    this.address = address;
    return this;
  }

   /**
   * Get address
   * @return address
  **/
  @ApiModelProperty(value = "")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public UserAccount createdTime(DateTime createdTime) {
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

  public UserAccount flag(Integer flag) {
    this.flag = flag;
    return this;
  }

   /**
   * Get flag
   * @return flag
  **/
  @ApiModelProperty(required = true, value = "")
  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public UserAccount id(Long id) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserAccount userAccount = (UserAccount) o;
    return Objects.equals(this.accountNo, userAccount.accountNo) &&
        Objects.equals(this.address, userAccount.address) &&
        Objects.equals(this.createdTime, userAccount.createdTime) &&
        Objects.equals(this.flag, userAccount.flag) &&
        Objects.equals(this.id, userAccount.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountNo, address, createdTime, flag, id);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserAccount {\n");
    
    sb.append("    accountNo: ").append(toIndentedString(accountNo)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    createdTime: ").append(toIndentedString(createdTime)).append("\n");
    sb.append("    flag: ").append(toIndentedString(flag)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

