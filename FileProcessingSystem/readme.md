# File Processing System

File Processing System is a Java-based application that simulates file generation, processing, and queue management in a multi-threaded environment. This system allows you to generate files with specific characteristics, add them to a queue, and process them with various handlers.

## Features

- File generation with random characteristics: type (JSON, XML, XLS) and size.
- Multi-threaded file processing with handlers for different file types.
- Queue management to limit the number of files in the queue.

## Components

The project consists of the following components:

1. **File Generator**: Generates files with random characteristics and adds them to the queue.

2. **File Processor**: Processes files from the queue based on their type (e.g., JSON, XML, XLS). The processing time depends on the file size.

3. **Queue Management**: Ensures that the queue does not exceed its capacity.

## Getting Started

To get started with the File Processing System, follow these steps:

1. Clone the repository to your local machine:

   _git clone https://github.com/Barashkov-GO/java_labs.git_

2. Navigate to the project directory:
   
    _cd java_labs/FileProcessingSystem_

3. Compile the Java files:

    _javac FileProcessingSystem.java_

4. Run the File Processing System:

    _java FileProcessingSystem_
