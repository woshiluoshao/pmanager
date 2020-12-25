package jp.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DirectorInfoEntity {

    private String account;
    private String username;
    private String password;
    private Date createTime;
    private String author;
    private String level;
    private String updateAuthor;

}
