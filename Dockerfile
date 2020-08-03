FROM adoptopenjdk/openjdk8-openj9:alpine-slim AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew shadowJar --no-daemon

FROM adoptopenjdk/openjdk8-openj9:alpine-jre

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/meme-machine-1.0-all.jar /app/meme-machine.jar

ENTRYPOINT ["java", "-server", "-Xms256M", "-Xmx450M", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=30", "-XX:G1HeapRegionSize=16M", "-XX:ParallelGCThreads=4", "-XX:+DisableExplicitGC", "-XX:+AlwaysPreTouch", "-XX:InitiatingHeapOccupancyPercent=15", "-XX:G1MixedGCLiveThresholdPercent=50", "-XX:+UseNUMA", "-XX:MaxTenuringThreshold=15", "-XX:+UseCompressedOops", "-XX:+OptimizeStringConcat", "-XX:ReservedCodeCacheSize=2048m", "-XX:+CMSParallelRemarkEnabled", "-XX:+UseStringDeduplication", "-noverify", "-jar", "/app/meme-machine.jar"]
