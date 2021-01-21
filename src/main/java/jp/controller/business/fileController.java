package jp.controller.business;

import jp.service.IFileHandleService;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class fileController {

    @Autowired
    IFileHandleService fileHandleService;

    /**
     * 图片转换
     * @param request
     * @return
     */
    @RequestMapping(value = "/fileChange.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo fileChangeMethod(HttpServletRequest request) {

        return fileHandleService.fileToPdf(request);
    }

    /**
     * PDF填充
     * @param request
     * @return
     */
    @RequestMapping(value = "/fileUpload.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo fileUploadMethod(HttpServletRequest request, HttpServletResponse response) {

        return fileHandleService.fileUpload(request, response);
    }

    /**
     * PDF填充
     * @param request
     * @return
     */
    @RequestMapping(value = "/fileFill.json")
    @ResponseBody
    public void fileFillMethod(HttpServletRequest request, HttpServletResponse response) {

        fileHandleService.fileFill(request, response);
    }

    /**
     * PDF填充
     * @param request
     * @return
     */
    @RequestMapping(value = "/fileTempFill.json")
    @ResponseBody
    public void fileTempFillMethod(HttpServletRequest request, HttpServletResponse response) {

        fileHandleService.fileTempFill(request, response);
    }


}
