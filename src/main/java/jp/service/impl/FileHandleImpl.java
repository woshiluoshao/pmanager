package jp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import jp.service.IFileHandleService;
import jp.utils.FileUtils;
import jp.utils.PDFUtils;
import jp.utils.ResultVoUtil;
import jp.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.DateUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileHandleImpl implements IFileHandleService {

    private static final int regular_width = 842;
    private static final int regular_height = 595;
    private static final int vertical_regular_width = 595;
    private static final int vertical_regular_height = 842;

    static final int WORD_FORMAT_PDF = 17;
    static final int PPT_FORMAT_PDF = 32;
    static final int EXCEL_FORMAT_PDF = 0;

    @Autowired
    Environment env;

    @Override
    public ResultVo fileToPdf(HttpServletRequest request) {

        //yml中配置的文件保存路径（D:/root/deploy/）
        String fileSavePath = env.getProperty("manager.save-path");

        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获取上传的文件
            MultipartFile multiFile = multipartRequest.getFile("file");

            assert multiFile != null;
            String fileName = multiFile.getOriginalFilename();
            String suffix = FileUtils.getFileSuffix(fileName);
            String fileNameNoExt = FileUtils.getFileNameNoEx(fileName);
            String contentType =  multiFile.getContentType();

            String pdfOutPath = fileSavePath + fileNameNoExt + ".pdf";

            if(contentType.contains("image/")) {
                boolean result = pictureToPDF(multiFile, pdfOutPath);
                if(result) return ResultVoUtil.success();
            } else {

                //将文件保存到当前服务启磁盘
                InputStream inputStream = multiFile.getInputStream();
                String totalPath = fileSavePath + fileName;
                FileUtils.createFolder(fileSavePath);
                boolean result = FileUtils.saveFileByStream(inputStream, totalPath);

                if(!result) return ResultVoUtil.error("E111","文件保存到磁盘失败");

                boolean changSuc = false;
                if (suffix.equals("doc") || suffix.equals("docx") || suffix.equals("txt"))  {
                    changSuc = wordToPDF(totalPath, pdfOutPath);
                } else if (suffix.equals("ppt") || suffix.equals("pptx")) {
                    changSuc = pptToPDF(totalPath, pdfOutPath);
                } else if (suffix.equals("xls") || suffix.equals("xlsx")) {
                    changSuc = excelToPDF(totalPath, pdfOutPath);
                } else {
                    return ResultVoUtil.error("W111", "暂不支持该文件格式("+suffix+")转PDF");
                }
                
                if(changSuc) return ResultVoUtil.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultVoUtil.error("0001", "转换失败");
    }


    @Override
    public ResultVo fileUpload(HttpServletRequest request, HttpServletResponse response) {

        String fileSavePath = env.getProperty("manager.save-path");
        OutputStream os = null;
        InputStream inputStream = null;
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 获取上传的文件
            MultipartFile multiFile = multipartRequest.getFile("file");

            assert multiFile != null;
            String fileName = multiFile.getOriginalFilename();
            String pdfOutPath = fileSavePath + fileName;

            FileUtils.createFolder(fileSavePath);
            boolean result = FileUtils.saveFileByStream(multiFile.getInputStream(), pdfOutPath);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("filePath", pdfOutPath);
            jsonObject.put("fileName", fileName);
            if(result) return ResultVoUtil.success("文件上传成功", jsonObject.toJSONString());

        } catch (Exception e) {

        }
        return ResultVoUtil.error("E111","文件上传失败");
    }

    /**
     * 文件填充
     * @param request
     * @param response
     * @return
     */
    @Override
    public void fileFill(HttpServletRequest request, HttpServletResponse response) {

        String filePath = request.getParameter("filePath");
        String fileName = request.getParameter("fileName");
        OutputStream os = null;
        InputStream inputStream = null;
        try {

            File file = new File(filePath);
            inputStream = new FileInputStream(file);

//            //这是直接输出到磁盘
//            FileOutputStream outputStream = new FileOutputStream(pdfOutPath);

            //直接输出
            byte[] byt = new byte[inputStream.available()];
            inputStream.read(byt);

            Map<String, String> data = new HashMap<>();
            data.put("username", "小明");
            data.put("sex", "男");
            data.put("like", "女");

            //通过浏览器输出
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            os = response.getOutputStream();

            boolean result = PDFUtils.fillTemplate(os, byt, data);

            System.out.println("填充成功");
            response.getOutputStream().flush();
            response.getOutputStream().close();
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                    if(os != null)os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public void fileTempFill(HttpServletRequest request, HttpServletResponse response) {

        String filePath = "D:/root/deploy/填充模板.pdf";
        String fileName = "填充模板.pdf";
        String username = request.getParameter("username");
        String sex = request.getParameter("sex");
        String jiguan = request.getParameter("jiguan");
        String time = "2021-01-18";


        OutputStream os = null;
        InputStream inputStream = null;
        try {

            File file = new File(filePath);
            inputStream = new FileInputStream(file);

//            //这是直接输出到磁盘
//            FileOutputStream outputStream = new FileOutputStream(pdfOutPath);

            //直接输出
            byte[] byt = new byte[inputStream.available()];
            inputStream.read(byt);

            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("sex", sex);
            data.put("jiguan", jiguan);
            data.put("time", time);

            //通过浏览器输出
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
            os = response.getOutputStream();

            boolean result = PDFUtils.fillTemplate(os, byt, data);

            System.out.println("填充成功");
            response.getOutputStream().flush();
            response.getOutputStream().close();
            os.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                    if(os != null)os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 图片转PDF(文件流形式)
     * @param multiFile
     * @param pdfOutPath
     * @return
     */
    private boolean pictureToPDF(MultipartFile multiFile, String pdfOutPath) {

        boolean changeResult = false;

        Document doc = null;
        try {
            FileOutputStream fos = new FileOutputStream(pdfOutPath);

            doc = new Document(null, 0, 0, 0, 0);
            // 写入PDF文档
            PdfWriter.getInstance(doc, fos);
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;

            float newHeight = 0f, newWidth = 0f;

            img = ImageIO.read(multiFile.getInputStream());

            Map<String,Float> result = setImageWidthHeight(img.getWidth(), img.getHeight(),newWidth, newHeight);
            newWidth = result.get("width");
            newHeight = result.get("height");
            doc.setPageSize(new Rectangle(newWidth,newHeight));
            image = Image.getInstance(multiFile.getBytes());

            image.scaleAbsolute(new Rectangle(newWidth,newHeight));
            // 添加图片到文档
            doc.newPage();
            doc.open();
            doc.add(image);

            // 关闭文档
            doc.close();

            changeResult = true;
            System.out.println("图片转PDF成功");
        } catch (Exception e) {
            System.out.println("图片转PDF失败");
            e.printStackTrace();
        } finally {
            if(doc != null) doc.close();
        }

        return  changeResult;
    }

    /**
     * 图片转PDF(文件路径形式)
     * @param imagPath
     * @param pdfOutPath
     * @return
     */
    private boolean pictureToPDF(String imagPath, String pdfOutPath) {

        boolean changeResult = false;

        try {
            FileOutputStream fos = new FileOutputStream(pdfOutPath);

            Document doc = new Document(null, 0, 0, 0, 0);
            // 写入PDF文档
            PdfWriter.getInstance(doc, fos);
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;

            float newHeight = 0f, newWidth = 0f;

            img = ImageIO.read(new File(imagPath));

            Map<String,Float> result = setImageWidthHeight(img.getWidth(), img.getHeight(),newWidth, newHeight);
            newWidth = result.get("width");
            newHeight = result.get("height");
            doc.setPageSize(new Rectangle(newWidth,newHeight));
            image = Image.getInstance(imagPath);
            image.scaleAbsolute(new Rectangle(newWidth,newHeight));
            // 添加图片到文档
            doc.newPage();
            doc.open();
            doc.add(image);

            // 关闭文档
            doc.close();

            changeResult = true;
            System.out.println("图片转PDF成功");
        } catch (Exception e) {
            System.out.println("图片转PDF失败");
            e.printStackTrace();
        }

        return  changeResult;
    }

    /**
     * word转PDF
     * @param docPath
     * @param pdfOutPath
     * @return
     */
    private boolean wordToPDF(String docPath, String pdfOutPath) {

        Boolean changeResult = false;
        ActiveXComponent app = null;
        try{
            //打开word应用程序
            app = new ActiveXComponent("Word.Application");
            //设置word不可见
            app.setProperty("Visible", false);
            //获得word中所有打开的文档,返回Documents对象
            Dispatch docs = app.getProperty("Documents").toDispatch();
            //调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            Dispatch doc = Dispatch.call(docs,
                    "Open",
                    docPath,
                    false,
                    true
            ).toDispatch();
            //调用Document对象的SaveAs方法，将文档保存为pdf格式
            Dispatch.call(doc,
                    "ExportAsFixedFormat",
                    pdfOutPath,
                    WORD_FORMAT_PDF		//word保存为pdf格式宏，值为17
            );
            //关闭文档
            Dispatch.call(doc, "Close",false);

            System.out.println("doc文档转PDF成功");
            changeResult = true;
        } catch (Exception e) {
            System.out.println("doc文档转PDF失败");
            e.printStackTrace();
        } finally {
            if (app != null) {
                //关闭word应用程序
                app.invoke("Quit", new Variant[0]);
            }
        }
        //如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
        return changeResult;
    }

    /**
     * Excel转PDF
     * @param inputFile
     * @param pdfOutPath
     */
    private boolean excelToPDF(String inputFile, String pdfOutPath) {

        boolean changeResult = false;
        System.out.println("启动Excel...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch excel = null;
        try {
            // 创建一个excel对象
            app = new ActiveXComponent("Excel.Application");
            // 不可见打开excel
            app.setProperty("Visible", new Variant(false));
            // 获取文挡属性
            Dispatch excels = app.getProperty("Workbooks").toDispatch();
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            excel = Dispatch.call(excels, "Open", inputFile).toDispatch();
            System.out.println("打开文档..." + inputFile);
            System.out.println("转换文档到PDF..." + pdfOutPath);
            File toFile = new File(pdfOutPath);
            if (toFile.exists()) {
                toFile.delete();
            }
            // Excel不能调用SaveAs方法
            Dispatch.call(excel, "ExportAsFixedFormat", EXCEL_FORMAT_PDF, pdfOutPath);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");
            changeResult = true;
        } catch (Exception e) {
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(excel, "Close", false);
            System.out.println("关闭文档");
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        //如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
        
        return changeResult;
    }

    /**
     * ppt转PDF
     * @param inputFile
     * @param pdfFile
     * @return
     */
    private boolean pptToPDF(String inputFile, String pdfFile) {

        boolean changeResult = false;
        
        System.out.println("启动PPT...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch ppt = null;
        try {
            // 创建一个ppt对象
            app = new ActiveXComponent("PowerPoint.Application");
            // 不可见打开（PPT转换不运行隐藏，所以这里要注释掉）
            // app.setProperty("Visible", new Variant(false));
            // 获取文挡属性
            Dispatch ppts = app.getProperty("Presentations").toDispatch();
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            ppt = Dispatch.call(ppts, "Open", inputFile, true, true, false).toDispatch();
            System.out.println("打开文档..." + inputFile);
            System.out.println("转换文档到PDF..." + pdfFile);
            File tofile = new File(pdfFile);
            if(tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(ppt, "SaveAs", pdfFile, PPT_FORMAT_PDF);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");
            changeResult = true;
        } catch (Exception e) {
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(ppt, "Close");
            System.out.println("关闭文档");
            if (app != null)
                app.invoke("Quit", new Variant[] {});
        }
        //如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
        
        return changeResult;
    }

    /**
     * 设置PDF的宽高
     *
     * @param width
     * @param height
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Map<String, Float> setImageWidthHeight(float width, float height, float newWidth, float newHeight) {
        float res = width / height;
        if (res >= 1) { //横向
            if (width > regular_width) {
                newWidth = regular_width;
                newHeight = (regular_width / width) * height;
            } else if (height > regular_height) {
                newHeight = regular_height;
                newWidth = (regular_height / height) * width;
            } else {
                //如果都在范围内，不做任何操作
                newWidth = width;
                newHeight = height;
            }
        } else {
            if (width > vertical_regular_width) {
                newWidth = vertical_regular_width;
                newHeight = (vertical_regular_width / width) * height;
            } else if (height > vertical_regular_height) {
                newHeight = vertical_regular_height;
                newWidth = (vertical_regular_height / height) * width;
            } else {
                //如果都在范围内，不做任何操作
                newWidth = width;
                newHeight = height;
            }
        }
        Map<String, Float> result = new HashMap<String, Float>();
        result.put("width", newWidth);
        result.put("height", newHeight);
        return result;
    }
}


