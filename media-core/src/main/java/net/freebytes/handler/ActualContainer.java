package net.freebytes.handler;

/**
 * @version 1.0
 * @author: 千里明月
 * @date: 2019/4/3 14:39
 */

/**
 *
 * 将资源放入容器中，
 * 容器拒绝一个正在处理中的资源被再次处理。
 *
 * @param <T>
 */
public interface ActualContainer<T> {
    /**
     *   将资源置入容器,  由默认冲突处理策略去处理
     * @param resource  资源的标识
     * @throws HandleException   如果冲突解决成功，则将资源置入容器；如果处理失败则直接抛出运行时异常
     */
    void access(T resource) throws HandleException;

    /**
     *将资源置入容器, 由指定冲突处理策略去处理
     * @param resource  资源的标识
     * @param handlerStrategy
     */
    void access(T resource, ConflicResourceHandlerStrategy handlerStrategy) throws HandleException;


    interface ConflicResourceHandlerStrategy<T> {
        void handleConflic(T resource);
    }

    /**
     * 强制删除容器中的资源
     * @param resource
     * @return
     */
    boolean remove(T resource);
}
