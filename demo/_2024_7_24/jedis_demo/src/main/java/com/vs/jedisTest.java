package com.vs;

import redis.clients.jedis.Jedis;

public class jedisTest {

    public Jedis initJedis() {
        Jedis jedis = new Jedis("1.94.180.58", 6379);
        jedis.auth("666666");
        jedis.select(0);
        return jedis;
    }

    public void get(Jedis jedis, String key) {
        String ret = jedis.get(key);
        System.out.println(ret);
    }

    public void set(Jedis jedis, String key, String val) {
        String ret = jedis.set(key, val);
        System.out.println(ret);
    }

    public void closeJedis(Jedis jedis) {
        if(jedis != null) jedis.close();
    }
}
