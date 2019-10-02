package net.freebytes.handler;

/**
 * @version 1.0
 * @author: 千里明月
 * @date: 2019/4/10 11:32
 * <p>
 * 源的处理状态监听
 * <p>
 * 不可用状态   -1
 * 已完成状态   1
 * 执行中状态   0
 */
public interface StateListener {
    int STATE_COMPLETE = 1, STATE_EXECUTING = 0, STATE_UNABLE = -1;

    void update(int state);
}
