package jp.db.mybatis.model;

import java.util.Date;

public class InterfaceDeployInfo {
    private Integer uuid;

    private String projectId;

    private String environment;

    private String sender;

    private String receiver;

    private String readMe;

    private String fileType;

    private String fileUrl;

    private String deployIP;

    private String deployPort;

    private String projectCallAddress;

    private Integer deployStatus;

    private Date createTime;

    private String createAuthor;

    private Integer updateCount;

    private Date updateTime;

    private String updateAuthor;

    private String comments;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment == null ? null : environment.trim();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? null : sender.trim();
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public String getReadMe() {
        return readMe;
    }

    public void setReadMe(String readMe) {
        this.readMe = readMe == null ? null : readMe.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public String getDeployIP() {
        return deployIP;
    }

    public void setDeployIP(String deployIP) {
        this.deployIP = deployIP == null ? null : deployIP.trim();
    }

    public String getDeployPort() {
        return deployPort;
    }

    public void setDeployPort(String deployPort) {
        this.deployPort = deployPort == null ? null : deployPort.trim();
    }

    public String getProjectCallAddress() {
        return projectCallAddress;
    }

    public void setProjectCallAddress(String projectCallAddress) {
        this.projectCallAddress = projectCallAddress == null ? null : projectCallAddress.trim();
    }

    public Integer getDeployStatus() {
        return deployStatus;
    }

    public void setDeployStatus(Integer deployStatus) {
        this.deployStatus = deployStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateAuthor() {
        return createAuthor;
    }

    public void setCreateAuthor(String createAuthor) {
        this.createAuthor = createAuthor == null ? null : createAuthor.trim();
    }

    public Integer getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Integer updateCount) {
        this.updateCount = updateCount;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(String updateAuthor) {
        this.updateAuthor = updateAuthor == null ? null : updateAuthor.trim();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }
}