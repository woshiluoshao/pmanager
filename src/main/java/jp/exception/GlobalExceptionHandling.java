package jp.exception;

import jp.enums.MessageEnum;
import jp.utils.ResultVoUtil;
import jp.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandling {

    /**
     * 通用异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVo exception(Exception e) {
        return ResultVoUtil.error(MessageEnum.E008);
    }

}
