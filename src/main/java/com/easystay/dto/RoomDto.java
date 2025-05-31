package com.easystay.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private Long id;
    private String name;
    private String description;
    private Integer pricePerNight;
    private Boolean available;
    private String location;
    private Integer maxGuests;
    private String roomType;
    private Float latitude;
    private Float longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<AmenityDto> amenities;
    private Set<RoomImageDto> images;
    private List<ReservationDto> reservations;
    
}
