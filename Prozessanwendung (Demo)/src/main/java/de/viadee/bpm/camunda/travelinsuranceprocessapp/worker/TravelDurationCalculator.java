package de.viadee.bpm.camunda.travelinsuranceprocessapp.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class TravelDurationCalculator {

    @JobWorker(type = "calculate-travel-duration", fetchVariables = {"travelStart", "travelEnd"})
    public void calculateTravelDuration(final JobClient client, final ActivatedJob job, @Variable String travelStartInput, @Variable String travelEndInput) {
        LocalDate travelStart = LocalDate.parse(travelStartInput);
        LocalDate travelEnd = LocalDate.parse(travelEndInput);
        long travelDurationInDays = calculateDurationInDays(travelStart, travelEnd);
        client.newCompleteCommand(job)
                .variable("travelDurationInDays", travelDurationInDays)
                .send()
                .join();
    }

    private long calculateDurationInDays(LocalDate start, LocalDate end) {
        return DAYS.between(start, end);
    }
}
