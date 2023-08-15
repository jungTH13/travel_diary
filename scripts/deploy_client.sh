REPOSITORY=/home/ubuntu/client

# echo "> 현재 구동 중인 애플리케이션 pid 확인"

# CURRENT_PID=$(pgrep -fla java | grep hayan | awk '{print $1}')

# echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

# if [ -z "$CURRENT_PID" ]; then
#   echo "현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
# else
#   echo "> kill -15 $CURRENT_PID"
#   sudo kill -15 $CURRENT_PID
#   sleep 5
# fi

echo "> process 종료"

ps -ef | grep node | awk '{print $2}' | sudo xargs kill -9
# ps -ef | grep java => java 라는 이름을 가지는 프로세스 전부 출력
# awk '{print $2}' => 앞 명령어의 결과 컬럼에서 두 번째 필드만 출력
# xargs kill -9 => 앞에서 출력된 값을 인자로 kill -9 을 실행


echo "> 새 애플리케이션 배포"

# JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)

cd $REPOSITORY

sudo docker-compose up -d

# sudo npm install

# sudo nohup npm run dev >> $REPOSITORY/nohup.out 2>&1 &