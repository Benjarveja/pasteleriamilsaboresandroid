package cl.milsabores.pasteleria.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserProfileResponse {
    String id;
    String email;
    String firstName;
    String lastName;
    String run;
    String phone;
    String birthDate;
    String region;
    String comuna;
    String street;
    String address;
}

