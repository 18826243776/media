package net.freebytes.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version 1.0
 * @author: 千里明月
 * @date: 2019/4/10 10:53
 * <p>
 * 针对视频文件处理的容器
 * <p>
 * 需要手动控制容器内的资源增减，继而判断该资源是否能够被处理。
 */
public class ActualVideoContainer implements ActualContainer<String> {

    private static final Logger logger = LoggerFactory.getLogger(ActualVideoContainer.class);

    private ActualVideoContainer() {
    }

    private static ActualContainer container = new ActualVideoContainer();

    public static ActualContainer get() {
        return container;
    }

    /**
     * 存放视频文件url的线程安全的set集合
     */
    private Set<String> sourceFileSet = Collections.newSetFromMap(new ConcurrentHashMap(1000));

    @Override
    public void access(String resource) throws HandleException {
        access(resource, (s) -> {
            long begin = System.currentTimeMillis();
            boolean flag = true;
            while (flag) {
                try {
                    wait(1000);
                    long end = System.currentTimeMillis();
                    if ((end - begin) > 1000 * 60 * 5) {
                        throw new RuntimeException(resource+"----文件处理时间超过5分钟");
                    }
                    flag = sourceFileSet.contains(s) == true ? true : false;
                } catch (InterruptedException e) {
                    throw new HandleException(e);
                }
            }
        });
    }

    @Override
    public void access(String resource, ConflicResourceHandlerStrategy strategy) throws HandleException {
        if (strategy == null) {
            throw new HandleException("冲突处理策略不能为空");
        }
        synchronized (this.getClass()) {
            if (sourceFileSet.contains(resource)) {
                strategy.handleConflic(resource);
            }
            sourceFileSet.add(resource);
        }
    }

    @Override
    public boolean remove(String resource) {
        if (resource == null) {
            throw new HandleException("资源标识不允许为空");
        }
        synchronized (this.getClass()) {
            return sourceFileSet.remove(resource);
        }
    }
}

