REPOSITORY=/home/ubuntu/server

echo "java process 종료"

ps -ef | grep java | awk '{print $2}' | sudo xargs kill -9
# ps -ef | grep java => java 라는 이름을 가지는 프로세스 전부 출력
# awk '{print $2}' => 앞 명령어의 결과 컬럼에서 두 번째 필드만 출력
# xargs kill -9 => 앞에서 출력된 값을 인자로 kill -9 을 실행