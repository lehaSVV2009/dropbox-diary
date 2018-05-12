FROM openjdk:8

COPY build/libs/dropbox-diary-api.jar /app.jar

EXPOSE 8080

ENTRYPOINT [ "java" ]

CMD ["-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
