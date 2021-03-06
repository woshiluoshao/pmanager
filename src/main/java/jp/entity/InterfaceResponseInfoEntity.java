package jp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class InterfaceResponseInfoEntity {

    private String projectId;
    private String director;
    private String project;
    private String projectName;
    private String developmentLanguage;
    private String developmentArchitect;
    private String functionPoint;
    private String comments;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private int updateCount;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    private String projectStart;
    private String projectEnd;
    private String collaborator;
    private String projectManager;
    private String projectStatus;
    private String flowChart;
}
