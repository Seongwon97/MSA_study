package com.example.concurrency_problem.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.concurrency_problem.domain.Participant;
import com.example.concurrency_problem.domain.ParticipantRepository;
import com.example.concurrency_problem.domain.Party;
import com.example.concurrency_problem.domain.PartyRepository;
import com.example.concurrency_problem.dto.response.ParticipantResponse;
import com.example.concurrency_problem.dto.response.PartyResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public PartyResponse create(String partyName, int capacity) {
        Party party = partyRepository.save(new Party(partyName, capacity));
        return new PartyResponse(party.getId(), party.getName(), party.getCapacity());
    }

    @Transactional
    public ParticipantResponse participate(Long partyId, String name) {
        Party party = partyRepository.findById(partyId).orElseThrow();
        if (party.isFull()) {
            throw new RuntimeException("정원이 가득 차있습니다.");
        }

        Participant participant = participantRepository.save(new Participant(name, party));
        return new ParticipantResponse(participant.getId(), participant.getName());
    }
}
