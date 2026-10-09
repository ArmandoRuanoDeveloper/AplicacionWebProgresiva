FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew \
 && ./gradlew bootJar -x test --no-daemon \
 && cp "$(ls build/libs/*.jar | grep -v plain | head -1)" /app/app.jar

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/app.jar app.jar
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]