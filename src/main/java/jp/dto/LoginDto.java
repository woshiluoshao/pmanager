package jp.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String account;
    private String username;
    private String password;
    private String level;
}
