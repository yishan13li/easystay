package com.easystay.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomImageDto {
	
    private Long id;
    private Long roomId;
    private String url;
    private Boolean isCover;
    private LocalDateTime uploadedAt;
}
