package com.anand.embedabble;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoCode {

    private Double latitude;
    private Double longitude;
}
