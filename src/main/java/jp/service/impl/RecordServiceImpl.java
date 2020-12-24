package jp.service.impl;

import jp.db.mybatis.dao.RecordThirdPartMapper;
import jp.db.mybatis.model.RecordThirdPart;
import jp.db.mybatis.model.RecordThirdPartWithBLOBs;
import jp.enums.MessageEnum;
import jp.service.IRecordService;
import jp.utils.*;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordServiceImpl implements IRecordService {

    @Resource
    RecordThirdPartMapper thirdPartMapper;

    @Override
    public ResultVo addThirdPart(RecordThirdPartWithBLOBs thirdPartWithBLOBs, HttpServletRequest request) {

        int cnt = -1;
        if(StringUtils.isEmpty(thirdPartWithBLOBs.getUuid())) {
            //开始追加
            thirdPartWithBLOBs.setUuid(CommonUtils.getUuid());
            Date nowTime = DateUtils.getCurrentTime();
            thirdPartWithBLOBs.setCreateTime(nowTime);
            thirdPartWithBLOBs.setUpdateTime(nowTime);
            thirdPartWithBLOBs.setUpdateCount(0);
            cnt = thirdPartMapper.insertSelective(thirdPartWithBLOBs);
        } else {
            RecordThirdPart model = thirdPartMapper.selectByPrimaryKey(thirdPartWithBLOBs.getUuid());
            if(model != null) {
                if(model.getUpdateCount() != thirdPartWithBLOBs.getUpdateCount()) {
                    return ResultVoUtil.error(MessageEnum.W004);
                }
                thirdPartWithBLOBs.setCreateTime(model.getCreateTime());
                Date nowTime = DateUtils.getCurrentTime();
                thirdPartWithBLOBs.setUpdateTime(nowTime);
                thirdPartWithBLOBs.setUpdateCount(model.getUpdateCount() == null? 0 : model.getUpdateCount() + 1);
            }
            cnt = thirdPartMapper.updateByPrimaryKeySelective(thirdPartWithBLOBs);
        }

        if(cnt > 0) return ResultVoUtil.success();

        return ResultVoUtil.error(MessageEnum.E007, null, "数据操作失败");
    }

    @Override
    public Layui selectThirdPart(HttpServletRequest request) {

        try {
            int limit = Integer.parseInt(request.getParameter("limit"));
            int page = Integer.parseInt(request.getParameter("page"));

            Map<String, Object> param = new HashMap<>();
            param.put("start", ((page -1) * limit));
            param.put("len", limit);

            List<RecordThirdPartWithBLOBs> thirdList = thirdPartMapper.getThirdPartList(param);
            int total = thirdPartMapper.countThirdPartList(param);

            if(thirdList.size() > 0) {
                PageUtils pageUtil = new PageUtils(thirdList, total, 10,1);
                return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Layui.data(0, null);
    }

    @Override
    public ResultVo delThirdPart(HttpServletRequest request) {

        try {
            String uuid = request.getParameter("uuid");
            RecordThirdPartWithBLOBs model = thirdPartMapper.selectByPrimaryKey(uuid);
            if(model == null) {
                return ResultVoUtil.error(MessageEnum.W005);
            }

            int del = thirdPartMapper.deleteByPrimaryKey(uuid);
            if(del > 0) {
                return ResultVoUtil.success(MessageEnum.DEL0000);
            } else {
                return ResultVoUtil.error(MessageEnum.E015);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultVoUtil.error(MessageEnum.E015);
    }
}
