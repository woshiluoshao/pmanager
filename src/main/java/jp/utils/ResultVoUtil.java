package jp.utils;

import jp.enums.BaseEnumInterface;
import jp.enums.MessageEnum;
import jp.vo.ResultVo;
import org.springframework.util.StringUtils;

public class ResultVoUtil {

    /**
     * 返回成功
     * @return
     */
    public static ResultVo success() {
        return new ResultVo(MessageEnum.SUCCESS.getCode(), MessageEnum.SUCCESS.getMsg());
    }

    public static ResultVo success(String msg) {
        if(msg == null || StringUtils.isEmpty(msg.trim())) {
            msg = MessageEnum.SUCCESS.getMsg();
        }
        return new ResultVo(MessageEnum.SUCCESS.getCode(), msg);
    }

    /**
     * 返回成功有返回值
     * @param json
     * @return
     */
    public static ResultVo success(String msg, String json) {
        if(msg == null || StringUtils.isEmpty(msg.trim())) {
            msg = MessageEnum.SUCCESS.getMsg();
        }
        return new ResultVo(MessageEnum.SUCCESS.getCode(), msg, json);
    }

    public static ResultVo error(String code, String msg) {
        return new ResultVo(code, msg);
    }

    /**
     * 无返回值的错误方法
     *
     * @param baseEnumInterface 系统级别错误相关枚举
     * @return
     */
    public static ResultVo error(BaseEnumInterface baseEnumInterface) {
        return new ResultVo(baseEnumInterface.getCode(), baseEnumInterface.getMsg());
    }

    /**
     * 有返回值错误方法
     *
     * @param baseEnumInterface 枚举父接口
     * @param json              JSON 返回值
     * @return
     */
    public static ResultVo error(BaseEnumInterface baseEnumInterface, String json) {
        return new ResultVo(baseEnumInterface.getCode(), baseEnumInterface.getMsg(), json);
    }

    /**
     * 返回错误信息，如果有参数的话
     * @param baseEnumInterface
     * @param json
     * @param paramStr
     * @return
     */
    public static ResultVo error(BaseEnumInterface baseEnumInterface, String json, String... paramStr) {

        String msg = baseEnumInterface.getMsg();
        msg = MessageUtils.message(msg, paramStr);
        return new ResultVo(baseEnumInterface.getCode(), msg, json);
    }
}
