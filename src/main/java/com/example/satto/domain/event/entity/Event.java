package com.example.satto.domain.event.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String category;
    private LocalDateTime startWhen;
    private LocalDateTime untilWhen;

    @OneToOne(mappedBy = "event")
    private PhotoContest photoContest;

}
