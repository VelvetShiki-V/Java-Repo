import redis.clients.jedis.Jedis;
import com.vs.jedisTest;

public class main {
    public static void main(String[] args) {
        jedisTest instance = new jedisTest();
        Jedis jedis = instance.initJedis();

        // get
        instance.set(jedis, "java", "test!");
        instance.get(jedis, "java");
    }
}
