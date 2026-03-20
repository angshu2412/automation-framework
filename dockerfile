# Use official Maven image with Java 11 to build and run tests
FROM maven:3.9.6-eclipse-temurin-11

# Install Chrome and dependencies for headless execution
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    unzip \
    gnupg \
    --no-install-recommends

# Add Google Chrome repository and install
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
    >> /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Set working directory inside container
WORKDIR /app

# Copy pom.xml first - Docker caches this layer
# Dependencies only re-download if pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy rest of project
COPY src ./src
COPY testng.xml .

# Run tests in headless mode by default
# -Dheadless=true overrides config.properties value
CMD ["mvn", "clean", "test", \
     "-Dheadless=true", \
     "-Dbrowser=chrome", \
     "-Dcucumber.filter.tags=@regression"]