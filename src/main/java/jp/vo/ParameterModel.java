package jp.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ParameterModel {

    private String method;
    private String timestamps;
    private String transId;
    private JSONObject data;
}
