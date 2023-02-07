package com.example.firstProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FirstProjectApplication {

	public static void main(String[] args) {
		System.out.println("****************");
		System.out.println(FirstProjectApplication.class.toString());
		Annotation[] annotations =  FirstProjectApplication.class.getAnnotations();
		for (Annotation annotation: annotations
			 ) {
			System.out.println(annotation.toString());
		}


		SpringApplication.run(FirstProjectApplication.class, args);
	}

}
