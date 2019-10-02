package net.freebytes.entity;

/**
 * Created by 千里明月 on 2018/11/7.
 */
public class SourceVideo extends SourceFile {
    private Integer length;
    private String resolutionRatio;
    private String snapshot;
    private String width;
    private String height;
    private Boolean useful = false;
    private String m3u8Url;


    public Boolean getUseful() {
        return useful;
    }

    public void setUseful(Boolean useful) {
        this.useful = useful;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getResolutionRatio() {
        return resolutionRatio;
    }

    public void setResolutionRatio(String resolutionRatio) {
        this.resolutionRatio = resolutionRatio;
    }

    public String getM3u8Url() {
        return m3u8Url;
    }

    public void setM3u8Url(String m3u8Url) {
        this.m3u8Url = m3u8Url;
    }
}
