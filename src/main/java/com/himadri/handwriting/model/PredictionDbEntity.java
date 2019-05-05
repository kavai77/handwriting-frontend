package com.himadri.handwriting.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter
@Builder
@ToString
public class PredictionDbEntity {
    @Id
    private Long id;
    private String ip;
    private String ipCountry;
    private byte[] image;
    private int preferredPrediction;
    private double preferredConfidence;
    private String predictions;
    @Index
    private Date createTime;
}
