package com.example.with_lettuce.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.with_lettuce.application.PartyService;
import com.example.with_lettuce.dto.request.ParticipateRequest;
import com.example.with_lettuce.dto.request.PartyRequest;
import com.example.with_lettuce.dto.response.ParticipantResponse;
import com.example.with_lettuce.dto.response.PartyResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parties")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @PostMapping
    public ResponseEntity<PartyResponse> create(@RequestBody PartyRequest request) {
        PartyResponse response = partyService.create(request.getName(), request.getCapacity());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{partyId}")
    public ResponseEntity<ParticipantResponse> participate(@PathVariable Long partyId,
                                                           @RequestBody ParticipateRequest request) {
        ParticipantResponse response = partyService.participate(partyId, request.getName());

        return ResponseEntity.ok(response);
    }
}
