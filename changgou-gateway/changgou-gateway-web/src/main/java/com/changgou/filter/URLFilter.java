package com.changgou.filter;

/**
 * 不需要认证就能访问的路径校验
 */
public class URLFilter {

    private static final String allUrl = "/user/login,/api/user/add";

    /**
     * 校验当前访问路径是否需要校验权限
     * 如果不需要验证： false
     * 需要验证： true
     * @param url
     * @return
     */
    public static boolean hasAuthorize(String url){
        //不需要拦截的URL
        String[] urls = allUrl.split(",");

        for(String uri: urls){
            if (url.equals(uri)){
                return false;
            }
        }
        return true;
    }
}
