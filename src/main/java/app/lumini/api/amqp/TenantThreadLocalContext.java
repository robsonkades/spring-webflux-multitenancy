package app.lumini.api.amqp;

public class TenantThreadLocalContext {
    public static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static void set(String tenantInformation) {
        threadLocal.set(tenantInformation);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static String get() {
        return threadLocal.get();
    }
}
