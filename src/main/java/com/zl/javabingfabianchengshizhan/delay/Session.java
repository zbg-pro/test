package com.zl.javabingfabianchengshizhan.delay;

import org.voovan.tools.TDateTime;
import org.voovan.tools.TEnv;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * @author Allen.zhang
 * @title: Session
 * @projectName zl
 * @description:  延迟队列，说白了就是把放入队列的数据进行了一次排序，并且再通过getDelay
 * 将会在每个元素的 getDelay() 方法返回的值的时间段之后才释放掉该元素。如果返回的是 0 或者负值，延迟将被认为过期，该元素将会在 DelayQueue 的下一次 take 被调用的时候被释放掉
 * @date 2022/3/314:21
 */
public class Session {

    /* 延迟时间 */
    private static final int ALIVE_TIME_SECOND = 7;

    /* 延迟队列 */
    DelayQueue<DelayElements> queue = new DelayQueue<>();

    /* 模拟session存放信息，保证线程安全 */
    private Map<String, Object> userSession = new ConcurrentHashMap<>();

    public Session(){}

    public void put(String key, Object value, int delaySecond) {
        // 放入session集合中
        userSession.put(key, value);
        // 构造延时列队元素
        DelayElements delayElements = new DelayElements(key, ALIVE_TIME_SECOND + delaySecond);

        queue.remove(key);
        // 插入新的队列
        queue.add(delayElements);
        System.out.println("添加session key：" + key + ",value:" + value.toString() + "成功");
    }

    public Object get(String key) {
        Object object = userSession.get(key);
        if (object == null){
            System.out.println("获取"+key+"失败。对象已过期");
            return null;
        }

        /*刷新对应key值的指针，顺延session过期时间*/
        DelayElements delayElements=new DelayElements(key, ALIVE_TIME_SECOND);
        queue.remove(delayElements);
        queue.put(delayElements);
        System.out.println("获取"+key+"成功："+object+",生命周期重新计算");
        return object;
    }

    private void sessionGcMethod() throws InterruptedException{
        while(true){
            // 延迟时间内take方法不会获取到值
            // 那么将阻塞线程
            DelayElements delayElements = queue.take();
            queue.remove(delayElements);
            userSession.remove(delayElements.getKey());
            System.out.println(TDateTime.now() + " 删除过期元素key"+delayElements.getKey());
            Thread.sleep(300);
        }
    }

    public void startSession(){
        Thread seesionGcThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sessionGcMethod();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // 设置为守护线程
        seesionGcThread.setDaemon(true);
        seesionGcThread.start();
    }

    public static void main(String[] args) {
        Session session = new Session();
        session.startSession();
        session.put("username7", "yvan7", 7);
        session.put("username6", "yvan6", 6);
        session.put("username4", "yvan4", 4);
        session.put("username8", "yvan8", 8);
        session.put("username2", "yvan2", 2);
        session.put("username1", "yvan1", 1);

        TEnv.sleep(10000000);
    }

}
