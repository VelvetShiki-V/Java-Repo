package com.vs.phase2;
import org.w3c.dom.ls.LSOutput;
import java.util.*;

public class HashMapTest {
    public class User {
        private Integer id;
        private String name;
        private String pwd;

        public User() {
            this.id = 0;
            this.name = "default";
            this.pwd = "";
        }

        public User(Integer id, String name, String pwd) {
            this.id = id;
            this.name = name;
            this.pwd = pwd;
        }

        // 需要重写hashCode和equals确保键值唯一
        @Override
        public boolean equals(Object o) {
            // 如果为自身则相等
            if (this == o) return true;
            // 如果类型不相等返回false
            if (o == null || this.getClass() != o.getClass()) return false;
            // 如果目标对象与该对象所有属性值都相等则相等
            User user = (User) o;
            return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(pwd, user.pwd);
        }

        @Override
        public int hashCode() {
            // 通过Objects工具类传入对象的多个属性哈希值
            return Objects.hash(id, name, pwd);
        }

        @Override
        public String toString() {
            return "{id: " + this.id + " name: " + this.name + " pwd: " + this.pwd + "}";
        }
    }

    public void test1() {
        // Map为接口，需要创建实现类对象
        Map<Integer, String> mp = new HashMap<>();
        mp.put(1, "123");
        mp.put(10, "564");
        mp.put(2, "dfghgfd");
        mp.put(5, "fvf");
        mp.put(9, "5646");
        mp.put(7, "ghm");
        mp.remove(2);
        mp.remove(5);

        // 生成entry对象集合, 其中Entry为Map内部Set访问键值对接口
        Set<Map.Entry<Integer, String>> entries = mp.entrySet();

        // 内部toString遍历
        System.out.println(entries);

        // 范围for遍历
        for(Map.Entry<Integer, String> e: entries) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }

        // lambda遍历
        entries.forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));
        // 1 = 123
        // 7 = ghm
        // 9 = 5646
        // 10 = 564

//        Set<Integer> keys = mp.keySet();     // 返回Set类型
//        System.out.println(keys instanceof Set<Integer>);
//        System.out.println(keys);
//        System.out.println(mp.get(1));      // 返回key对应val
        // true
        // [1, 7, 9, 10]
        // 123

        // 范围for
//        for(Integer k: keys) {
//            System.out.println("key: " + k + " val: " + mp.get(k));
//        }
        // key: 1 val: 123
        // key: 7 val: ghm
        // key: 9 val: 5646
        // key: 10 val: 564

        // 迭代器
//        Iterator<Integer> it = keys.iterator();
//        while(it.hasNext()) {
//            Integer k = it.next();
//            System.out.println(k + " = " + mp.get(k));
//        }
        // 1 = 123
        // 7 = ghm
        // 9 = 5646
        // 10 = 564

        // lambda
//        keys.forEach(k -> System.out.println("key: " + k + " val: " + mp.get(k)));
        // key: 1 val: 123
        // key: 7 val: ghm
        // key: 9 val: 5646
        // key: 10 val: 564

//        System.out.println(mp);
//        System.out.println(mp.containsKey(1));
//        System.out.println(mp.containsKey(2));
//        System.out.println(mp.containsKey(7));
//        System.out.println(mp.containsValue("123"));
//        System.out.println(mp.containsValue("564"));
//        System.out.println(mp.containsValue("fvf"));
//        System.out.println(mp.size());
//        System.out.println(mp.isEmpty());
//        mp.clear();
//        System.out.println(mp.isEmpty());
    }

    public void test2() {
//        int[] array = {1,2,3};
//        String s = "!23";
//        Integer i = 1;
//        ArrayList<String> arr = new ArrayList<>();

        User u1 = new User(1, "shiki", "123");
        User u2 = new User(10, "drake", "234");
        User u3 = new User(5, "goods", "345");

//        // 观察哈希值
//        System.out.println((new User()).hashCode());
//        System.out.println((new User()).hashCode());
//        // 644310590
//        // 644310590
//
//        System.out.println(Objects.hash(new User()));
//        System.out.println(Objects.hash(new User()));
//
//        System.out.println(Objects.hashCode(new User()));
//        System.out.println(Objects.hashCode(new User()));

        HashMap<Integer, User> hmp = new HashMap<>();
        hmp.put(1, new User());
        hmp.put(1, new User());
        hmp.put(1, new User());
        hmp.put(2, new User());
        hmp.put(10, new User());
        hmp.put(10, new User());
        hmp.put(3, u1);
        hmp.put(4, u2);
        hmp.put(4, u2);
        hmp.put(5, u3);

        // 获取所有值
        Collection<User> vals = hmp.values();
        System.out.println(vals);
        // [{id: 0 name: default pwd: }, {id: 0 name: default pwd: }, {id: 1 name: shiki pwd: 123},
        // {id: 10 name: drake pwd: 234}, {id: 5 name: goods pwd: 345}, {id: 0 name: default pwd: }]

        // 获取所有键
        System.out.println(hmp.keySet());   // [1, 2, 3, 4, 5, 10]

        // 将map转为对象集合, lambda遍历输出
        Set<Map.Entry<Integer, User>> entries = hmp.entrySet();
        entries.forEach(entry -> System.out.println("key: " + entry.getKey() + " = " + entry.getValue()));
        // key: 1 = {id: 0 name: default pwd: }
        // key: 2 = {id: 0 name: default pwd: }
        // key: 3 = {id: 1 name: shiki pwd: 123}
        // key: 4 = {id: 10 name: drake pwd: 234}
        // key: 5 = {id: 5 name: goods pwd: 345}
        // key: 10 = {id: 0 name: default pwd: }

        // 范围for遍历
        Set<Integer> keys = hmp.keySet();
        for(Integer k: keys) {
            System.out.println("k: " + k + " = " + hmp.get(k));
        }
        // k: 1 = {id: 0 name: default pwd: }
        // k: 2 = {id: 0 name: default pwd: }
        // k: 3 = {id: 1 name: shiki pwd: 123}
        // k: 4 = {id: 10 name: drake pwd: 234}
        // k: 5 = {id: 5 name: goods pwd: 345}
        // k: 10 = {id: 0 name: default pwd: }

        // 迭代器
        Iterator<Integer> it = hmp.keySet().iterator();
        while(it.hasNext()) {
            Integer key = it.next();
            System.out.println("key: " + key + " = " + hmp.get(key));
        }
        // key: 1 = {id: 0 name: default pwd: }
        // key: 2 = {id: 0 name: default pwd: }
        // key: 3 = {id: 1 name: shiki pwd: 123}
        // key: 4 = {id: 10 name: drake pwd: 234}
        // key: 5 = {id: 5 name: goods pwd: 345}
        // key: 10 = {id: 0 name: default pwd: }


//        hmp.forEach(entry -> System.out.println(entry));



//        System.out.println(s.getClass());
//        System.out.println(i.getClass());       // 包装类具有类方法获取
//        System.out.println(arr.getClass());
//        System.out.println(hmp.getClass());
//        System.out.println(array.getClass());
//        System.out.println(array[0].getClass());      // 基本数据类型没有类方法获取
    }

    public void test3() {
        // 通过随机数生成key
        String[] level = {"A", "B", "C", "D"};
        ArrayList<String> arr = new ArrayList<>();
        Random rand = new Random();
//        arr.addAll(Arrays.asList("A", "B", "B", "C", "C", "C"));
        for(int i = 0; i < 100; i++) {
            arr.add(level[(rand.nextInt(level.length))]);
        }

        // 利用map统计key出现次数
        // C++统计写法： hmp["A"]++;
        Map<String, Integer> hmp = new HashMap<>();
        for(String s: arr) {
            // 方式三：merge，如果s不存在，它将添加键s并设置其值为 1。如果s已经存在，它将现有值与1相加
            hmp.merge(s, 1, (oldVal, newVal) -> oldVal + newVal);

            // 方式二: compute，对于键s，如果val存在覆盖原Entry对象值++；否则创建新键并将对应值设为0
//            hmp.compute(s, (k, v) -> v == null ? 0 : v + 1);


            // 方式一：如果键存在，返回对应值；若不存在返回默认值
//            int val = hmp.getOrDefault(s, 0);
//            val++;
//            hmp.put(s, val);
        }
        System.out.println(hmp);        // {A=30, B=26, C=17, D=27}
    }

    public void test4() {
        String str = "hello this is a test we are gonna calculate each alphabet appearing at this sentence now lets do it   ";
        char[] arr = str.replaceAll(" ", "").trim().toCharArray();
//        System.out.println();
//        output: hellothisisatestwearegonnacalculateeachalphabetappearingatthissentencenowletsdoit

        Map<Character, Integer> tmp = new TreeMap<>();
        for(Character c: arr) {
            tmp.merge(c, 1, (oldVal, newVal) -> oldVal + newVal);
        }

        tmp.forEach((k, v) -> System.out.println("key: " + k + " = " + "val: " + v));
        // key: a = val: 11
        // key: b = val: 1
        // key: c = val: 4
        // key: d = val: 1
        // key: e = val: 12
        // key: g = val: 2
        // key: h = val: 5

        // System.out.println(tmp);
        // output: {a=11, b=1, c=4, d=1, e=12, g=2, h=5, i=5, l=6, n=6, o=4, p=3, r=2, s=6, t=10, u=1, w=2}
    }

    public void test5() {
//        List<Integer> li = new ArrayList<>();
//        Collections.addAll(li, 1, 2, 3, 4);
//        System.out.println(li);     // output: [1, 2, 3, 4]
//
//        Collections.shuffle(li);
//        System.out.println(li);     // output: [3, 4, 1, 2]
//
//        Collections.swap(li, 0, 3);;
//        System.out.println(li);     // output: [4, 2, 3, 1]
//
//        Collections.reverse(li);
//        System.out.println(li);     // output: [1, 3, 2, 4]

        Map<String, Integer> mp = new HashMap<>();
        mp.put("1", 1);
        mp.put("2", 1);
        mp.put("3", 1);
        mp.put("4", 1);
        mp.put("5", 1);

        mp.remove("3");
        mp.replace("5", 4);

        System.out.println(mp);     // {1=1, 2=1, 4=1, 5=4}

        Map<String, Integer> mp2 = Map.copyOf(mp);
        // 拷贝的map为不可变，无法修改
//        mp2.put("8");
//        mp2.remove("5");
        System.out.println(mp2);    // {1=1, 2=1, 4=1, 5=4}
    }
}
