package com.example.akupinjam.dto;

import java.util.UUID;

import com.example.akupinjam.models.CustomerDetails;
import com.example.akupinjam.models.Plafond;
import com.example.akupinjam.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailsDto {
    private UUID id;
    private double availablePlafond;
    private UUID plafondId;
    private UserDto user;

    public static CustomerDetailsDto fromEntity(CustomerDetails entity) {
        return new CustomerDetailsDto(
            entity.getId(),
            entity.getAvailablePlafond(),
            entity.getPlafondPlan() != null ? entity.getPlafondPlan().getId() : null,
            UserDto.fromEntity(entity.getUser())
        );
    }

    public CustomerDetails toEntity(Plafond plafond, User user) {
        CustomerDetails entity = new CustomerDetails();
        entity.setId(this.id);
        entity.setAvailablePlafond(this.availablePlafond);
        entity.setPlafondPlan(plafond);
        entity.setUser(user);
        return entity;
    }
}
