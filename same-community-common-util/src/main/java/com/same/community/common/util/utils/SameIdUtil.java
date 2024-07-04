package com.same.community.common.util.utils;



import com.same.community.common.meta.enums.IdEnum;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Zixuan.Yang
 * @date 2024/1/9 01:24
 */
public class SameIdUtil {

    private static final long EPOCH = 1609459200000L; // 设置起始时间戳，这里使用2021-01-01 00:00:00的时间戳
    private static final int MACHINE_ID_BITS = 10; // 机器标识位数
    private static final int MAX_MACHINE_ID = (int) Math.pow(2, MACHINE_ID_BITS) - 1; // 最大机器标识
    private static final int RANDOM_BITS = 15; // 随机数位数
    private static final int MAX_RANDOM = (int) Math.pow(2, RANDOM_BITS) - 1; // 最大随机数

    private static long lastTimestamp = -1L;
    private static long machineId = getMachineId();
    private static long randomSequence = 0L;
    private static final Object lock = new Object();
    private static final SecureRandom random = new SecureRandom();


    // 获取机器标识
    private static long getMachineId() {
        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            return ((long) hardwareAddress[hardwareAddress.length - 1] & 0xFF) % (MAX_MACHINE_ID + 1);
        } catch (Exception e) {
            // 异常时使用随机数
            return new SecureRandom().nextInt((int) Math.pow(2, MACHINE_ID_BITS));
        }
    }

    private static long generateUniqueId(long prefix) {

        long timestamp = System.currentTimeMillis() - EPOCH;

        synchronized (lock) {
            if (timestamp < lastTimestamp) {
                throw new IllegalStateException("Clock moved backwards, unable to generate unique ID");
            }

            if (timestamp == lastTimestamp) {
                randomSequence = (randomSequence + 1) & MAX_RANDOM;
                if (randomSequence == 0) {
                    // 如果随机数溢出，等待下一毫秒
                    timestamp = waitNextMillis(lastTimestamp);
                }
            } else {
                randomSequence = new SecureRandom().nextInt((int) Math.pow(2, RANDOM_BITS));
            }

            lastTimestamp = timestamp;
        }

        // 按照要求的长度拼接ID
        long result = (timestamp << (MACHINE_ID_BITS + RANDOM_BITS)) | (machineId << RANDOM_BITS) | randomSequence;

        // 返回固定12位长度的ID
        result = result % 100000000000L;
        // 添加前缀
        result = Long.parseLong(String.valueOf(prefix) + String.valueOf(result));
        return result;
    }

    private static long generateUniqueId() {

        long timestamp = System.currentTimeMillis() - EPOCH;

        synchronized (lock) {
            if (timestamp < lastTimestamp) {
                throw new IllegalStateException("Clock moved backwards, unable to generate unique ID");
            }

            if (timestamp == lastTimestamp) {
                randomSequence = (randomSequence + 1) & MAX_RANDOM;
                if (randomSequence == 0) {
                    // 如果随机数溢出，等待下一毫秒
                    timestamp = waitNextMillis(lastTimestamp);
                }
            } else {
                randomSequence = new SecureRandom().nextInt((int) Math.pow(2, RANDOM_BITS));
            }

            lastTimestamp = timestamp;
        }

        // 按照要求的长度拼接ID
        long result = (timestamp << (MACHINE_ID_BITS + RANDOM_BITS)) | (machineId << RANDOM_BITS) | randomSequence;

        // 返回固定12位长度的ID
        result = result % 1000000000L;
        return result;
    }

    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis() - EPOCH;
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis() - EPOCH;
        }
        return timestamp;
    }


    public static Long getId(Long prefix) {
        return generateUniqueId(prefix);
    }
    //IdEnum

    public static Long getId(IdEnum idEnum) {
        return generateUniqueId(idEnum.getPrefix());
    }

    public static Long getUserId() {
        return generateUniqueId();
    }


    /**
     * 生成包含UUID、时间戳和机器信息的唯一ID字符串
     *
     * @return 唯一ID字符串
     */
    public static String getStrUUID() {
        UUID uuid = UUID.randomUUID();
        long timestamp = Instant.now().toEpochMilli();
        String machineInfo = String.format("%02d", machineId);
        return uuid.toString() + "-" + timestamp + "-" + machineInfo;
    }

    /**
     * 获取唯一字符串ID
     * @return
     */
    public static String getStrID() {
        return generateUniqueStrId();
    }

    public static String generateUniqueStrId() {
        long timestamp = System.currentTimeMillis() - EPOCH;
        long randomPart;

        synchronized (lock) {
            if (timestamp == lastTimestamp) {
                randomSequence = (randomSequence + 1) & (1 << RANDOM_BITS) - 1;
                if (randomSequence == 0) {
                    timestamp = waitNextMillis(lastTimestamp);
                }
            } else {
                randomSequence = random.nextInt(1 << RANDOM_BITS);
            }

            lastTimestamp = timestamp;
        }

        randomPart = randomSequence | (machineId << RANDOM_BITS);

        return toHex(timestamp, 8) + toHex(randomPart, 16);
    }
    // 将长整形转换为固定长度的16进制字符串
    private static String toHex(long value, int length) {
        String hexString = Long.toHexString(value);
        while (hexString.length() < length) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
    public static void main(String[] args) {
        System.err.println(getStrID());
        System.err.println(generateUniqueStrId());
    }


}

