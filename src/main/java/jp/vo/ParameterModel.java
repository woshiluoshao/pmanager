package jp.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ParameterModel {

    private String code;
    private String msg;
    //private JSONObject data;

    private String RESULTCODE;
    private String RESULRMSG;
    private String DATA;
}
