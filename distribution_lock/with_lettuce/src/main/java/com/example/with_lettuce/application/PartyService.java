package com.example.with_lettuce.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.with_lettuce.domain.Participant;
import com.example.with_lettuce.domain.ParticipantRepository;
import com.example.with_lettuce.domain.Party;
import com.example.with_lettuce.domain.PartyRepository;
import com.example.with_lettuce.dto.response.ParticipantResponse;
import com.example.with_lettuce.dto.response.PartyResponse;
import com.example.with_lettuce.infrastructure.redis.RedisLockRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final ParticipantRepository participantRepository;
    private final RedisLockRepository redisLockRepository;

    public PartyResponse create(String partyName, int capacity) {
        Party party = partyRepository.save(new Party(partyName, capacity));
        return new PartyResponse(party.getId(), party.getName(), party.getCapacity());
    }

    public ParticipantResponse participate(Long partyId, String name) {
        try {
            // spin lock을 통해 100ms마다 락을 얻을 수 있는지 확인
            while (!redisLockRepository.lock(partyId)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            Party party = partyRepository.findById(partyId).orElseThrow();
            if (party.isFull()) {
                throw new IllegalArgumentException("정원이 가득 차있습니다.");
            }

            Participant participant = participantRepository.save(new Participant(name, party));
            return new ParticipantResponse(participant.getId(), participant.getName());
        } finally {
            redisLockRepository.unlock(partyId);
        }
    }
}
