package com.zl.javabingfabianchengshizhan.synutils;

import org.voovan.tools.TEnv;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author Allen.zhang
 * @title: Preloader
 * @projectName zl
 * @description: TODO
 * @date 2021/11/81:18
 */
public class Preloader {

    private final FutureTask<ProductInfo> future = new FutureTask<>(new Callable<ProductInfo>() {

        @Override
        public ProductInfo call() throws DataLoadException {
            return loadProductInfo();
        }

        private ProductInfo loadProductInfo() {
            System.out.println("load product info");
            TEnv.sleep(1000);
            return new ProductInfo("1", "sdfsdfsdf");
        }
    });

    private Thread thread = new Thread(future);

    public void start(){
        thread.start();
    }

    public ProductInfo get() throws Exception {
        try {
            return future.get();
        } catch (Exception e) {
            Throwable throwable = e.getCause();
            if (throwable instanceof DataLoadException) {
                throw (DataLoadException) throwable;
            } else {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Preloader preloader = new Preloader();
        preloader.start();
        System.out.println(preloader.get());
    }


    private class ProductInfo {
        String id;
        String name;

        public ProductInfo(String id, String name){
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "ProductInfo{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}

class DataLoadException extends Exception {
}
