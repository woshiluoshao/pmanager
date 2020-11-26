package jp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserListEntity {

    private String userId;
    private String password;
    private Date createTime;
}
