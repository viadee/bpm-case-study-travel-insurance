package de.viadee.bpm.camunda.travelinsuranceprocessapp.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;

import java.time.LocalDate;
import java.time.Period;

public class AgeChecker {
    private final int AGE_OF_ADULTHOOD_IN_GERMANY = 18;

    @JobWorker(type = "check-age", fetchVariables = {"age"})
    public void checkAge(final JobClient client, final ActivatedJob job, @Variable int age) {
        boolean isAdult = AGE_OF_ADULTHOOD_IN_GERMANY <= age;
        client.newCompleteCommand(job)
                .variable("policyHolderIsAdult", isAdult)
                .send()
                .join();
    }
}
