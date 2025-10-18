package com.projectathena.mineworkerservice.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.stereotype.Component;


@Component
public class WorkerIdProvider {

    private final String workerId;
    Logger logger = LoggerFactory.getLogger(WorkerIdProvider.class);

    public WorkerIdProvider(EurekaInstanceConfigBean eurekaInstanceConfig) {
        this.workerId = eurekaInstanceConfig.getInstanceId();
        logger.info("Worker with ID: {}", this.workerId);
    }

    public String getWorkerId() {
        return this.workerId;
    }
}