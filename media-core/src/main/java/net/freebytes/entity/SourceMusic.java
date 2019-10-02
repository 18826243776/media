
package net.freebytes.entity;


public class SourceMusic extends SourceFile {

    private Integer length;
    private String artist;  // 音频作者
    private String album;   // 专辑信息

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
