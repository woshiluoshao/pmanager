package jp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DeployTask {

    private String id;
    private String name;
    private DeployEntity vac;
    private Date createTime;
}
