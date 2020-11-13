package jp.exception;

import jp.enums.MessageEnum;
import lombok.Data;

@Data
public class CustomException extends RuntimeException {

    /**
     * 状态码
     */
    private String code;

    public CustomException(MessageEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    /**
     * @param code    状态码
     * @param message 错误信息
     */
    public CustomException(String code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
