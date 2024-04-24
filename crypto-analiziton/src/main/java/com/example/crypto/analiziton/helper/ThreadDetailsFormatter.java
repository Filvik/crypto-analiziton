package com.example.crypto.analiziton.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ThreadDetailsFormatter {

    public static void logFormattedThreadDetails() {
        Map<Thread, StackTraceElement[]> allThreads = Thread.getAllStackTraces();
        int i = 0;
        log.info("---------------Details about active threads------------------------");
        for (Map.Entry<Thread, StackTraceElement[]> entry : allThreads.entrySet()) {
            Thread thread = entry.getKey();
            log.info("---------------------------------------------------");
            log.info(String.valueOf(i++));
            log.info("Thread Name: " + thread.getName());
            log.info("Thread ID: " + thread.getId());
            log.info("Thread State: " + thread.getState());
            log.info("Thread Priority: " + thread.getPriority());
            log.info("Thread Group: " + thread.getThreadGroup().getName());
            log.info("Is Daemon: " + thread.isDaemon());
        }
        log.info("----------------------End----------------------------");
    }
}
