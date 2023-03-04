package com.travelProject.travelDiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // EntityListeners => CreatedDate 어노테이션 사용을 위해 추가 (JpaAuditing 기능 사용하기 위해 추가)
@SpringBootApplication
public class TravelDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelDiaryApplication.class, args);
	}

}
