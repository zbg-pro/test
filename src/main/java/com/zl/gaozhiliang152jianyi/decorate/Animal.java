package com.zl.gaozhiliang152jianyi.decorate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/24 11:09 上午
 * @auth ALLEN
 */
public interface Animal {
    void doStuff();
    boolean addFeature(Class<? extends Feature> clz);
    boolean removeFeature(Class<? extends Feature> clz);

}

class Rat implements Animal {

    @Override
    public void doStuff() {
        System.out.println("Jerry will play with Tom.");
    }

    @Override
    public boolean addFeature(Class<? extends Feature> clz) {
        return false;
    }

    @Override
    public boolean removeFeature(Class<? extends Feature> clz) {
        return false;
    }
}

interface Feature {
    void load();
}

class FlyFeature implements Feature {

    @Override
    public void load() {
        System.out.println("增加一只翅膀。。。");
    }
}

class DigFeature implements Feature {

    @Override
    public void load() {
        System.out.println("增加挖洞能力。。。");
    }
}

class DecorateAnimal implements Animal {

    private Animal animal;

    private List<Class<? extends Feature>> classes = new ArrayList<>();

    public DecorateAnimal(Animal animal){
        this.animal = animal;
    }

    public boolean addFeature(Class<? extends Feature> clz){
        return classes.add(clz);
    }

    public boolean removeFeature(Class<? extends Feature> clz){
        return classes.remove(clz);
    }

    @Override
    public void doStuff() {

        ClassLoader classLoader = getClass().getClassLoader();

        classes.forEach(clz -> {
            InvocationHandler handler = (proxy, method, args) -> {
                Object obj = null;

                if (Modifier.isPublic(method.getModifiers())) {
                    obj = method.invoke(clz.newInstance(), args);
                }

                return obj;
            };

            Feature proxy = (Feature) Proxy.newProxyInstance(classLoader, clz.getInterfaces(), handler);

            proxy.load();

        });

        animal.doStuff();

    }

    public static void main(String[] args) {
        Animal Jerry = new Rat();

        Jerry = new DecorateAnimal(Jerry);

        Jerry.addFeature(FlyFeature.class);

        Jerry.addFeature(DigFeature.class);

        Jerry.doStuff();

        Jerry.removeFeature(FlyFeature.class);

        Jerry.doStuff();
    }
}





