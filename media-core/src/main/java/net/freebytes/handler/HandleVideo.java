package net.freebytes.handler;

import net.freebytes.config.EnvironmentProperties;
import net.freebytes.entity.SourceVideo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 千里明月 on 2018/11/13.
 * 视频处理器
 */
public abstract class HandleVideo extends SourceCommHandler<SourceVideo> {

    //    private static final Logger logger = LoggerFactory.getLogger(HandleVideo.class);
    protected String ffmpegPath = EnvironmentProperties.getFfmpegPath();
    private static String[] encodes = {"h264"};
    private StateListener stateListener = null;
    protected ActualContainer sourceContainer = ActualVideoContainer.get();

    public HandleVideo setSourceContainer(ActualContainer sourceContainer) {
        this.sourceContainer = sourceContainer;
        return this;
    }

    public HandleVideo setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
        return this;
    }

    public void setEncodes(String[] encodes) {
        HandleVideo.encodes = encodes;
    }

    private VideoInstance videoInstance;

    protected class VideoInstance {
        private int duration;
        private String encode;
        private String size;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getEncode() {
            return encode;
        }

        public void setEncode(String encode) {
            this.encode = encode;
        }
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setFfmpegPath(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
    }

    protected SourceVideo init(String url) {
//        if (source.getInitial()) {
//            return;
//        }
//        File file = new File(uploadPath, url);
//        String suffix = FileTypeAnalyse.getSuffix(file);
//        source.setSuffix(suffix);
//        source.setFile(file);
//        source.setSourceType(SourceTypes.getVideoType());
//        analyseDetail();
//        source.setInitial(true);
        return null;
    }

    @Override
    public SourceVideo handleSource() {
//        String resource = source.getUrl();
//        if (sourceContainer != null) {
//            sourceContainer.access(resource);
//        }
//        try {
//            handleVideoSource();
//        } catch (Exception e) {
//            logger.error("处理视频源失败", e);
//        } finally {
//            sourceContainer.remove(resource);
//        }
        return null;
    }

    abstract void handleVideoSource();

    /**
     * 视频信息深度校验
     *
     * @return
     */
    protected boolean validate() {
        return true;
    }

    /**
     * 解析视频的详细信息并设值
     *
     * @return
     */
    protected SourceVideo analyseDetail() {
        File inputFile = source.getFile();
        if (inputFile == null || !validate()) {
            return null;
        }
        String inputFilePath = new File(uploadPath + source.getUrl()).getAbsolutePath();
        VideoInstance videoInstance = getInfoFromFFmpeg(inputFilePath);
        if (videoInstance == null) {
            return null;
        }
        String encode = videoInstance.getEncode();
        if (StringUtils.isBlank(encode)) {
            return null;
        }

        source.setLength(videoInstance.getDuration());
        source.setSize(inputFile.length() + "");
        for (String formatName : encodes) {
            if (formatName.startsWith(encode)) {
                source.setUseful(true);
                return source;
            }
        }
        source.setUseful(false);
        return source;
    }


    private VideoInstance getInfoFromFFmpeg(String inputFilePath) {
        List<String> commands = new ArrayList<>();
        commands.add(ffmpegPath);
        commands.add("-i");
        commands.add(inputFilePath);
        ProcessBuilder builder = new ProcessBuilder();
        Process p;
        builder.command(commands);
        builder.redirectErrorStream(true);
        try {
            p = builder.start();
        } catch (IOException e) {
            logger.error("", e);
            return null;
        }

        StringBuilder sb = new StringBuilder();
        //从输入流中读取视频信息
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
//                System.out.println(line);
            }
            if (p.isAlive()) {
                p.waitFor();
            }
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }

        //从视频信息中解析时长和编码
        String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
        String regexEncode = "Video: (.*?),";

        Matcher m = Pattern.compile(regexDuration).matcher(sb.toString());
        videoInstance = new VideoInstance();
        if (m.find()) {
            videoInstance.setDuration(getTimelen(m.group(1)));
        }

        Matcher matcher = Pattern.compile(regexEncode).matcher(sb.toString());
        if (matcher.find()) {
            videoInstance.setEncode(matcher.group(1));
        }
        return videoInstance;
    }


    //格式:"00:00:10.68" 转化为秒数
    protected int getTimelen(String timelen) {
        int min = 0;
        String strs[] = timelen.split(":");
        if (strs[0].compareTo("0") > 0) {
            min += Integer.valueOf(strs[0]) * 60 * 60;//秒
        }
        if (strs[1].compareTo("0") > 0) {
            min += Integer.valueOf(strs[1]) * 60;
        }
        if (strs[2].compareTo("0") > 0) {
            min += Math.round(Float.valueOf(strs[2]));
        }
        return min;
    }

    public void deleteFileOnSecure(File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            logger.warn(file.getName() + "--临时文件,删除失败", e);
        }
    }


    public static String[] getFormatNames() {
        return new String[]{"mp4", "avi", "mpeg"};
    }
}
