package com.same.community.common.meta.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.same.community.common.meta.context.RequestContext;
import com.same.community.common.meta.constants.AppConst;
import com.same.community.common.meta.context.UserContext;
import com.same.community.common.meta.model.SameUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class FeignClientFilter extends OncePerRequestFilter {

    @Autowired
    private Environment environment;

    private boolean isLocalProfileActive;
    @PostConstruct
    public void init() {
        String[] activeProfiles = environment.getActiveProfiles();
        isLocalProfileActive = activeProfiles != null && activeProfiles.length >0  && Arrays.asList(activeProfiles).contains("local");
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (isLocalProfileActive) {
            // 如果是本地调试环境，注入默认的用户信息
            log.debug("本地调试环境，注入默认的用户信息:123");
            SameUserInfo defaultUser = new SameUserInfo();
            defaultUser.setUid(123L);
            UserContext.setUser(defaultUser);
        }else {
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
