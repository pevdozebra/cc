FROM java:8-jdk

ENV DOCKERIZE_VERSION v0.2.0
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

RUN mkdir /opt/cc
WORKDIR /opt/cc
COPY target/user-service-0.0.1-SNAPSHOT.jar /opt/cc
VOLUME /opt/cc
ENTRYPOINT ["dockerize", "-timeout", "5m", "-wait", "tcp://registry-service:8761", "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/cc/user-service-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
EXPOSE 8085