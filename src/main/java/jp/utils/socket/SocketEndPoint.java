package jp.utils.socket;

import com.alibaba.fastjson.JSONObject;
import jp.entity.ChatEntity;
import jp.service.IUserService;
import jp.utils.ChatUtils;
import jp.utils.CommonUtils;
import jp.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import static jp.utils.socket.SocketPool.*;
import static jp.utils.socket.SocketHandler.createKey;

// 注入容器
@Component
// 表明这是一个websocket服务的端点
@ServerEndpoint("/net/websocket/{name}")
public class SocketEndPoint {

    private static final Logger log = LoggerFactory.getLogger(SocketEndPoint.class);

    private static IUserService userService;

    @Autowired
    public void setUserService(IUserService userService){
        SocketEndPoint.userService = userService;
    }

    /**
     * 连接建立时被调用
     * @param name
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("name") String name,  Session session) {
//        log.info("有新的连接：{}", session);
//        addPool(createKey(name), session);
//        for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
//            if (item.getKey().equals(name)) {
//                SocketHandler.sendMessageAll("<div style='width: 100%; float: left;'>用户【" + name + "】已上线</div>", name);
//            }
//        }
//        log.info("在线人数：{}",count());
//        sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
//        for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
//            log.info("12: {}", item.getKey());
//        }


        log.info("有新的连接：{}", session);
        addPool(createKey(name), session);
        for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
            if (item.getKey().equals(name)) {
                String message = "<div style='width: 100%; float: left; color:green'>[" + DateUtils.getCurrentTimeYmdHmsss() + "] " + name + "上线啦!!!</div>";
                String chatJson = ChatUtils.chatJson("true", "", "", message);
                SocketHandler.sendMessageAll(chatJson, name);
            }
        }
    }

    /**
     * 接收到客户端发送的消息时被调用
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        //String message = "";
        JSONObject jsonObject = JSONObject.parseObject(message);
        String fromName = jsonObject.getString("fromName");
        String toName = jsonObject.getString("toName");
        String messageContent = jsonObject.getString("message");
        String isSystem = jsonObject.getString("isSystem");

        if(StringUtils.isEmpty(toName)) {
            //没有发送者，算群发消息
            SocketHandler.sendMessageAll( "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + fromName + "群发消息</div><div style='width: 100%; font-size: 12px; font-weight: bolder; float: right;'>" + messageContent + "</div>", fromName);
        } else {
            Session userSession;
            for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
                if (item.getKey().equals(toName)) {
                    userSession = item.getValue();

                    //String chatMessage = "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + fromName + "</div><div style='width: 100%; font-size: 12px; font-weight: bolder; float: right; background-color: #FFFFFF'>" + messageContent + "</div>";
                    String chatMessage = "<div style='width: 100%; float: left;'><span style='float: left;'>" + fromName + "&nbsp;&nbsp;</span><br/><span style='float: left; font-size: 12px; font-weight: bolder; background-color: #ACC3D1'>" + messageContent + "</span></div>";

                    String chatJson = ChatUtils.chatJson("false", fromName, toName, chatMessage);
                    SocketHandler.sendMessage(userSession, chatJson);
                }
            }
        }

        /*if (message.contains("[allUsers]")) {
            String userInfo = message.substring(message.indexOf("[allUsers]")).replace("[allUsers]----------", "");
            SocketHandler.sendMessageAll( "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + userInfo + "群发消息</div><div style='width: 100%; font-size: 12px; font-weight: bolder; float: right;'>" + message.substring(0, message.indexOf("[")) + "</div>", userInfo);
        } else {
            String acceptUser = message.substring(message.indexOf("[") + 1, message.lastIndexOf("]"));
            String sendUser = message.substring(message.lastIndexOf("-") + 1, message.length());
            Session userSession;
            for (Map.Entry<String, Session> item : sessionMap().entrySet()) {
                if (item.getKey().equals(acceptUser)) {
                    userSession = item.getValue();
                    String userInfo = message.substring(0, message.indexOf("["));
                    SocketHandler.sendMessage(userSession, "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + sendUser + "</div><div style='width: 100%; font-size: 12px; font-weight: bolder; float: right;'>" + userInfo + "</div>");
                }
            }
        }*/
        log.info("有新消息： {}", message);
    }

    /**
     * 连接关闭时被调用
     * @param name
     * @param session
     */
    @OnClose
    public void onClose(@PathParam("name") String name,Session session) {
        log.info("连接关闭： {}", session);
        removePool(createKey(name));
        log.info("在线人数：{}", count());
        sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
        for (Map.Entry<String, Session> item : sessionMap().entrySet()){
            log.info("12: {}", item.getKey());
        }
        String message = "<div style='width: 100%; float: left; color:red'>[" + DateUtils.getCurrentTimeYmdHmsss() + "] " + name + "离开啦!!!</div>";
        String systemJson = ChatUtils.chatJson("true", "", "", message);
        SocketHandler.sendMessageAll(systemJson, name);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("退出发生异常： {}", e.getMessage());
        }
        log.info("连接出现异常： {}", throwable.getMessage());
    }
}