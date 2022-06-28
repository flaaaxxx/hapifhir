package com.example.hapifhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Example {

    @EventListener(ApplicationReadyEvent.class)
    public void start(){
// We're connecting to a DSTU1 compliant server in this example
        FhirContext ctx = FhirContext.forR4();
        String serverBase = "https://hapi.fhir.org/baseR4";

        IGenericClient client = ctx.newRestfulGenericClient(serverBase);

// Perform a search
        Bundle results = client
                .search()
                .forResource(Patient.class)
//                .where(Patient.FAMILY.matches().value("duck"))
                .where(Patient.ADDRESS_CITY.matches().value("Chicago"))
                .returnBundle(Bundle.class)
                .execute();

        System.out.println("Found " + results.getEntry().size() + " patients named 'duck'");

        Observation obs = new Observation();

// These are all equivalent
//        obs.setIssuedElement(new InstantType(new Date()));
//        obs.setIssuedElement(new InstantType(new Date(), TemporalPrecisionEnum.MILLI));
        obs.setIssued(new Date());

// The InstantType also lets you work with the instant as a Java Date
// object or as a FHIR String.
        Date date = obs.getIssuedElement().getValue(); // A date object
        String dateString = obs.getIssuedElement().getValueAsString(); // "2014-03-08T12:59:58.068-05:00"

        System.out.println(dateString);
    }

}
