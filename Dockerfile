FROM openjdk:11
LABEL maintainer="ohg429@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/ticket-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} seller-server.jar
ARG ACCESS_KEY=$ACCESS_KEY
ARG SECRET_KEY=$SECRET_KEY
ENV AWS_DB_ACCESSKEY=${ACCESS_KEY}
ENV AWS_DB_SECRETKEY=${SECRET_KEY}

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/seller-server.jar"]