package com.example.concurrency_problem.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipantResponse {
    private final Long id;
    private final String name;
}
