package jp.service.impl;

import com.alibaba.fastjson.JSON;
import jp.db.mybatis.dao.UserAccountInfoMapper;
import jp.db.mybatis.model.UserAccountInfo;
import jp.dto.LoginDto;
import jp.enums.MessageEnum;
import jp.service.IAccountService;
import jp.utils.DateUtils;
import jp.utils.Layui;
import jp.utils.PageUtils;
import jp.utils.ResultVoUtil;
import jp.vo.ResultVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements IAccountService {

    @Resource
    UserAccountInfoMapper accountInfoMapper;

    /**
     * 账号登录
     * @param loginDto
     * @param request
     * @return
     */
    @Override
    public ResultVo accountLogin(LoginDto loginDto, HttpServletRequest request) {

        if(loginDto == null || StringUtils.isEmpty(loginDto.getAccount()) || StringUtils.isEmpty(loginDto.getPassword())) {
            System.out.println("参数不完整");
            return ResultVoUtil.error(MessageEnum.E002, null, "账号", "密码");
        }

        UserAccountInfo accountInfo = accountInfoMapper.selectByPrimaryKey(loginDto.getAccount());

        if(accountInfo == null) {
            return ResultVoUtil.error(MessageEnum.E011);
        } else if(!accountInfo.getPassword().equals(loginDto.getPassword())) {
            return ResultVoUtil.error(MessageEnum.E012);
        } else if(!accountInfo.getLevel().equals(loginDto.getLevel())) {
            return ResultVoUtil.error(MessageEnum.E016);
        }

        //账号信息计入缓存
        HttpSession session = request.getSession();
        session.setAttribute("accountInfo", accountInfo);

        return ResultVoUtil.success("登录成功");
    }

    /**
     * 账号分配
     * @param accountInfo
     * @return
     */
    @Override
    public ResultVo accountAssign(UserAccountInfo accountInfo) {

        //此处不做后端数据过滤
        UserAccountInfo accountModel = accountInfoMapper.selectByPrimaryKey(accountInfo.getAccount());

        if(accountModel != null) {
            return ResultVoUtil.error(MessageEnum.E014);
        }

        accountInfo.setCreateTime(DateUtils.getCurrentTime());
        accountInfo.setUpdateAuthor(accountInfo.getAuthor());
        int assignCnt = accountInfoMapper.insertSelective(accountInfo);

        if(assignCnt > 0) return ResultVoUtil.success("账号分配成功");

        return ResultVoUtil.error(MessageEnum.E017, null, "账号分配失败");
    }

    /**
     * 删除账号
     * @param accountInfo
     * @return
     */
    @Override
    public ResultVo accountDel(UserAccountInfo accountInfo) {

        //此处不做后端数据过滤
        UserAccountInfo accountModel = accountInfoMapper.selectByPrimaryKey(accountInfo.getAccount());

        if(accountModel != null) {
           int delCnt = accountInfoMapper.deleteByPrimaryKey(accountInfo.getAccount());
           if(delCnt > 0) return ResultVoUtil.success("账号删除成功");
           return ResultVoUtil.error(MessageEnum.E017, null, "账号删除失败");
        }

        return ResultVoUtil.error(MessageEnum.E017, null, "账号不存在");
    }

    /**
     * 查找用户集合
     * @param request
     * @return
     */
    @Override
    public Layui accountList(HttpServletRequest request) {

        try {
            int limit = Integer.parseInt(request.getParameter("limit"));
            int page = Integer.parseInt(request.getParameter("page"));

            Map<String, Object> param = new HashMap<>();
            param.put("start", ((page -1) * limit));
            param.put("len", limit);

            List<UserAccountInfo> accountInfoList = accountInfoMapper.selectAccountList(param);
            int total = accountInfoMapper.countAccountList();
            if(accountInfoList.size() > 0) {
                PageUtils pageUtil = new PageUtils(accountInfoList, total, 10,1);
                return Layui.data(pageUtil.getTotalCount(), pageUtil.getList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Layui.data(0, null);
    }

    /**
     * 获取在线的人员(除自身外)
     * @param request
     * @return
     */
    @Override
    public ResultVo accountOnline(HttpServletRequest request) {
        String account = request.getParameter("account");
        List<UserAccountInfo> onlineList = accountInfoMapper.selectOnlineAccountList(account);
        if(onlineList != null && onlineList.size() > 0) return ResultVoUtil.success("查询成功", JSON.toJSONString(onlineList));

        return ResultVoUtil.error(MessageEnum.W001);
    }
}
