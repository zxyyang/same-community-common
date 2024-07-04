package com.same.community.common.meta.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.same.community.common.meta.context.RequestContext;
import com.same.community.common.meta.constants.AppConst;
import com.same.community.common.meta.context.UserContext;
import com.same.community.common.meta.model.SameUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@Slf4j
@Component
public class FeignClientFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String userJson = request.getHeader(AppConst.USER_CONTEXT);
        if (StrUtil.isNotBlank(userJson)) {
            try {
                userJson = URLDecoder.decode(userJson, "UTF-8");
                SameUserInfo userInfo = JSON.parseObject(userJson, SameUserInfo.class);
                // 将UserInfo放入上下文中
                UserContext.setUser(userInfo);
            } catch (Exception e) {
                log.error("初始化上下文异常", e);
            }
        }

        String remoteIP = request.getHeader(AppConst.REMOTE_ADDR);
        if (StrUtil.isNotBlank(remoteIP)) {
            RequestContext.setRemoteIp(remoteIP);
        }

        try {
            filterChain.doFilter(request, response);
        }
        finally {
            UserContext.clear(); // 清空上下文
            RequestContext.clear();
        }

    }

}
