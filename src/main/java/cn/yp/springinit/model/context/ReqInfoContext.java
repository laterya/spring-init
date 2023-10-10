package cn.yp.springinit.model.context;

import lombok.Data;

/**
 * @author yp
 * @date: 2023/10/10
 */
public class ReqInfoContext {

    private static ThreadLocal<ReqInfo> contexts = new InheritableThreadLocal<>();

    public static void addReqInfo(ReqInfo reqInfo) {
        contexts.set(reqInfo);
    }

    public static void clear() {
        contexts.remove();
    }

    public static ReqInfo getReqInfo() {
        return contexts.get();
    }

    @Data
    public static class ReqInfo {
        private String token;

        private Long userId;

        private String path;
    }
}
