package com.example.satto.domain.event.service.impl;

import com.example.satto.domain.event.dto.EventCategoryListResponseDto;
import com.example.satto.domain.event.dto.EventCategoryResponseDto;
import com.example.satto.domain.event.entity.Event;
import com.example.satto.domain.event.entity.PhotoContest;
import com.example.satto.domain.event.entity.PhotoContestDislike;
import com.example.satto.domain.event.entity.PhotoContestLike;
import com.example.satto.domain.event.repository.EventRepository;
import com.example.satto.domain.event.repository.PhotoContestDislikeRepository;
import com.example.satto.domain.event.repository.PhotoContestLikeRepository;
import com.example.satto.domain.event.repository.PhotoContestRepository;
import com.example.satto.domain.event.service.EventService;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.global.common.code.status.ErrorStatus;
import com.example.satto.global.common.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final PhotoContestRepository photoContestRepository;
    private final PhotoContestLikeRepository photoContestLikeRepository;
    private final PhotoContestDislikeRepository photoContestDislikeRepository;

    // 이벤트 카테고리 목록 조회
    public EventCategoryListResponseDto getEventCategoryInfoList() {
        List<Event> eventList = eventRepository.findAll();
        List<EventCategoryResponseDto> eventCategoryResponseDtoList = new ArrayList<>();
        for (Event event : eventList) {
            Long participantsCount = photoContestRepository.countByEvent(event);
            EventCategoryResponseDto eventCategoryResponseDto = EventCategoryResponseDto.builder()
                    .category(event.getCategory())
                    .participantsCount(participantsCount)
                    .startWhen(event.getStartWhen())
                    .untilWhen(event.getUntilWhen())
                    .build();
            eventCategoryResponseDtoList.add(eventCategoryResponseDto);
        }
        return EventCategoryListResponseDto.builder()
                .eventCategoryResponseDtoList(eventCategoryResponseDtoList)
                .build();
    }

    // 사진 콘테스트 좋아요 상태 변경
    // TODO BaseResponseStatus로 응답값 반환
    public String likePhotoContest(Long photoContestId, Users user) {
        PhotoContest photoContest = photoContestRepository.findById(photoContestId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_EVENT));

        Optional<PhotoContestLike> photoContestLike = photoContestLikeRepository.findByUserAndPhotoContest(user, photoContest);

        if(photoContestLike.isPresent()) {
            photoContestLikeRepository.delete(photoContestLike.get());
            return "좋아요 취소";
        }
        else {
            photoContestLikeRepository.saveAndFlush(new PhotoContestLike(user, photoContest));
            return "좋아요 부여";
        }
    }

    // 사진 콘테스트 싫어요 상태 변경
    // TODO BaseResponseStatus로 응답값 반환
    public String dislikePhotoContest(Long photoContestId, Users user) {
        PhotoContest photoContest = photoContestRepository.findById(photoContestId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_EVENT));

        Optional<PhotoContestDislike> photoContestDislike = photoContestDislikeRepository.findByUserAndPhotoContest(user, photoContest);

        if(photoContestDislike.isPresent()) {
            photoContestDislikeRepository.delete(photoContestDislike.get());
            return "싫어요 취소";
        }
        else {
            photoContestDislikeRepository.saveAndFlush(new PhotoContestDislike(user, photoContest));
            return "싫어요 부여";
        }
    }
}
