package io.factorialsystems.msscstore21users.security;


public class TenantContext {
    private static final ThreadLocal<String> ctx = new ThreadLocal<>();

    public static String getCurrentTenant() {

        return ctx.get();
    }

    public static void setContext(String context ) {
       ctx.set(context);
    }

    public static void clear() {

        ctx.remove();
    }
}
