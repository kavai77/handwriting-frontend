package com.himadri.handwriting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.ObjectifyService;
import com.himadri.handwriting.model.Pixels;
import com.himadri.handwriting.model.Prediction;
import com.himadri.handwriting.model.PredictionDbEntity;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

@Component
public class StorageService {
    private DatabaseReader dbReader;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws IOException {
        ObjectifyService.init();
        ObjectifyService.register(PredictionDbEntity.class);
        dbReader = new DatabaseReader.Builder(getClass().getResourceAsStream("/GeoLite2-Country.mmdb")).build();
    }

    public void storePrediction(String remoteAddress, Pixels pixels, List<Prediction> predictions) {
        Prediction preferredPrediction = predictions.stream().filter(Prediction::isPreferred).findFirst().orElseThrow(RuntimeException::new);
        String country;
        try {
            country = dbReader.country(InetAddress.getByName(remoteAddress)).getCountry().getName();
        } catch (Exception e) {
            country = null;
        }
        String predictionsJson;
        try {
            predictionsJson = objectMapper.writeValueAsString(predictions);
        } catch (JsonProcessingException e) {
            predictionsJson = null;
        }
        PredictionDbEntity entity = PredictionDbEntity.builder()
            .ip(remoteAddress)
            .ipCountry(country)
            .image(pixels.getPngEncoded())
            .preferredPrediction(preferredPrediction.getPrediction())
            .preferredConfidence(preferredPrediction.getConfidence())
            .predictions(predictionsJson)
            .createTime(new Date())
            .build();
        ObjectifyService.ofy().save().entity(entity);
    }
}
