package com.vs.Proxy;

public class Athlete implements Animal {
    public Athlete() {}
    public Athlete(String name, Integer age, String major) {
        this.name = name;
        this.age = age;
        this.major = major;
    }

    @Override
    public void running(int kilos) {
        System.out.println(this.name + "--" + this.age + "--" + this.major + "--" + "running..." + kilos + "km");
    }

    @Override
    public void marathon() {
        System.out.println(this.name + "--" + this.age + "--" + this.major + "--" + "marathonning...");
    }

    @Override
    public void jumping(int times) {
        System.out.println(this.name + "--" + this.age + "--" + this.major + "--" + "jumping..." + times + "times");
    }

    @Override
    public String toString() {
        return this.name + this.age + this.major;
    }

    // props
    private String name;
    private Integer age;
    private String major;
}
