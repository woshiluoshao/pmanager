package jp.service.impl;

import jp.db.mybatis.dao.RecordThirdPartMapper;
import jp.db.mybatis.model.RecordThirdPartWithBLOBs;
import jp.service.IRecordService;
import jp.utils.Layui;
import jp.utils.PageUtils;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordServiceImpl implements IRecordService {

    @Resource
    RecordThirdPartMapper thirdPartMapper;

    @Override
    public ResultVo addThirdPart(HttpServletRequest request) {


        return null;
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
}
