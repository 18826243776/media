package net.freebytes.handler;

import com.zhcs.sb.common.common.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @author: 千里明月
 * @date: 2019/7/3 15:08
 */
public class FileInputFillter {

    public static Result valid(MultipartFile file, String fileType) {
        Result result = new Result(false);
        boolean exit = SourceTypes.exist(fileType);
        if (!exit) {
            result.setMessage("文件类型不合法");
            return result;
        }
        String type = FileTypeAnalyse.analyse(file);
        if (FileTypeAnalyse.UNDIFINE_TYPE.equals(type)) {
            result.setMessage("文件类型不合法");
            return result;
        }
        String suffix = FileTypeAnalyse.getSuffix(file);
        if (SourceTypes.getVideoType().equals(type)) {
            List<String> list = Arrays.asList(HandleVideo.getFormatNames());
            if (!list.contains(suffix)) {
                result.setMessage("文件类型不合法, 暂时支持的视频类型有：" + list);
                return result;
            }
            result.setResult(true);
        } else if (SourceTypes.getPictureType().equals(type)) {
            List<String> list = Arrays.asList(HandlePicture.getFormatNames());
            if (!list.contains(suffix)) {
                result.setMessage("文件类型不合法, 暂时支持的图片类型有：" + list);
                return result;
            }
            result.setResult(true);
        } else if (SourceTypes.getMusicType().equals(type)) {
            List<String> list = Arrays.asList(HandlerMusic.getFormatNames());
            if (!list.contains(suffix)) {
                result.setMessage("文件类型不合法, 暂时支持的音频类型有：" + list);
                return result;
            }
            result.setResult(true);
        } else if (SourceTypes.getDocType().equals(type)) {
            List<String> list = Arrays.asList(HandleDoc.getFormatNames());
            if (!list.contains(suffix)) {
                result.setMessage("文件类型不合法, 暂时支持的文档类型有：" + Arrays.asList(HandleDoc.getFormatNames()));
                return result;
            }
            result.setResult(true);
        }
        return result;
    }
}
