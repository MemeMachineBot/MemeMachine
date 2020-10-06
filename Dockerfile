FROM adoptopenjdk/openjdk8-openj9:alpine-slim AS build
WORKDIR /home/gradle/src

COPY settings.gradle build.gradle gradlew ./
COPY gradle ./gradle/

RUN chmod +x ./gradlew

RUN ./gradlew dependencies

COPY src ./src/

RUN ./gradlew shadowJar

FROM adoptopenjdk/openjdk8-openj9:alpine-jre

COPY --from=build /home/gradle/src/build/libs/meme-machine-1.0-all.jar /app/meme-machine.jar

ENTRYPOINT ["java", "-server", "-Xms256M", "-Xmx450M", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=30", "-XX:G1HeapRegionSize=16M", "-XX:ParallelGCThreads=4", "-XX:+DisableExplicitGC", "-XX:+AlwaysPreTouch", "-XX:InitiatingHeapOccupancyPercent=15", "-XX:G1MixedGCLiveThresholdPercent=50", "-XX:+UseNUMA", "-XX:MaxTenuringThreshold=15", "-XX:+UseCompressedOops", "-XX:+OptimizeStringConcat", "-XX:ReservedCodeCacheSize=2048m", "-XX:+CMSParallelRemarkEnabled", "-XX:+UseStringDeduplication", "-jar", "/app/meme-machine.jar"]
