package com.same.community.common.util.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.same.community.common.util.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Zixuan.Yang
 * @date 2024/3/16 12:05
 */
@Component
@Slf4j
@Order(1)
public class NacosServiceConfig implements CommandLineRunner {
    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Override
    public void run(String... args) throws Exception {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String serviceName = discoveryProperties.getService();
        String group = discoveryProperties.getGroup();
        Instance instance;
        try {
            instance = nacosServiceManager.getNamingService().selectOneHealthyInstance(serviceName, group);
        } catch (NacosException e) {
            log.error("NACOS获取实例失败:{}", ExceptionUtil.stacktraceToString(e));
            throw new RuntimeException(e);
        }

        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            instance.setIp(IpUtil.externalNetworkIp());
            try {
                nacosServiceManager.getNamingService().registerInstance(serviceName, group, instance);
                log.info("NACOS更新实例权重信息成功");
            } catch (NacosException e) {
                log.error("NACOS更新实例失败", e);
            }
        } else {
            instance.setIp("127.0.0.1");
            try {
                nacosServiceManager.getNamingService().deregisterInstance(serviceName, group, instance.getIp(), instance.getPort());
                log.info("本地环境，实例已下线");
            } catch (NacosException e) {
                log.error("本地环境实例下线失败", e);
            }
        }
    }
}

