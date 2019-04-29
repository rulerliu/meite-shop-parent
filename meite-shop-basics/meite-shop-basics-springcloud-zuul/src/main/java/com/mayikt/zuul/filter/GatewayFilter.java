package com.mayikt.zuul.filter;

import com.mayikt.zuul.build.GatewayBuilderDirector;
import com.mayikt.zuul.mapper.BlacklistMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: liuwq
 * @date: 2019/4/29 0029 20:58
 * @version: V1.0
 * @Copyright:该项目“基于SpringCloud2.x构建微服务电商项目”由每特教育|蚂蚁课堂版权所有，未经过允许的情况下， 私自分享视频和源码属于违法行为。
 */
@Component
@Slf4j
public class GatewayFilter extends ZuulFilter {

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Autowired
    private GatewayBuilderDirector gatewayBuilderDirector;

    /**
     * 请求之前拦截处理业务逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        // 获取请求对象
        HttpServletRequest request = context.getRequest();
        // 获取响应对象
        HttpServletResponse response = context.getResponse();
        response.setContentType("UTF-8");

        // 获取客户端真实ip地址
        String ipAddres = getIpAddr(request);
        if (StringUtils.isEmpty(ipAddres)) {
            resultError(context, "未能够获取到ip地址");
        }

        /*MeiteBlacklist blacklist = blacklistMapper.findBlacklist(ipAddres);
        if (blacklist != null) {
            resultError(context, "ip:" + ipAddres + "don't have access rights");
        }

        Map<String, String> verifyMap = SignUtil.toVerifyMap(request.getParameterMap(), false);
        if (!SignUtil.verify(verifyMap)) {
            resultError(context, "ip:" + ipAddres + ",sign failed");
        }*/

        gatewayBuilderDirector.direcot(context, ipAddres , response, request);

        return null;
    }

    /**
     * 请求之前拦截
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 获取Ip地址
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private void resultError(RequestContext context, String errorMsg) {
        context.setResponseStatusCode(401);
        // 网关响应为false，不会转发服务
        context.setSendZuulResponse(false);
        context.setResponseBody(errorMsg);

    }

}
