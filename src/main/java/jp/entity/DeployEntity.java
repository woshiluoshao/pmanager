package jp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DeployEntity {

    /**
     *申请人
     */
    private String sender;
    private String reason;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date applyTime;
    private String applyStatus;
    private String environment;
    private String readme;

    /**
     * 审核人
     */
    private String checker;
    private String result;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date receiveTime;

    /**
     * 任务执行后的ID
     */
    private String processInstanceId;

}
