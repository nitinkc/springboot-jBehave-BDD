package com.bookstore.jbehave.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for JSONPlaceholder API integration
 * Represents user data from https://jsonplaceholder.typicode.com/users
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalUserDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private AddressDto address;
    private String phone;
    private String website;
    private CompanyDto company;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private GeoDto geo;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class GeoDto {
            private String lat;
            private String lng;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyDto {
        private String name;
        private String catchPhrase;
        private String bs;
    }
}