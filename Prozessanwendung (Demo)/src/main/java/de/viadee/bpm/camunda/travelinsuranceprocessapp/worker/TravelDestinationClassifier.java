package de.viadee.bpm.camunda.travelinsuranceprocessapp.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

import java.util.Set;

public class TravelDestinationClassifier {

    private final Set<String> NATIVE_COUNTRIES = Set.of("Deutschland");
    private final Set<String> EU_FOREIGN_COUNTRIES = Set.of("Frankreich", "Schweden", "Italien", "Schweiz");

    @JobWorker(type = "classify-travel-destination-country", fetchVariables = {"travelDestinationCountry"})
    public void classifyTravelDestination(final JobClient client, final ActivatedJob job, @Variable String travelDestinationCountry) {
        String travelDestinationCountryClassification = classifyCountry(travelDestinationCountry);
        client.newCompleteCommand(job)
                .variable("exceededInsuredPartnersMax", travelDestinationCountryClassification)
                .send()
                .join();
    }


    private String classifyCountry(String country) {
        if (NATIVE_COUNTRIES.contains(country)) {
            return "Deutschland";
        }
        else if (EU_FOREIGN_COUNTRIES.contains(country)) {
            return "EU Ausland";
        }
        else {
            return "Nicht-EU Ausland";
        }
    }
}
