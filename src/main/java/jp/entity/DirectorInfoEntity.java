package jp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DirectorInfoEntity {

    private String userId;
    private String password;
    private Date createTime;
}
