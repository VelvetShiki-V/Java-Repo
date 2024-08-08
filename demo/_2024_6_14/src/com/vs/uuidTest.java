package com.vs;
import java.util.UUID;

public class uuidTest {
    public void test() {
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
        // 6c880484147245bfba172708849c557f
        // 31d3e15b4d324c80815ff7d25a819acb
        // 09670ab4a75647a1b360b87384a492c0
    }

    public void test2() {
        // 生成版本 1 的 UUID
        UUID timeBasedUUID = UUID.randomUUID();
        System.out.println("Time-based UUID: " + timeBasedUUID);

        // 生成版本 4 的 UUID
        UUID randomUUID = UUID.randomUUID();
        System.out.println("Random UUID: " + randomUUID);

        // 获取 UUID 的各个部分
        long mostSigBits = timeBasedUUID.getMostSignificantBits();
        long leastSigBits = timeBasedUUID.getLeastSignificantBits();
        System.out.println("Most significant bits: " + mostSigBits);
        System.out.println("Least significant bits: " + leastSigBits);

        // 从字符串解析 UUID
        UUID fromString = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        System.out.println("UUID from string: " + fromString);

        // output
        // Time-based UUID: ad376952-748a-40cd-964e-ed67ee74240a
        // Random UUID: 453c1a3f-15a1-4299-803b-49b1aad5f6e2
        // Most significant bits: -5965183378565480243
        // Least significant bits: -7615888889198926838
        // UUID from string: 123e4567-e89b-12d3-a456-426614174000
    }
}
