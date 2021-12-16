package com.zl.javabingfabianchengshizhan.deskindex;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

/**
 * @author Allen.zhang
 * @title: FileCrawler
 * @projectName zl
 * @description: TODO
 * @date 2021/11/722:58
 */
public class FileCrawler implements Runnable{

    private final BlockingQueue<File> fileQueue;

    private final FileFilter fileFilter;

    private final File root;

    public FileCrawler(BlockingQueue<File> queue, FileFilter fileFilter, File root) {
        this.fileQueue = queue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    @Override
    public void run() {
        try {
            crawl(root);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private void crawl(File root) throws InterruptedException {
        File[] entries = root.listFiles(fileFilter);
        if (entries != null) {
            for (File file : entries) {
                if (file.isDirectory()) {
                    crawl(file);
                } else if(!alreadyIndexed(file)) {
                    fileQueue.put(file);
                }
            }
        }
    }

    private boolean alreadyIndexed(File file) {
        return !fileQueue.contains(file) && Indexer.indexedFile.contains(file);
    }
}
