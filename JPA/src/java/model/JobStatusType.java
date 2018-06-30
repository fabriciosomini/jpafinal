/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author fabri
 */
public enum JobStatusType {
    UNASSIGNED (100),
    NOT_STARTED(101),
    STARTED(102),
    CANCELED_BY_HIREE(103),
    CANCELED_BY_HIRER(104),
    DONE(105),
    APPROVED(106),
    REPROVED(107);

    public static JobStatusType getValue(int jobStatusTypeValue) {
        List<JobStatusType> values = Arrays.asList(values());
   
        JobStatusType value = values.stream()
                .filter(js -> js.getValue() == jobStatusTypeValue)
                .findAny()
                .orElse(null);
        
        return value;
    }

    private final int value;

    private JobStatusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

