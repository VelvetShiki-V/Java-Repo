package com.vs.Reflection;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Properties;

public class ReflectionTest {
    public void test() {
        try {
            // 获取 Class 对象
            User user1 = new User("velvet", "12345");
            // 或通过reference获取
            Class<?> cls = Class.forName("com.vs.Reflection.User");

            // 获取类名
            System.out.println("Class Name: " + cls.getName());
            System.out.println(user1.getClass());

            // 获取构造函数
            Constructor<?> constructor = cls.getConstructor(String.class, String.class);
            System.out.println(constructor);
            // 根据获取到的构造函数创建实例赋值
            Object instance = constructor.newInstance("shiki", "123123");
            System.out.println("Instance created: " + instance);

            // 获取所有方法（包含父类的所有公共方法，若不想要父类则用getDeclaredMethods——子类公有 + 私有）
            Method[] methods = cls.getMethods();
            for(Method m: methods) {
                System.out.println(m);
            }
            // 获取单个方法并调用invoke
            System.out.println("***********************");
            Method method = cls.getMethod("registry", String.class, String.class);
            Object result = method.invoke(instance, "vs", "asdasdsa");      // invoke为调用该方法的实例对象
            System.out.println("Method result: " + result);
            // 获取方法名字，修饰符，参数
            System.out.println(method.getName());
            System.out.println(method.getModifiers());
            Parameter[] pms = method.getParameters();
            for(Parameter p: pms) {
                System.out.println(p.getName());
            }
            // 获取返回值
            // 获取抛出的异常类型
            Class[] excps = method.getExceptionTypes();
            for(Class e: excps) {
                System.out.println(e);
            }
            System.out.println("***********************");

            // 获取字段并修改（修饰符 + 值）
            Field field = cls.getDeclaredField("username");
            field.setAccessible(true); // 确保可以访问私有字段
            field.set(instance, "Reflected");
            System.out.println("Field value changed: " + instance);

        } catch (ClassNotFoundException | NoSuchMethodException |
                 IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void test2() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 1. 读取配置文件，获取全类名和方法
        Properties prop = new Properties();
        FileReader reader = new FileReader("D:\\Code\\java\\_2024_6_14\\src\\com\\vs\\Reflection\\prop.properties");
        prop.load(reader);
        String className = (String)prop.get("classname");
        String methodName = (String)prop.get("method");
        String name = (String)prop.get("key");
        String pwd = (String)prop.get("val");

        // 2. 根据全类名构造class对象，并实例化对象
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getConstructor(String.class, String.class);
        Object o = constructor.newInstance(name, pwd);
        System.out.println("实例化对象: " + o);

        // 3. 根据对象调用方法
        Method method = clazz.getMethod(methodName, String.class, String.class);
        String ret = (String) method.invoke(o, "mamba", "00000000");
        System.out.println("调用反射方法" + ret);
        System.out.println("ret: " + o);

        // output
        // 实例化对象: name: velvet--pwd: 12345--0
        // 调用反射方法register success
        // ret: name: mamba--pwd: 00000000--2
    }
}

class User {
    public String username;
    protected String password;
    private Integer uid;

    public User() {
        this.username = "default shiki";
        this.password = "123456";
        this.uid = 00001;
    }

    public User(String name, String pwd) {
        this.username = name;
        this.password = pwd;
        this.uid = 00000;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String registry(String name, String pwd) {
        this.username = name;
        this.password = pwd;
        this.uid = 00002;
        return "register success";
    }

    public String toString() {
        return "name: " + this.username + "--" + "pwd: " + this.password + "--" + this.uid;
    }
}
