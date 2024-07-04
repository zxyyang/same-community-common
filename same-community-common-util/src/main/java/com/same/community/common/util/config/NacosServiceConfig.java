package com.same.community.common.util.config;

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
 * 由Zixuan.Yang编写
 * 日期：2024/3/16
 */
@Component
@Slf4j
@Order(1) // 确保这个Runner在其他Runner之前执行
public class NacosServiceConfig implements CommandLineRunner {
    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Override
    public void run(String... args) throws Exception {
        String serviceName = discoveryProperties.getService();
        String group = discoveryProperties.getGroup();

        Instance instance = new Instance();
        instance.setServiceName(serviceName);
        instance.setClusterName(group);

        // 判断当前操作系统是否是linux (线上环境)
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            instance.setIp(IpUtil.externalNetworkIp());
        } else {
            // 本地环境
            instance.setIp("127.0.0.1");
        }

        try {
            // 注册实例
            nacosServiceManager.getNamingService().registerInstance(serviceName, group, instance);
            log.info("NACOS实例注册成功");

            // 获取一个健康的实例
            Instance healthyInstance = nacosServiceManager.getNamingService().selectOneHealthyInstance(serviceName, group);

            // 设置当前实例的权重（如果需要）
            // double newWeight = 10.0; // 你希望设置的新权重
            // healthyInstance.setWeight(newWeight);

            log.info("NACOS获取健康实例成功: {}", healthyInstance);
        } catch (NacosException e) {
            log.error("NACOS操作失败", e);
        }
    }
}
