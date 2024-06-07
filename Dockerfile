FROM eclipse-temurin:17-jre

WORKDIR /app/ngfp-filing-service

COPY ./build/libs/ngfp-filing-service*.jar ngfp-filing-service.jar

CMD ["java", "-jar", "ngfp-filing-service.jar"]
