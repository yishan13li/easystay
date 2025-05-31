package com.easystay.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long id;
    private UserDto userDto;
    private RoomDto roomDto;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;
    private Integer totalPrice;
    private LocalDateTime createdAt;
    
}
