package de.viadee.bpm.camunda.travelinsuranceprocessapp.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

public class InsuredPartnersChecker {

    public static final int INSURED_PARTNERS_MAX = 6;

    @JobWorker(type = "check-insured-partners", fetchVariables = {"insuredPartnersTotal"})
    public void checkInsuredPartners(final JobClient client, final ActivatedJob job, @Variable int insuredPartnersTotal) {
        boolean exceededInsuredPartnersMax = insuredPartnersTotal > INSURED_PARTNERS_MAX;
        client.newCompleteCommand(job)
                .variable("exceededInsuredPartnersMax", exceededInsuredPartnersMax)
                .send()
                .join();
    }
}
