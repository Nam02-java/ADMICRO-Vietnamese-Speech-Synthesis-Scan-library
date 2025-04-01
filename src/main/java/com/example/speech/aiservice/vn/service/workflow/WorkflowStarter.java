package com.example.speech.aiservice.vn.service.workflow;

import com.example.speech.aiservice.vn.service.schedule.TimeDelay;
import com.example.speech.aiservice.vn.service.wait.WaitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowStarter {

    private final PreProcessorService preProcessorService;
    private final WaitService waitService;
    private final TimeDelay timeDelay;

    @Autowired
    public WorkflowStarter(PreProcessorService preProcessorService, WaitService waitService, TimeDelay timeDelay) {
        this.preProcessorService = preProcessorService;
        this.waitService = waitService;
        this.timeDelay = timeDelay;
        startMonitoring();
    }

    public void startMonitoring() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int second = timeDelay.getSecond();
                    waitService.waitForSeconds(5);
                    if (second != 0) {
                        preProcessorService.startWorkflow(second);
                    }
                }
            }
        }).start();
    }
}
