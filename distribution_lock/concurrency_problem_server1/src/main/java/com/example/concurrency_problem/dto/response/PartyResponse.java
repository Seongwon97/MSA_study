package com.example.concurrency_problem.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PartyResponse {

    private final Long id;
    private final String name;
    private final int capacity;
}
