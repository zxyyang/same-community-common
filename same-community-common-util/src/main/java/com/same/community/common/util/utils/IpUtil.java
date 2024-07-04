package com.same.community.common.util.utils;

import cn.hutool.core.util.StrUtil;
import com.same.community.common.meta.context.RequestContext;
import com.same.community.common.meta.model.IP;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author Zixuan.Yang
 * @date 2023/12/14 18:46
 */
@Slf4j
public class IpUtil {

    private static final String LOCAL_IP = "127.0.0.1";
    private static final String IP_UTILS_FLAG = ",";
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private static final String IP_DATA_PATH = "/ip/ip2region.xdb";

    public static final String EXTERNAL_NETWORK_API_URL = "http://api.ipify.org";

    private static Searcher _searcher = null;

    static {
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(IP_DATA_PATH);
            inputStream = classPathResource.getInputStream();
            byte[] cBuff;
            cBuff = FileUtil.readToByte(inputStream);
            _searcher = Searcher.newWithBuffer(cBuff);
            log.info("IP信息初始化完成---------------------");
        } catch (Exception e) {
            log.error("初始化ip信息失败:{}", e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 获取IP地址 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr() {
        String ip = RequestContext.getRemoteIp();
        if (ip == null) {
            return "unknown";
        }

        String remoteIp = "0:0:0:0:0:0:0:1".equals(ip) ? LOCAL_IP : ip;
        String[] split = remoteIp.split(",");
        String targetIp = null;
        for (String ipOne : split) {
            if (!"unknown".equals(ipOne)) {
                targetIp = ipOne;
                break;
            }
        }
        return targetIp;
    }


    public static String search(String ip) {
        IP geography = findGeography(ip);
        if (geography == null) {
            return null;
        }
        return geography.getCity();
    }


    /**
     * 根据请求IP 获取省份和城市信息
     *
     * @return {@code String}
     */
    public static Pair<String, String> findProvinceAndCity() {
        String remoteIp = getIpAddr();
        if (StrUtil.isBlank(remoteIp)) {
            return null;
        }

        IP geography = findGeography(remoteIp);
        if (geography == null) {
            return null;
        }

        return Pair.of(geography.getProvince(), geography.getCity());
    }


    public static IP findGeography(String remote) {
        IP ip = null;
        if (StrUtil.isBlank(remote)) {
            return IP.Local();
        }
        try {
            String block = _searcher.search(remote);
            if (block != null) {
                ip = new IP();
                String[] region = block.split("[\\|]");
                if (region.length == 5) {
                    ip.setCountry(region[0]);
                    if (!StringUtils.isBlank(region[1]) && !region[1].equalsIgnoreCase("null")) {
                        ip.setRegion(region[1]);
                    } else {
                        ip.setRegion("");
                    }
                    if (!StringUtils.isBlank(region[2]) && !region[2].equalsIgnoreCase("null")) {
                        ip.setProvince(region[2]);
                    } else {
                        ip.setProvince("");
                    }
                    if (!StringUtils.isBlank(region[3]) && !region[3].equalsIgnoreCase("null")) {
                        ip.setCity(region[3]);
                    } else {
                        ip.setCity("");
                    }
                    if (!StringUtils.isBlank(region[4]) && !region[4].equalsIgnoreCase("null")) {
                        ip.setIsp(region[4]);
                    } else {
                        ip.setIsp("");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("search ip error", ex);
        }
        return ip;
    }

    public static String getIpAddr(String remote) {
        String ipAddr = "未知";
        IP geography = findGeography(remote);
        if (Objects.isNull(geography)) {
            return ipAddr;
        }
        ipAddr = geography.getProvince();
        if (StrUtil.isBlank(ipAddr)) {
            ipAddr = "未知";
        }

        return ipAddr;
    }
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            //以下两个获取在k8s中，将真实的客户端IP，放到了x-Original-Forwarded-For。而将WAF的回源地址放到了 x-Forwarded-For了。
            ip = request.getHeader("X-Original-Forwarded-For");
            if (org.springframework.util.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            //获取nginx等代理的ip
            if (org.springframework.util.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            if (org.springframework.util.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (org.springframework.util.StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (org.springframework.util.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (org.springframework.util.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            //兼容k8s集群获取ip
            if (org.springframework.util.StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (LOCAL_IP.equalsIgnoreCase(ip) || LOCALHOST_IP.equalsIgnoreCase(ip)) {
                    //根据网卡取本机配置的IP
                    InetAddress iNet = null;
                    try {
                        iNet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error("getClientIp error: {}", e);
                    }
                    ip = iNet.getHostAddress();
                }
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        //使用代理，则获取第一个IP地址
        if (!org.springframework.util.StringUtils.isEmpty(ip) && ip.indexOf(IP_UTILS_FLAG) > 0) {
            ip = ip.substring(0, ip.indexOf(IP_UTILS_FLAG));
        }
        log.info("获取客户端IP：[{}]",ip);
        return ip;
    }

    public static String getLocalIP() {
        String ip = "";
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            InetAddress addr;
            try {
                addr = InetAddress.getLocalHost();
                ip = addr.getHostAddress();
            } catch (UnknownHostException e) {
                log.error("获取失败", e);
            }
            return ip;
        } else {
            try {
                Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
                while (e1.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) e1.nextElement();
                    if (!ni.getName().equals("eth0")) {
                        continue;
                    } else {
                        Enumeration<?> e2 = ni.getInetAddresses();
                        while (e2.hasMoreElements()) {
                            InetAddress ia = (InetAddress) e2.nextElement();
                            if (ia instanceof Inet6Address) {
                                continue;
                            }
                            ip = ia.getHostAddress();
                            return ip;
                        }
                        break;
                    }
                }
            } catch (SocketException e) {
                log.error("获取失败", e);
            }
        }
        return "";
    }


    /**
     * 外部网络 IP
     *
     * @return {@code String}
     */
    public static String externalNetworkIp() {
        return HttpClientUtils.doGet(EXTERNAL_NETWORK_API_URL, null);
    }

    public static void main(String[] args) {
//        System.err.println(findGeography("218.81.10.32"));
        System.err.println(externalNetworkIp());
    }

}