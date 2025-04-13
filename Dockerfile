#---------------------------------------------------------------------
# Stage 1: Build Stage
#---------------------------------------------------------------------
# Use an official Maven image that includes the desired JDK version (Java 17)
# 'AS builder' names this stage so we can refer to it later.
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set the working directory inside the container for this stage
WORKDIR /app

# Copy the Maven Project Object Model file (pom.xml).
# Copying this first leverages Docker's layer caching. The next step
# (dependency download) will only re-run if pom.xml changes.
COPY pom.xml .

# Download project dependencies to the local Maven repository inside the image.
# 'go-offline' attempts to download everything needed for the build.
# '-B' runs Maven in non-interactive (batch) mode.
# ** Alternative if using Maven Wrapper: COPY .mvn/ .mvn/ **
# ** Alternative if using Maven Wrapper: COPY mvnw . **
# ** Alternative if using Maven Wrapper: RUN ./mvnw dependency:go-offline -B **
RUN mvn dependency:go-offline -B

# Copy the application source code into the container.
# This is done *after* downloading dependencies, so changes to source code
# won't cause dependencies to be re-downloaded (unless pom.xml changed too).
COPY src ./src

# Package the application into a JAR file, skipping tests.
# The compiled code and packaged JAR will be in the /app/target directory.
# ** Alternative if using Maven Wrapper: RUN ./mvnw package -DskipTests -B **
RUN mvn package -DskipTests -B

#---------------------------------------------------------------------
# Stage 2: Runtime Stage
#---------------------------------------------------------------------
# Use a slim Java Runtime Environment (JRE) image for the final stage.
# This makes the final image much smaller than using a full JDK.
# Ensure the JRE version matches the JDK used for building (Java 17).
FROM openjdk:17-oracle

# Set the working directory for the final application image
WORKDIR /app

# Copy *only* the executable JAR file from the 'builder' stage.
# The '--from=builder' flag specifies the source stage.
# '/app/target/*.jar' copies the JAR (using wildcard * just in case the name includes version).
# 'app.jar' renames the copied JAR to a consistent name for the ENTRYPOINT.
COPY --from=builder /app/target/*.jar app.jar

# Create a dedicated system user and group with no login shell for security.
# Running the application as a non-root user is a security best practice.
# '--system' flags are typically used for service accounts.
RUN groupadd -r appgroup && useradd -r -g appgroup -s /sbin/nologin appuser
# Switch to the non-root user for running the application.
# Any subsequent RUN, CMD, or ENTRYPOINT instructions will run as this user.
USER appuser

# Inform Docker that the container listens on this port at runtime.
# This is documentation; you still need '-p host_port:container_port' when running.
# Spring Boot default is 8080.
EXPOSE 8080

# Define the command to execute when the container starts.
# Uses the 'exec' form (JSON array) which is preferred.
# This runs the Spring Boot application packaged in app.jar.
ENTRYPOINT ["java", "-jar", "app.jar"]

# Example of adding JVM options (e.g., memory limits):
# ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]