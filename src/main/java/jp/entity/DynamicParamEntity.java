package jp.entity;

import lombok.Data;

@Data
public class DynamicParamEntity {

    private Integer id;
    private String paramKey;
    private String paramType;
    private String paramValue;
}
