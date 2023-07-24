## travel_diary
배포서버 url : https://www.life-traveldiary.net/login
#### 여행을 하며 찍은 사진과 추억을 기록하고 공유하기 위한 프로젝트입니다.
#### 여행의 계획에서부터 기록, 공유까지 제공하는 서비스를 만들고 있습니다.
#### 여러 사람과 더 간편하게 여행의 시작부터 끝까지 함께 할 수 있는 서비스를 목표로 합니다.
</br>

## travel_diary production 인프라(배포) 스트럭처
production 인프라의 경우 terraform으로 작성되었습니다.
![infra_structure](https://github.com/jungTH13/travel_diary/assets/87050915/de57c096-5acd-4fba-8b94-218b235ea71d)
#### 기본 배포 방식은 블루/그린 방식이며 무중단 배포를 지원합니다.
#### autoscaling 방식으로는 scale out 방식을 적용 하였습니다.
<br>

## DATABASE
데이터베이스는 mysql을 사용합니다. 
![다운로드 (2)](https://github.com/jungTH13/travel_diary/assets/87050915/06ab9ccf-3305-42ac-a17e-2d40cdcdc146)
