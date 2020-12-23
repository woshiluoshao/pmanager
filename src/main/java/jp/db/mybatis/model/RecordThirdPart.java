package jp.db.mybatis.model;

import java.util.Date;

public class RecordThirdPart {
    private String uuid;

    private String company;

    private String companyname;

    private String scene;

    private String contact;

    private String contactinformation;

    private String localcontact;

    private String localcontactinfo;

    private Date createtime;

    private Integer updatecount;

    private Date updatetime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname == null ? null : companyname.trim();
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene == null ? null : scene.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContactinformation() {
        return contactinformation;
    }

    public void setContactinformation(String contactinformation) {
        this.contactinformation = contactinformation == null ? null : contactinformation.trim();
    }

    public String getLocalcontact() {
        return localcontact;
    }

    public void setLocalcontact(String localcontact) {
        this.localcontact = localcontact == null ? null : localcontact.trim();
    }

    public String getLocalcontactinfo() {
        return localcontactinfo;
    }

    public void setLocalcontactinfo(String localcontactinfo) {
        this.localcontactinfo = localcontactinfo == null ? null : localcontactinfo.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getUpdatecount() {
        return updatecount;
    }

    public void setUpdatecount(Integer updatecount) {
        this.updatecount = updatecount;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}