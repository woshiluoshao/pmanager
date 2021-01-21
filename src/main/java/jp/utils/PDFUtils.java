package jp.utils;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PDFUtils {

    /**
     * 根据模板填充pdf
     *
     * @param os       输出流
     * @param template 模板 模板制作请参考: https://99designs.com/blog/design-tutorials/pdf-business-card-template-tutorial/
     * @param params   K:文本域名称 V:填充的值
     */
    public static boolean fillTemplate(OutputStream os, byte[] template, Map<String, String> params) {// 利用模板生成pdf

        boolean result = false;

        PdfStamper stamper = null;
        PdfReader reader = null;
        try {
            //  读入pdf表单
            reader = new PdfReader(template);

            //  根据表单生成一个新的pdf
            stamper = new PdfStamper(reader, os);

            //  获取pdf表单
            AcroFields form = stamper.getAcroFields();

            // 给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            form.addSubstitutionFont(bf);

            // 遍历data 给pdf表单表格赋值
            for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                form.setField(entry.getKey(), entry.getValue());
            }

            stamper.setFormFlattening(true);

            result = true;
            //-------------------------------------------------------------
            System.out.println("===============PDF导出成功=============");
        } catch (Exception e) {
            System.out.println("===============PDF导出失败=============");
            result = false;
            e.printStackTrace();
        } finally {
            try {
                stamper.close();
                reader.close();
            } catch (Exception e) {
                result = false;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            String outPath = "D:/fillResult.pdf";
            String path = "D:/fill测试.pdf";

            InputStream ins = new BufferedInputStream(new FileInputStream(path));
            ////读取工程目录下的文件
            //InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("template/smmTemplate.pdf");
            FileOutputStream os = new FileOutputStream(outPath);
            byte[] byt = new byte[ins.available()];
            ins.read(byt);

            Map<String, String> data = new HashMap<>();
            data.put("username", "小明");
            data.put("sex", "男");
            data.put("like", "女");
            fillTemplate(os, byt, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
