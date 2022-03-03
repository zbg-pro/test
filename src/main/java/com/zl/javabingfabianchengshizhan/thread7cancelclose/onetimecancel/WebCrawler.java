package com.zl.javabingfabianchengshizhan.thread7cancelclose.onetimecancel;

import net.jcip.annotations.GuardedBy;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Allen.zhang
 * @title: WebCrawaler
 * @projectName zl
 * @description: TODO
 * @date 2022/1/515:40
 */
public abstract class WebCrawler {

    private volatile TrackingExecutor exec;

    @GuardedBy("this")
    private final Set<URL> urlsToCrawl = new HashSet<>();

    private ConcurrentHashMap<URL, Boolean> seen = new ConcurrentHashMap<>();

    private static final int TIMEOUT = 500;

    private static final TimeUnit UNIT = TimeUnit.MILLISECONDS;
    
    public WebCrawler(URL url) {
        urlsToCrawl.add(url);
    }

    public synchronized void start() {
        exec = new TrackingExecutor(Executors.newCachedThreadPool());
        urlsToCrawl.forEach(e -> {
            submitCrawlTask(e);
        });
        urlsToCrawl.clear();
    }

    protected void submitCrawlTask(URL url) {
        exec.execute(new CrawlTask(url));
    }

    public synchronized void stop() throws InterruptedException {
        try {
            saveUncrawled(exec.shutdownNow());
            if (exec.awaitTermination(TIMEOUT, UNIT)){
                saveUncrawled(exec.getCancelledTasks());
            }
        } finally {
            exec = null;
        }

    }

    protected void saveUncrawled(List<Runnable> uncrawled) {
        uncrawled.forEach(e -> {
            CrawlTask crawlTask = (CrawlTask) e;
            urlsToCrawl.add(crawlTask.getPage());
        });
    }

    public abstract List<URL> processPage(URL url);

    private class CrawlTask implements Runnable {

        private final URL url;

        public CrawlTask(URL url) {
            this.url = url;
        }

        boolean alreadyCrawled() {
            return seen.putIfAbsent(url, true) != null;
        }

        void makeUncrawled() {
            seen.remove(url);
            System.out.printf("marking %s uncrawled%n", url);
        }

        @Override
        public void run() {
            for (URL link: processPage(url)) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                submitCrawlTask(link);
            }
        }

        public URL getPage() {
            return url;
        }
    }

}
