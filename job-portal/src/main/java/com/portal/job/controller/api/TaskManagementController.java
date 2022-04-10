package com.portal.job.controller.api;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Sets;
import com.portal.job.executor.ExecutorServiceManager;
import com.portal.job.services.task.TaskManagerService;
import com.portal.job.task.Task;

@Controller
@RequestMapping("/api/task")
public class TaskManagementController {

    @Autowired
    private TaskManagerService taskManagerService;
    @Autowired
    private ExecutorServiceManager executorServiceManager;

    @RequestMapping("get")
    public @ResponseBody QueuesOutPut getQueues() {

        Map<String, Task<?>> inProgressMap = taskManagerService.getInProgressQueue();
        QueuesOutPut out = new QueuesOutPut();
        Set<String> inQueue = Sets.newHashSet();

        inProgressMap.forEach((k, v) -> inQueue.add(k));
        out.setInQueue(inQueue);
        out.setActiveThreads(executorServiceManager.getActiveCountOfThreads());
        return out;
    }

    @RequestMapping("manage")
    public @ResponseBody QueuesOutPut manageQueues(@RequestParam final String clear) {

        if ("true".equals(clear)) {
            taskManagerService.getInProgressQueue().clear();
        }

        Map<String, Task<?>> inProgressMap = taskManagerService.getInProgressQueue();
        QueuesOutPut out = new QueuesOutPut();
        Set<String> inQueue = Sets.newHashSet();

        inProgressMap.forEach((k, v) -> inQueue.add(k));

        out.setInQueue(inQueue);
        out.setActiveThreads(executorServiceManager.getActiveCountOfThreads());
        return out;
    }

    class QueuesOutPut {
        Set<String> inQueue;
        int activeThreads;

        public Set<String> getInQueue() {
            return inQueue;
        }

        public void setInQueue(Set<String> inQueue) {
            this.inQueue = inQueue;
        }

        public int getActiveThreads() {
            return activeThreads;
        }

        public void setActiveThreads(int activeThreads) {
            this.activeThreads = activeThreads;
        }
    }
}
