FROM openjdk:17-oracle
WORKDIR /app
COPY target/task-management-system-0.0.1-SNAPSHOT.jar school-security-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar"]
CMD ["school-security-0.0.1-SNAPSHOT.jar"]