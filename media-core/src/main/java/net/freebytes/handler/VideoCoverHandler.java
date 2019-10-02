package net.freebytes.handler;

/**
 * Created by 千里明月 on 2019/1/31.
 */

import com.zhcs.srp.source.entity.SourceVideo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 视频截图处理器  (未测试)
 */
public class VideoCoverHandler extends HandleVideo {
    private static final Logger logger = LoggerFactory.getLogger(VideoCoverHandler.class);

    public VideoCoverHandler(SourceVideo video) {
        super.source = video;
    }

    private String resolutionRatio = "1920*1080";

    public void setResolutionRatio(String resolutionRatio) {
        this.resolutionRatio = resolutionRatio;
    }

    @Override
    protected void handleVideoSource() {
        String videoPath = new File(uploadPath + source.getUrl()).getAbsolutePath();
        int mid = source.getLength() / 2;
        String parentDirectory = new File(videoPath).getParent();
        String coverDirectory = "cover";
        File file = new File(parentDirectory, coverDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }

        String coverPicName = UUID.randomUUID() + ".jpg";
        String savePath = file.getAbsolutePath() + "/" + coverPicName;
        try {
            getVideoCover(videoPath, mid, savePath);
            String newSourceParentUrl = file.getCanonicalPath().replace(new File(uploadPath).getCanonicalPath(), "");
            String newSourceUrl = newSourceParentUrl.replaceAll("\\\\", "/") + "/" + coverPicName;
            source.setSnapshot(newSourceUrl);
        } catch (Exception e) {
            logger.error(videoPath + "-------视频截图失败-------------", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取视频第几秒的截图
     *
     * @param videoPath
     * @param time
     * @param savePath
     */
    private void getVideoCover(String videoPath, int time, String savePath) throws Exception {
        List<String> cmd = new ArrayList<>();
        cmd.add(ffmpegPath);
        cmd.add("-i");
        cmd.add(videoPath);
        cmd.add("-y");
        cmd.add("-f");
        cmd.add("mjpeg");
        cmd.add("-ss");
        cmd.add(String.valueOf(time));
        cmd.add("-t");
        cmd.add("0.001");
        cmd.add("-s");
        cmd.add(resolutionRatio);
        cmd.add(savePath);
        ProcessBuilder builder = new ProcessBuilder();
        Process process;
        try {
            builder.command(cmd);
            builder.redirectErrorStream(true);
            process = builder.start();
        } catch (Exception e) {
            logger.error("", e);
            return;
        }
        if (process.isAlive()) {
            process.waitFor();
        }
    }

}
