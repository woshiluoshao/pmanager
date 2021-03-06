package jp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 文件处理工具类
 */
public class FileUtils {

    public static String createFolder(String path,String folderName) {
        File folder = new File(path+File.separator+folderName);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    public static void createFolder(String path) {
        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件流保存到服务磁盘
     * @param inputStream
     * @param filePath
     * @return
     */
    public static boolean saveFileByStream(InputStream inputStream, String filePath) {

        boolean result = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            byte buffer[] = new byte[inputStream.available()];
            int length = 0;
            while((length = inputStream.read(buffer))>0){
                fos.write(buffer, 0, length);
            }
            //关闭输入流
            inputStream.close();
            fos.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }
                if(inputStream !=null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
            }
        }

        return result;
    }

    public static String getFileHash(String path) {

        String hash = "";
        try {
            //String path = "D:/test.pdf";
            File file = new File(path);
            String fileName = file.getName();
            Long length = file.length();
            InputStream inputStream2 = new FileInputStream(path);
            hash = DigestUtils.sha256Hex(inputStream2).toUpperCase();

            System.out.println("文件名:" + fileName);
            System.out.println("文件长度:" + length);
            System.out.println("文件hash:" + hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;

    }

    public static void main(String[] args) {
        String path = "D:/test.pdf";
        getFileHash(path);
    }

}
