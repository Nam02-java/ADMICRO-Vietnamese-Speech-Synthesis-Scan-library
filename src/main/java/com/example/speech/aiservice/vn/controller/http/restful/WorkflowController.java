package com.example.speech.aiservice.vn.controller.http.restful;

import com.example.speech.aiservice.vn.service.workflow.PreProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    private final PreProcessorService preProcessorService;

    @Autowired
    public WorkflowController(PreProcessorService preProcessorService) {
        this.preProcessorService = preProcessorService;
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopWorkflow() {
        System.out.println("Received STOP request!");
        preProcessorService.stopConditions();
        return ResponseEntity.ok("Workflow stopped successfully!");
    }
}
