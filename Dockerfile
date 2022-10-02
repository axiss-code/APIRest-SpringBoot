FROM openjdk:11-jre
copy target/jfc-0.0.1-SNAPSHOT.jar jfc-0.0.1-SNAPSHOT.jar
entrypoint ["java", "-jar", "/fc-0.0.1-SNAPSHOT.jar"]
