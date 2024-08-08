package com.vs.stream;
import java.util.stream.*;
import java.util.*;

public class StreamTest {
    class User {
        String name;
        Integer age;
        User() {}

        User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        User(String info) {
            /*this.name = info.split("-")[0];
            this.age = Integer.parseInt(info.split("-")[1]);*/
            this.name = info.split(", ")[0];
            this.age = Integer.parseInt(info.split(", ")[1]);
        }


        public int getAge() {
            return this.age;
        }
        public String getName() {
            return this.name;
        }
        public String toString() {
            return "name: " + this.name + " age: " + this.age;
        }
    }

    public void test() {
        // 根据集合创建流
//        List<String> list = new ArrayList<>();
//        Collections.addAll(list, "velvet", "velvetori", "shiki", "V", "svelte", "zbc");
//        Stream<String> sl = list.stream();
//        // 中间操作
//        sl = sl.filter(ele -> ele.startsWith("v")).filter(ele -> ele.length() > 6);
//        // forEach为终端操作
//        sl.forEach(ele -> System.out.println(ele));
        // output: velvetori

        // 双列集合
        // 1. 创建map集合对象
        Map<Integer, String> mp = new HashMap<>();
        mp.put(1, "1");
        mp.put(3, "1");
        mp.put(4, "1");
        mp.put(5, "1");
        mp.put(6, "1");
        // 根据键构建流对象
        Set<Integer> idSet = mp.keySet();
        Stream<Integer> smp = idSet.stream();
        // 终端处理
        smp.forEach(ele -> System.out.println(ele));        // 1 3 4 5 6

        // 2. 构建map集合
        TreeMap<String, Integer> tmp = new TreeMap<>();
        tmp.put("1", 1);
        tmp.put("2", 2);
        tmp.put("3", 3);
        tmp.put("4", 4);
        tmp.put("5", 5);
        tmp.put("6", 6);
        // ele为entry对象，链式执行流
        tmp.entrySet().stream().filter(ele -> ele.getValue() > 4).forEach(ele -> System.out.println(ele));
        // output: 5=5  6=6

        // 静态数据流
        Stream.of(1, 2, 3, 4, 5, 6).forEach(e -> System.out.println(e));
        // output: 1 2 3 4 5 6


        // api test
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry", "Date", "Elderberry");

        // 使用Stream API过滤、映射和收集
        List<String> filteredAndMappedFruits = fruits.stream()
                .filter(fruit -> fruit.startsWith("C"))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        System.out.println(filteredAndMappedFruits);
        // output: [cherry]

        // 使用Stream API进行聚合操作
//        fruits.stream().filter(fruit -> fruit.length() > 5).forEach(e -> System.out.println(e));
        // output: Banana  Cherry  Elderberry

        List<String> output = fruits.stream().filter(fruit -> fruit.length() > 5).toList();
        System.out.println("output: " + output);
        // output: [Banana, Cherry, Elderberry]

        String[] s = fruits.stream().filter(fruit -> fruit.length() > 5).toArray(val -> new String[val]);
        for(String i: s) {
            System.out.println(i);
        }
        // Banana
        // Cherry
        // Elderberry

//        System.out.println(fruits);     // output: [Apple, Banana, Cherry, Date, Elderberry]
    }

    public void test2() {
        /*List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Set<Integer> output = list.stream().filter(e -> e % 2 == 0).collect(Collectors.toSet());
        System.out.println(output);*/
        // output： [2, 4, 6, 8, 10]

        List<User> list2 = new ArrayList<>();
        list2.add(new User("velvet", 18));
        list2.add(new User("shiki", 24));
        list2.add(new User("steve", 15));

        Map<String, Integer> output = list2.stream().filter(user -> user.getAge() >= 18).collect(Collectors.toMap(k -> k.getName(), v -> v.getAge()));
        System.out.println(output);
        // output: {velvet=18, shiki=24}
    }

    public void test3() {
        // print
        List<String> list = Arrays.asList("a", "b", "c", "d");
        // 使用方法引用来替代Lambda表达式
        list.forEach(System.out::println);
        // output: a  b  c  d

        // reverse
        List<Integer> list2 = new ArrayList<>();
        Collections.addAll(list2, 1, 2, 3, 4, 5, 6, 7, 8);
        // 方法引用
        Collections.sort(list2, StreamTest::myReverse);
        System.out.println(list2);
        // output: [8, 7, 6, 5, 4, 3, 2, 1]

        // transfer
        List<String> list3 = Arrays.asList("1", "2", "3", "4");
        list3.stream().map(Integer::parseInt).forEach(e -> System.out.println(e));
        // output: 1 2 3 4
    }
    public static int myReverse(int a, int b) {
        return b - a;
    }

    public void test4() {
//        List<String> list = Arrays.asList("velvet", "vs_not_velvetshiki", "shiki", "vvv", "V", "V_Shiki", "shikiV");
        // 使用类成员方法引用
//        list.stream().filter(new JudgeVV()::getv).forEach(e -> System.out.println(e));
        // output: velvet  vs_not_velvetshiki  V_Shiki

        // 构造方法引用
        List<String> list2 = Arrays.asList("velvet-18", "vs_not_velvetshiki-15", "shiki-25", "vvv-18", "V-31", "V_Shiki-36", "shikiV-27");
        // 将数组map为User, 类的构造方法需要与map重写方法参数返回值保持一致
        list2.stream().map(u -> new User(u)).forEach(e -> System.out.println(e));
//        list2.stream().map(User::new).forEach(e -> System.out.println(e));

        // name: velvet age: 18
        // name: vs_not_velvetshiki age: 15
        // name: shiki age: 25
        // name: vvv age: 18
        // name: V age: 31
        // name: V_Shiki age: 36
        // name: shikiV age: 27
    }

    class JudgeVV {
        public boolean getv(String s) {
            return (s.startsWith("v") || s.startsWith("V")) && s.length() >=6;
        }
    }

    public void test5() {
        // 实例方法调用
        String s = "abcdefg";
        String newS = s.toUpperCase();
        System.out.println(s);          // abcdefg
        System.out.println(newS);       // ABCDEFG

        List<String> list = new ArrayList<>();
        Collections.addAll(list, "abc", "def", "GHI", "jk");
        // 类名引用成员方法
        list.stream().map(String::toUpperCase).forEach(System.out::println);
        // output
        // ABC
        // DEF
        // GHI
        // JK

        List<String> list2 = Arrays.asList("a", "b", "c");
// forEach引用了String成员方法，接收每个元素类型为String，且
        list2.forEach(System.out::println);
    }

    public void test6() {
/*        List<String> list = new ArrayList<>();
        Collections.addAll(list, "V, 18", "shiki, 19", "velvet, 32");

        // list数组通过流处理通过构造方法引用转换为User对象，再收集到新的数组中，打印
        List<User> arr = list.stream().map(User::new).collect(Collectors.toList());
        System.out.println(arr);
        // [name: V age: 18, name: shiki age: 19, name: velvet age: 32]*/

        Set<User> us = new HashSet<>();
        us.add(new User("shiki", 18));
        us.add(new User("velvet", 14));
        us.add(new User("drake", 13));
        us.add(new User("velvetori", 8));
        // 通过类成员方法引用转换user->String, 并遍历输出
//        us.stream().map(User::getName).forEach(System.out::println);

        List<String> collect = us.stream().map(User::toString).collect(Collectors.toList());
        System.out.println(collect);
        // output: [name: shiki age: 18, name: drake age: 13, name: velvetori age: 8, name: velvet age: 14]

    }
}
