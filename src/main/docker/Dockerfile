FROM java:8
VOLUME /tmp
ADD spring-boot-es-0.0.1-SNAPSHOT.jar springboot-es.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/springboot-es.jar"]
