package jp.enums;

/**
 * 枚举类型，主要是返回错误信息等
 */
public enum MessageEnum implements BaseEnumInterface{

    SUCCESS("0000","成功"),
    E001("E001","参数不完整,请输入{0}"),
    E002("E002","参数不完整,{0},{1}不为空"),
    E003("E003","{0}类型错误,请输入{1}"),
    E004("E004","{0}参数错误,请输入{1}"),
    E005("E005","{0}长度有误,最小长度{1},最大长度{2}"),
    E006("E006","功能标识:{0}不存在,请确认!"),
    E007("E007","操作失败:{0}"),
    E008("E008","未知异常"),
    E009("E009","访问失败,请通过接口访问!"),
    ;

    private String code;
    private String msg;

    MessageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 通过状态码获取枚举对象
     * @param code 状态码
     * @return 枚举对象
     */
    public static MessageEnum getByCode(String code){
        for (MessageEnum resultEnum : MessageEnum.values()) {
            if(code == resultEnum.getCode()){
                return resultEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}