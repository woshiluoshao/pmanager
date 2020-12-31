package jp.dto;

import lombok.Data;

@Data
public class DeployDto {

    private String sender;
    private String receiver;
    private String environment;
    private String readme;
}
