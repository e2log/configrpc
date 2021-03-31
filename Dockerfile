FROM amazoncorretto:11
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=container","-jar","/app.jar"]
