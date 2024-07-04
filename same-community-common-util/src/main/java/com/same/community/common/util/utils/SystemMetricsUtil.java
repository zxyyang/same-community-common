package com.same.community.common.util.utils;

import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

/**
 * @author Zixuan.Yang
 * @date 2024/3/16 12:43
 */
@Component
public class SystemMetricsUtil {


    private final SystemInfo systemInfo;
    private final HardwareAbstractionLayer hal;
    private final OperatingSystem os;

    public SystemMetricsUtil() {
        this.systemInfo = new SystemInfo();
        this.hal = systemInfo.getHardware();
        this.os = systemInfo.getOperatingSystem();
    }

    /**
     * 获取 CPU 使用率。
     *
     * @return 当前 CPU 使用率 (0.0 到 1.0 之间)
     */
    public double getCpuUsage() {
        CentralProcessor processor = hal.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        return processor.getSystemCpuLoadBetweenTicks(prevTicks);
    }

    /**
     * 获取内存使用率。
     *
     * @return 当前内存的使用率 (0.0 到 1.0 之间)
     */
    public double getMemoryUsage() {
        GlobalMemory memory = hal.getMemory();
        return (double) (memory.getTotal() - memory.getAvailable()) / memory.getTotal();
    }

    /**
     * 获取系统负载平均值。
     *
     * @return 包含最近 1 分钟、5 分钟和 15 分钟平均负载的数组
     */
    public double[] getSystemLoadAverage() {
        return hal.getProcessor().getSystemLoadAverage(3);
    }



    /**
     * 获取物理 CPU 核心数量。
     *
     * @return 物理 CPU 核心数量
     */
    public int getPhysicalProcessorCount() {
        return hal.getProcessor().getPhysicalProcessorCount();
    }

    /**
     * 获取逻辑 CPU 核心数量（可能由于超线程技术而大于物理核心数）。
     *
     * @return 逻辑 CPU 核心数量
     */
    public int getLogicalProcessorCount() {
        return hal.getProcessor().getLogicalProcessorCount();
    }

    /**
     * 获取总物理内存大小。
     *
     * @return 总物理内存大小（以字节为单位）
     */
    public long getTotalMemorySize() {
        return hal.getMemory().getTotal();
    }

    // 其他系统指标方法...


}
