package com.tencentcloud.cls.producer.errors;

/**
 * @author farmerx
 */
public class MaxBatchCountExceedException extends ProducerException {
    public MaxBatchCountExceedException(String message) {
        super(message);
    }
}
