package com.himadri.handwriting;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.himadri.handwriting.model.Pixels;
import com.himadri.handwriting.model.Prediction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ComputeGateway {
    private ObjectMapper objectMapper = new ObjectMapper();

    public List<Prediction> callPredictionHttpClient(Pixels pixels) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            HttpPost httpPost = new HttpPost("http://18.224.135.237/service/prediction");
            PredictionComputeInput input = new PredictionComputeInput(pixels.getTransformedPixels(), pixels.getRawPixels());
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(input), ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            return objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<Prediction>>() { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AllArgsConstructor
    @Getter
    private static class PredictionComputeInput {
        private final double[] transformedPixels;
        private final double[] rawPixels;
    }

}
