package com.zl.dd;

import com.zl.netty.ProxyFactory;
import com.zl.netty.heartBeat.common.CustomHeartbeatHandler;
import com.zl.netty.heartBeat.server.ServerHandler;
import org.junit.Test;
import org.voovan.tools.TEnv;
import org.voovan.tools.compiler.function.DynamicFunction;
import org.voovan.tools.reflect.TReflect;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by hl on 2018/6/15.
 */
public class TestReflact {


    public static void main(String[] args) throws Exception {
        String[] s1 = new String[]{"a","b","c"};
        TReflect.allocateInstance(ProxyFactory.class);
        String s = "sdfsdf";
        String ss = (String) createNewInstance(s);
        System.out.println(ss);

        try {
            unsafeTest1();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static Object createNewInstance(Object obj){
        try {
            return obj.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object createNewInstance2(Object obj){
        try {
            return TReflect.allocateInstance(obj.getClass());
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void unsafeTest1() throws Exception {
        TReflect.allocateInstance(ProxyFactory.class);
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        // This creates an instance of player class without any initialization
        Player p = (Player) unsafe.allocateInstance(Player.class);
        System.out.println(p.getAge()); // Print 0

        p.setAge(45); // Let's now set age 45 to un-initialized object
        System.out.println(p.getAge()); // Print 45

        Class[] classes = new Class[1];
        classes[0] = CustomHeartbeatHandler.class;
        boolean b = TReflect.classChecker(ServerHandler.class, classes);
        System.out.println(b);

        TestReflact tr = TReflect.allocateInstance(TestReflact.class);


        Player pp = TReflect.invokeMethod(tr, "createNewInstance2", p);
        System.out.println(pp.getAge());


        PrivateClass p1 = new PrivateClass();

        Class<?> classType = p1.getClass();

        // 获取Method对象
        Method method = classType.getDeclaredMethod("sayHello", new Class[] { String.class });

        method.setAccessible(true); // 抑制Java的访问控制检查
        // 如果不加上上面这句，将会Error: TestPrivate can not access a member of class PrivateClass with modifiers "private"
        String str1 = (String) method.invoke(p1, new Object[] { "zhangsan" });

        System.out.println(str1);

        String code =  "import java.util.ArrayList;\n\n" +
                "ArrayList list = new ArrayList();\n" +
                "System.out.println(temp1+ temp2);\n" +
                "list.add(temp1);" +
                "list.add(temp2);" +
                "return list;\n";
        DynamicFunction function = new DynamicFunction("testFunction",code);
        function.enableImportInCode(true);
        function.addPrepareArg(0, String.class, "temp1");
        function.addPrepareArg(1, String.class, "temp2");
        System.out.println(function.call("1111", "2222").toString());
    }

    @Test
    public void test2(){
        TEnv.getClassPath();
    }

}

class Player {
    private int age = 12;

    private Player() {
        this.age = 50;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String sayHello(String name){
        System.out.println("hello zl, " + name);
        return "hello, zl " + name;
    }
}



