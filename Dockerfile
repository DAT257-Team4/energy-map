# Use an official Node.js runtime as a parent image
FROM node:18

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Update the package lists and install the `build-essential` package in the container.
RUN apt-get update 
RUN apt-get install -y build-essential

# Install the OpenJDK 17 JDK and set env variable
RUN apt-get install -y openjdk-17-jdk
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64/
RUN echo $JAVA_HOME

# Change working directory to /app/server
WORKDIR /app/server

# Install any needed packages specified in package.json
RUN npm install

# Build the application
RUN npm run build

# Make port 3000 available to the world outside this container
EXPOSE 3000

# Define environment variable
ENV NODE_ENV production

# Run app.js when the container launches
CMD ["npm", "start"]