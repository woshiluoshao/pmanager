package jp.vo;

import lombok.Data;

@Data
public class ResultVo {
    //返回码
    private String code;
    //返回消息
    private String msg;
    //返回数据
    private String data;

    public ResultVo() {
    }

    public ResultVo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVo(String code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
