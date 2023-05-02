package com.example.with_redisson.application;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import com.example.with_redisson.domain.Participant;
import com.example.with_redisson.domain.ParticipantRepository;
import com.example.with_redisson.domain.Party;
import com.example.with_redisson.domain.PartyRepository;
import com.example.with_redisson.dto.response.ParticipantResponse;
import com.example.with_redisson.dto.response.PartyResponse;

import lombok.RequiredArgsConstructor;

@Service
//@Transactional
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final ParticipantRepository participantRepository;
    private final RedissonClient redissonClient;

    public PartyResponse create(String partyName, int capacity) {
        Party party = partyRepository.save(new Party(partyName, capacity));
        return new PartyResponse(party.getId(), party.getName(), party.getCapacity());
    }

    public ParticipantResponse participate(Long partyId, String name) {
        RLock lock = redissonClient.getLock(String.valueOf(partyId));
        try {
            if (!lock.tryLock(100, 2, TimeUnit.SECONDS)) {
                throw new RuntimeException("Lock 획득을 실패했습니다");
            }
            // 비즈니스 로직
            Party party = partyRepository.findById(partyId).orElseThrow();
            if (party.isFull()) {
                throw new RuntimeException("정원이 가득 차있습니다.");
            }

            Participant participant = participantRepository.save(new Participant(name, party));
            return new ParticipantResponse(participant.getId(), participant.getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
