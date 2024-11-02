package com.example.LibraryAPI;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableBook {

    @Id
    private Long bookId;
    private LocalDateTime checkoutTime;
    private LocalDateTime returnTime;
}