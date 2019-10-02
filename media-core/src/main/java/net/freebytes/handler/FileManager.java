package net.freebytes.handler;

import net.freebytes.config.EnvironmentProperties;
import net.freebytes.entity.SourceFile;

import java.io.*;
import java.util.Calendar;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by 千里明月
 * 文件管理
 */
public class FileManager {

    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    //文件仓库目录
    private static final String fileRepo = EnvironmentProperties.getFileRepo();
    //文件相对于仓库的存储目录
    private String fileStoreDirectory;
    //文件名
    private String storeName;
    //文件
    private File file;
    //文件类型 默认为common
    private String type = "common";

    public FileManager(File file) {
        if (!validateFile(file)) {
            return;
        }
        this.fileStoreDirectory = initStoreDirectory(type);
        this.storeName = UUID.randomUUID().toString().replaceAll("-", "") + "." + FileTypeAnalyse.getSuffix(file);
        this.file = file;
    }

    public FileManager(File file, String sourceType) {
        if (!SourceTypes.exitSourceType(sourceType) || !validateFile(file)) {
            return;
        }
        this.fileStoreDirectory = initStoreDirectory(sourceType);
        this.storeName = UUID.randomUUID().toString().replaceAll("-", "") + "." + FileTypeAnalyse.getSuffix(file);
        this.file = file;
        this.type = sourceType;
    }

    private boolean validateFile(File file) {
        if (file == null || file.length() == 0) {
            logger.warning("文件不能为空");
            return false;
        }
        if (file.getName().length() > 40) {
            return false;
        }
        if (file.length() > EnvironmentProperties.getFileMaxSize()) {
            return false;
        }
        return true;
    }

    private String initStoreDirectory(String typePath) {
        File typeDirectory = new File(fileRepo, typePath);
        if (!typeDirectory.exists()) {
            typeDirectory.mkdirs();
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String timePath = year + "" + (month < 10 ? "0" + month : month);
        File storeDirectory = new File(typeDirectory, timePath);
        if (!storeDirectory.exists()) {
            storeDirectory.mkdirs();
        }
        return "/" + typePath + "/" + timePath + "/";
    }


    public SourceFile store() {
        String originalFilename = file.getName();
        File file = new File(new File(fileRepo, fileStoreDirectory), storeName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            //logger.er(originalFilename + "文件持久化出错", e);
            return null;
        }
        SourceFile fileInstance = new SourceFile();
        fileInstance.setUrl(fileStoreDirectory + storeName);
        fileInstance.setSourceType(type);
        fileInstance.setOriginName(getFileNameWithoutSuffix(originalFilename));
        fileInstance.setStoreName(storeName);
        return fileInstance;
    }

    public boolean store(InputStream inputStream) {
        try (
                InputStream input = inputStream;
                BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
                FileOutputStream fileOutputStream = new FileOutputStream(fileStoreDirectory);
        ) {
            byte[] buffer = new byte[8092];
            int c = -1;
            while ((c = bufferedInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getFileNameWithoutSuffix(String originName) {
        return originName.substring(0, originName.lastIndexOf("."));
    }

    public static String getFileNameFromUrl(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1, url.length());
        return name;
    }

    /**
     * 删除源文件
     *
     * @param urls
     */
    public static void delete(String... urls) {
        for (String url : urls) {
            if (url == null || url.trim().length() == 0) {
                continue;
            }
            File file = new File(fileRepo, url);
            if (file.exists()) {
                try {
                    file.delete();
                } catch (Exception e) {
//                    logger.error("删除文件出错--" + url);
                    e.printStackTrace();
                }
            }
        }

    }


}
