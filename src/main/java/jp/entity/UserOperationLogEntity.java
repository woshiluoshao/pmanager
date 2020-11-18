package jp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserOperationLogEntity {

    private int id;
    private String userId;
    private String username;
    private String module;
    private String methods;
    private String methodName;
    private String acceptData;
    private String returnData;
    private String actionUrl;
    private String serverIp;
    private String visitIp;
    private Date createTime;
    private int comments;
}
