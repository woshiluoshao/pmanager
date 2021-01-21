package jp.service;

import jp.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IFileHandleService {

    ResultVo fileToPdf(HttpServletRequest request);
    ResultVo fileUpload(HttpServletRequest request, HttpServletResponse response);
    void fileFill(HttpServletRequest request, HttpServletResponse response);
    void fileTempFill(HttpServletRequest request, HttpServletResponse response);
}
