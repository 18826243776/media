package net.freebytes.handler;

import java.util.HashSet;

/**
 * Created by 千里明月 on 2019/1/31.
 */
public class SourceTypes {

    public final static HashSet sourceTypes = new HashSet<String>(8) {{
        add("doc");

        add("music");

        add("video");

        add("picture");
    }};

    public static boolean register(String sourceType) {
        if (!validateSourceType(sourceType)) {
            return false;
        }
        if (exitSourceType(sourceType)) {
            return true;
        }
        return sourceTypes.add(sourceType);
    }

    public static boolean validateSourceType(String sourceType) {
        return !(sourceType == null || sourceType.trim().length() == 0);
    }

    public static boolean exitSourceType(String sourceType) {
        return sourceTypes.contains(sourceType);
    }
}
