package net.freebytes.handler;

/**
 * Created by 千里明月 on 2019/2/19.
 * 源处理进度监听
 */
public interface ProgressListener {

    void update(int hasRead, int total);
    void update(float hasRead, float total);
}
