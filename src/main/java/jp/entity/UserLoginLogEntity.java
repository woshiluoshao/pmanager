package jp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserLoginLogEntity {

    private int id;
    private String userId;
    private int action;
    private String comments;
    private String visitIp;
    private Date createTime;
}
