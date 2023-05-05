package com.travelProject.travelDiary.service;

import com.travelProject.travelDiary.config.exceptionCode;
import com.travelProject.travelDiary.dto.ErrorCode;
import com.travelProject.travelDiary.entity.Thumbnail;
import com.travelProject.travelDiary.repository.ThumbnailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ThumbnailService {
    @Autowired
    private ThumbnailRepository thumbnailRepository;


    public Long thumbnailInsert(Thumbnail thumbnail) {
        LocalDateTime time = LocalDateTime.now();
        thumbnail.setCreatedDate(time);

        Long id = thumbnail.getId();
        if(id == null) {
            Long thumbnailId = thumbnailRepository.save(thumbnail).getId();
            return thumbnailId;
        } else {
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
    }

    public void thumbnailDelete(Thumbnail thumbnail) {
        Long id = thumbnail.getId();
        if(id > 0){
            throw new exceptionCode(ErrorCode.INVALID_PARAMETER);
        }
        thumbnailRepository.delete(thumbnail);
    }
}