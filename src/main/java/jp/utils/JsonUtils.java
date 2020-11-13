package jp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;
import jp.vo.JsonModel;
import jp.vo.ParameterModel;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtils {

    /**
     * 流解析成json
     * @param request
     */
    public static String streamToJsonData(HttpServletRequest request) {

        String result = "";

        try {

            BufferedReader reader = request.getReader();
            char[] buf = new char[512];

            int len = 0;
            StringBuffer contentBuffer = new StringBuffer();

            while ((len = reader.read(buf)) != -1) {
                contentBuffer.append(buf, 0, len);
            }

            result = contentBuffer.toString();
        } catch (IOException e) {
            result = "";
        }

        return result;
    }

    /**
     * 字符串转换成json格式输出
     * @param response
     * @param data
     */
    public static void writeData(HttpServletResponse response, String data) {

        try {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(data);

            pw.close();
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符串转换成json格式输出
     * @param data
     */
    public static void writeData(String data) {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(data);

            pw.close();
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否为json
     * @param jsonInfo
     * @return
     */
    public static boolean validIsJson(String jsonInfo) {

        try {
            JSONObject.parseObject(jsonInfo);
            return true;
        } catch (Exception e) {
        }

        return false;
    }

    /**
     * 对象转json
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {

        //防止首字母变成小写
        TypeUtils.compatibleWithJavaBean =true;
        String jsonStr = JSON.toJSONString(object);
        return jsonStr;
    }

    /**
     * 返回JSON
     * @param parameterModel
     * @return
     */
    public static String resultJson(ParameterModel parameterModel) {

        JsonModel jsonModel = new JsonModel();
        jsonModel.setRESULTCODE(parameterModel.getRESULTCODE());
        jsonModel.setRESULTMSG(parameterModel.getRESULRMSG());
        jsonModel.setDATA(parameterModel.getDATA());
        TypeUtils.compatibleWithJavaBean =true;
        String jsonInfo = JSONObject.toJSONString(jsonModel, SerializerFeature.WriteNullBooleanAsFalse);

        return jsonInfo;
    }

    /**
     * 返回JSON
     * @param response
     * @param parameterModel
     */
    public static void resultJson(HttpServletResponse response, ParameterModel parameterModel) {

        JsonModel jsonModel = new JsonModel();
        jsonModel.setRESULTCODE(parameterModel.getRESULTCODE());
        jsonModel.setRESULTMSG(parameterModel.getRESULRMSG());
        jsonModel.setDATA(parameterModel.getDATA());
        TypeUtils.compatibleWithJavaBean =true;
        String jsonInfo = JSONObject.toJSONString(jsonModel, SerializerFeature.WriteNullBooleanAsFalse);
        writeData(response, jsonInfo);
        //return jsonInfo;
    }
}
