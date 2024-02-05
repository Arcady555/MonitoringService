FROM maven:3.6.3-openjdk-17

RUN mkdir monitoring_service

WORKDIR monitoring_service

COPY . .

RUN mvn package -Dmaven.test.skip=true

CMD ["java", "-jar", "target/monitoring_service.jar"]