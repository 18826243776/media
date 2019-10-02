package net.freebytes.config;

/**
 * Created by 千里明月
 * 存储项目设置的环境变量
 */
public class EnvironmentProperties {
    //文件仓库位置
    private static String fileRepo;
    //单个文件最大内存限制
    private static long fileMaxSize;
    //ffmpeg处理器的路径
    private static String ffmpegPath;

    public static String getFfmpegPath() {
        return ffmpegPath;
    }
    public void setFfmpegPath(String ffmpegPath) {
        EnvironmentProperties.ffmpegPath = ffmpegPath;
    }
    public static String getFileRepo() {
        return fileRepo;
    }
    public void setFileRepo(String fileRepo) {
        EnvironmentProperties.fileRepo = fileRepo;
    }
    public static long getFileMaxSize() {
        return fileMaxSize;
    }
    public void setFileMaxSize(long fileMaxSize) {
        EnvironmentProperties.fileMaxSize = fileMaxSize;
    }
}
