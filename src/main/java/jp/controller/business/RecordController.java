package jp.controller.business;

import jp.db.mybatis.model.RecordThirdPartWithBLOBs;
import jp.service.IRecordService;
import jp.utils.Layui;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RecordController {

     @Autowired
     IRecordService recordService;

    /**
     * 查询厂商信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/thirdSelect.json", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public Layui thirdSelectMethod(HttpServletRequest request) {
        return recordService.selectThirdPart(request);
    }

    /**
     * 厂商增加
     * @param request
     * @return
     */
    @RequestMapping(value = "/thirdAdd.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo thirdAddMethod(RecordThirdPartWithBLOBs thirdPartWithBLOBs, HttpServletRequest request) {
        return recordService.addThirdPart(thirdPartWithBLOBs, request);
    }

    /**
     * 删除厂商
     * @param request
     * @return
     */
    @RequestMapping(value = "/thirdDel.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo thirdDelMethod(HttpServletRequest request) {
        return recordService.delThirdPart(request);
    }



}
