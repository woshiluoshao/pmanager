package jp.entity;

import lombok.Data;

@Data
public class ChatEntity {

    private String fromName;
    private String toName;
    private String message;
    private String isSystem;
}
