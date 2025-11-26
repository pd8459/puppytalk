package com.example.puppytalk.hospital;

import lombok.Getter;

@Getter
public class AnimalHospitalResponseDto {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String contact;
    private String operatingHours;

    public AnimalHospitalResponseDto(AnimalHospital hospital) {
        this.name = hospital.getName();
        this.address = hospital.getAddress();
        this.latitude = hospital.getLatitude();
        this.longitude = hospital.getLongitude();
        this.contact = hospital.getContact();
        this.operatingHours = hospital.getOperatingHours();
    }
}