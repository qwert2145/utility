package com.my;

import java.lang.reflect.Method;

/**
 * Created by dell on 2018/8/1.
 */
public class ReflectTest {
    private String name;

    void testNoParam(){
        System.out.println("testNoParam---");
    }

    void testParam(String name){
        System.out.println("testParam:param is" + name);
    }

    String testParamWithReturn(String name){
        return "testParamWithReturn " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws Exception {
        Class clazz = Class.forName("com.dafy.test.util.ReflectTest");
        Object obj = clazz.newInstance();

        ReflectTest test = (ReflectTest)obj;
//        test.testNoParam();

        //testNoParam
        Method method = clazz.getDeclaredMethod("testNoParam");
        method.invoke(obj);

        //testParam
        Method method1 = clazz.getDeclaredMethod("testParam",String.class);
        method1.invoke(obj,"test");

        //testParam
        Method method2 = clazz.getDeclaredMethod("testParamWithReturn",String.class);
        Object object = method2.invoke(obj, "test");
        System.out.println(object);

        clazz.getDeclaredField("name").set(obj, "setTest");
        System.out.println(test.getName());
    }
}
