package com.vs.cloud_common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SnowFlakeIdUtil {
    // 起始时间戳 (2020-01-01 00:00:00)
    private static final long START_TIMESTAMP = 1577836800000L;

    // 每部分的位数
    private static final long DATA_CENTER_BITS = 5L;  // 数据中心 ID 位数
    private static final long MACHINE_BITS = 5L;      // 机器 ID 位数
    private static final long SEQUENCE_BITS = 12L;    // 序列号位数

    // 每部分的最大值
    private static final long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BITS);
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    // 每部分的左移位数
    private static final long MACHINE_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_SHIFT = SEQUENCE_BITS + MACHINE_BITS;
    private static final long TIMESTAMP_SHIFT = DATA_CENTER_SHIFT + DATA_CENTER_BITS;

    // 数据中心 ID 和机器 ID（可以从配置中读取）
    private long dataCenterId;
    private long machineId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowFlakeIdUtil(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("DataCenterId out of range");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("MachineId out of range");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    // 生成下一个唯一 ID
    public synchronized String nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }

        if (currentTimestamp == lastTimestamp) {
            // 相同毫秒内，序列号递增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                // 序列号已满，等待下一毫秒
                currentTimestamp = nextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒内，序列号置为 0
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        // 生成唯一 ID
        long id = ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_SHIFT)
                | (machineId << MACHINE_SHIFT)
                | sequence;

        return String.valueOf(id);
    }

    // 获取下一毫秒
    private long nextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    // 从主机 IP 生成机器 ID
    public static long getMachineId() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] ipAddress = ip.getAddress();
            return (long) (ipAddress[ipAddress.length - 1] & 0xFF) % MAX_MACHINE_NUM;
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to get machine ID", e);
        }
    }

    // 使用默认配置生成 UID
    public static String generateUid() {
        long dataCenterId = 1L;  // 这里假设数据中心 ID 固定为 1，可以从配置读取
        long machineId = getMachineId();
        SnowFlakeIdUtil generator = new SnowFlakeIdUtil(dataCenterId, machineId);
        return generator.nextId();
    }
}
