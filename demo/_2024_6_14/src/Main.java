import com.vs.Proxy.Agency;
import com.vs.Proxy.Athlete;
import com.vs.Proxy.Animal;

public class Main {
    public static void main(String[] args) {
//        netTest n = new netTest();
//        n.test_receive();

//        uuidTest t = new uuidTest();
//        t.test2();

/*        ReflectionTest r = new ReflectionTest();
        try {
            r.test2();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/

        // proxy
        Athlete ath = new Athlete("shiki", 18, "marathon");
        // 不使用代理
        System.out.println("****************without proxy****************");
        ath.marathon();
        ath.running(10);
        ath.jumping(200);

        // 使用代理
        System.out.println("\n****************with proxy****************");
        System.out.println(ath);
        Animal agent = Agency.createProxy(ath);
        agent.marathon();
        agent.running(10);
        agent.jumping(200);

        // output
        // ****************without proxy****************
        // shiki--18--marathon--marathonning...
        // shiki--18--marathon--running...10km
        // shiki--18--marathon--jumping...200times
        //
        // ****************with proxy****************
        // shiki18marathon
        // ath is ready to long run through agency
        // shiki--18--marathon--marathonning...
        // ath is ready jogging through agency
        // shiki--18--marathon--running...10km
        // ath is ready popup through agency
        // shiki--18--marathon--jumping...200times
    }
}
