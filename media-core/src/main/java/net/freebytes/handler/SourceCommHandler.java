package net.freebytes.handler;

import net.freebytes.config.EnvironmentProperties;
import net.freebytes.entity.SourceParent;

/**
 * @version 1.0
 * @author: 千里明月
 * @date: 2019/10/2 18:15
 */
public abstract class SourceCommHandler<T extends SourceParent> implements SourceHandler {
    protected T source;
    protected String sourcePath = EnvironmentProperties.getFileRepo();
    protected String outputPath;

    @Override
    public T handleSource() {
        return null;
    }
}
