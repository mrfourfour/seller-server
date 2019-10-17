FROM openjdk:11
LABEL maintainer="ohg429@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/seller-server.jar
ADD ${JAR_FILE} seller-server.jar
ARG AWS_ACCESS_KEY
ARG AWS_SECRET_KEY
ENV AWS_ACCESS_KEY=$AWS_ACCESS_KEY
ENV AWS_SECRET_KEY=$AWS_SECRET_KEY
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/seller-server.jar"]