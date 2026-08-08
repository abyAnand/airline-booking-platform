package com.anand.payload.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityResponse {

    private Long id;
    private String name;
    private String cityCode;
    private String countryName;
    private String countryCode;
    private String regionCode;
    private String timeZoneOffset;
}
