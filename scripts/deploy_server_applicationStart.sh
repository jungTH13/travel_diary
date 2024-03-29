REPOSITORY=/home/ubuntu/server

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

echo "java process 종료"

ps -ef | grep java | awk '{print $2}' | sudo xargs kill -9


echo "프로젝트 빌드"

cd $REPOSITORY/travelDiary/travelDiary

./gradlew clean build

cd $REPOSITORY

cp -r $REPOSITORY/travelDiary/travelDiary/build/libs/*.jar ./


echo "java process 종료"

ps -ef | grep java | awk '{print $2}' | sudo xargs kill -9


echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

# sudo nohup java -jar -Duser.timezone=Asia/Seoul $JAR_NAME >> $REPOSITORY/nohup.log 2>&1 &
sudo nohup java -jar -Duser.timezone=Asia/Seoul $JAR_NAME >/dev/null 2>&1 &