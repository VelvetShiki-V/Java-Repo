import com.vs.exception.ExceptionTest;
import com.vs.stream.StreamTest;
import com.vs.fileIO.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        HashMapTest t = new HashMapTest();
//        t.test5();

//        StreamTest st = new StreamTest();
//        st.test6();

//        ExceptionTest t = new ExceptionTest();
//        t.test();

        FileTest f = new FileTest();
        try {
//            f.testDeepCopy("D:\\Code\\java\\_2024_6_5\\assets", "D:\\Code\\java\\_2024_6_5\\dest");
            f.testSort("D:\\Code\\java\\_2024_6_5\\assets\\data.txt", "D:\\Code\\java\\_2024_6_5\\dest");       // 1-7-2-3-0-9-4-8
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
