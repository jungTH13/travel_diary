package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    List<Thumbnail> findByIdIn(List<Long> idList);
}