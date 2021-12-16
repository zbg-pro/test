package com.zl.javabingfabianchengshizhan.deskindex;

import org.voovan.tools.TEnv;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Allen.zhang
 * @title: Indexer
 * @projectName zl
 * @description: TODO
 * @date 2021/11/723:09
 */
public class Indexer implements Runnable{
    private final BlockingQueue<File> queue;

    public static final Set<File> indexedFile = new CopyOnWriteArraySet<>();

    public Indexer(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                indexFile(queue.take());
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(File take) {
        indexedFile.add(take);
    }

    public static void main(String[] args) {
        File file = new File("D:\\java\\zl\\test\\src\\main\\java\\net");
        startIndexing(new File[] {file});
    }

    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingDeque<>(Integer.MAX_VALUE);
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        };

        for (File root: roots) {
            new Thread(new FileCrawler(queue, fileFilter, root)).start();
        }

        for (int i = 0; i < 30; i++) {
            new Thread(new Indexer(queue)).start();
        }

        TEnv.sleep(10000);

        System.out.println(indexedFile.size());
    }
}
