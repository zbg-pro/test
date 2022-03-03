package com.zl.javabingfabianchengshizhan.thread7cancelclose.poisonpill;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Allen.zhang
 * @title: IndexingService
 * @projectName zl
 * @description: TODO
 * @date 2022/1/423:56
 */
public class IndexingService {

    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;
    private static final int CAPACITY = 1000;

    public IndexingService(File root, final FileFilter fileFilter){
        this.root = root;
        queue = new LinkedBlockingQueue<>(CAPACITY);
        this.fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || fileFilter.accept(file);
            }
        };
    }


    public void start(){
        producer.start();
        consumer.start();
    }

    public void stop(){
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }


    class CrawlerThread extends Thread {
        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {

            } finally {
                while (true) {

                    try {
                        queue.put(POISON);
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        /*重新尝试*/
                    }
                }

            }
        }

        private void crawl(File root) throws InterruptedException {
            //...
        }

    }

    class IndexerThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON) {
                        break;
                    } else {
                        indexFile(file);
                    }
                }
            } catch (InterruptedException e) {
            }
        }

        public void indexFile(File file) {
            /*...*/
        };
    }


}
