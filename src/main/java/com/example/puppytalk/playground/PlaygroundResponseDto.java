package com.example.puppytalk.playground;

import lombok.Getter;

@Getter
public class PlaygroundResponseDto {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String operatingHours;
    private String contact;

    public PlaygroundResponseDto(Playground playground) {
        this.name = playground.getName();
        this.address = playground.getAddress();
        this.latitude = playground.getLatitude();
        this.longitude = playground.getLongitude();
        this.operatingHours = playground.getOperatingHours();
        this.contact = playground.getContact();
    }
}