package com.travelProject.travelDiary.repository;

import com.travelProject.travelDiary.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
}