package de.viadee.bpm.camunda.travelinsuranceprocessapp.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

import java.time.LocalDate;
import java.time.Period;

public class BirthdayToAgeConverter {
    @JobWorker(type = "check-age", fetchVariables = {"birthday"})
    public void checkAge(final JobClient client, final ActivatedJob job, @Variable String birthday) {
        int age = convertBirthdayToAge(LocalDate.parse(birthday));
        client.newCompleteCommand(job)
                .variable("age", age)
                .send()
                .join();
    }

    private int convertBirthdayToAge(LocalDate birthday) {
        LocalDate now = LocalDate.now();
        return Period.between(birthday, now).getYears();
    }
}
