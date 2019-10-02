package net.freebytes.handler;

import com.zhcs.srp.source.entity.SourceVideo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 千里明月 on 2018/4/16.
 */
public class TranscodeHandler extends HandleVideo {

    private static final Logger logger = LoggerFactory.getLogger(TranscodeHandler.class);

    private ProgressListener progressListener;

    private M3u8Handler m3u8Handler = new M3u8Handler();

    public TranscodeHandler setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    public TranscodeHandler(SourceVideo video) {
        source = video;
        init(source.getUrl());
    }

    @Override
    protected void handleVideoSource() {
        if (source.getUseful()) {
            return;
        }
        File file = source.getFile();
        String name = file.getName();
        String inputFilePath = file.getAbsolutePath();
        name = "tran_" + name;
        name = name.replace(name.substring(name.lastIndexOf(".") + 1, name.length()), "mp4");
        String outputFilePath = new File(file.getParent(), name).getAbsolutePath();
        // 视频转码h264
        VideoInstance videoInstance = transcoding(inputFilePath, outputFilePath);
        if (videoInstance != null) {
            source.setLength(videoInstance.getDuration());
            source.setSize(videoInstance.getSize());
            source.setUseful(true);
            source.setSuffix(FileTypeAnalyse.getSuffix(name));
            String newUrl = source.getUrl().replace(file.getName(), name);
            source.setUrl(newUrl);
        }
        // h264视频-->m3u8
//        Result<String> result = m3u8Handler.videoToM3u8(source);
//        if(result.getResult()) {
//            String m3u8Url = result.getT();
//            source.setM3u8Url(m3u8Url);
//        }
    }

    /**
     * 视频转码  默认转成h264编码
     *
     * @param inputFilePath
     * @param outputFilePath
     * @return
     */
    protected VideoInstance transcoding(String inputFilePath, String outputFilePath) {
        if (new File(outputFilePath).exists()) {
            throw new RuntimeException("输出文件名有重复");
        }
        List<String> commands = new ArrayList<>();
        commands.add(ffmpegPath);
        commands.add("-i");
        commands.add(inputFilePath);
        commands.add("-c:v");
        commands.add("libx264");
//        commands.add("-preset");
//        commands.add("fast");
        commands.add("-r");
        commands.add("23");
        commands.add("-c:a");
        commands.add("copy");
        commands.add(outputFilePath);
        logger.info("视频转码:" + commands);
        //模拟cmd指令发送
        ProcessBuilder builder = new ProcessBuilder();
        Process p;
        try {
            builder.command(commands);
            builder.redirectErrorStream(true);
            p = builder.start();
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }

        StringBuilder sb = new StringBuilder();
        //从输入流中读取视频信息
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))
        ) {
            String line = "1";
            while (StringUtils.isNotBlank(line)) {
                line = br.readLine();
                sb.append(line);
                logger.info(line);
                if (progressListener != null) {
                    progressListener.update(new DiscriminateHelper(line).discriminate(), source.getLength());
                }
            }
            if (p.isAlive()) {
                p.waitFor();
            }
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
        String regexDuration = "Lsize=(.*?)kB time=(.*?) ";
        Pattern pattern = Pattern.compile(regexDuration);
        Matcher m = pattern.matcher(sb.toString());
        VideoInstance videoInstance = null;
        if (m.find()) {
            videoInstance = new VideoInstance();
            long size = Long.valueOf(m.group(1).trim()) * 1024;
            int duration = getTimelen(m.group(2));
            videoInstance.setSize(size + "");
            videoInstance.setDuration(duration);
        }

        return videoInstance;
    }


    /**
     * 分析当前进度
     */
    private class DiscriminateHelper {
        private String input;
        private Integer output;

        public DiscriminateHelper(String input) {
            this.input = input;
        }

        public Integer discriminate() {
            //todo
            return output;
        }
    }


}
