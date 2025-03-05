#!/bin/bash

docker compose down

# Run Maven clean package
if ./mvnw clean package; then
  echo "Maven build successful. Running Docker Compose..."
  # Run Docker Compose up --build
  docker compose up --build
else
  echo "Maven build failed. Aborting Docker Compose."
  exit 1 # Exit with a non-zero status code to indicate failure
fi

# If both Maven and Docker Compose succeed, script finishes with exit code 0 (success)
