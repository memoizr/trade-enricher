## Java Coding task

#### How to run the project
After cloning to your local machine you can try running the pre-built application using:

```bash
java -jar dist/task-0.0.1-SNAPSHOT.jar
```

Please ensure Java 17 in installed on your machine and that port 8080 is not in use and that this command is executed from the root of the project.

### How to build the project
This command will run the tests and create a deployable jar file in `./target` directory of the project:
```bash
./mvnw clean package
```

### How to test the basic functionality
Once the app is up and running please feel free to test the app functionality by running this command from the root of the project:
```bash
curl --request POST --data-binary @trade.csv --header 'Content-Type: text/csv' --header 'Accept: text/csv' http://localhost:8080/api/v1/enrich
```

### How to access logs
Logs, including the error messages, are printed in the STDOUT of the program. 


## Considerations

This solution has some obvious tradeoffs, due in part to the way the requirements were stated, and in part to the time I allocated to it. Here's as few things to consider:
### Use of static file for `product.csv`
This solution obviously does not lend itself well to scaling to very large datasets due to memory constraints, as well as application startup times. This was a requirement included in the problem statement, and while it could have been challenged, I decided to stick with it because:
* There might be a reason why it was specifically stated it should be a static file
* Deciding on a more robust/scalable alternative would require a discussion about performance requirements, databases etc
* It was simple and fun to do it like this

That said, I took the liberty to make it so the file is accessed from the file system as opposed to the resources of the application, thus potentially somewhat decoupling the deployment of the data and the deployment of the app. Jar file size would not grow as the dataset grows, which is another advantage. The path to the file can be configured in the application properties.
Furthermore, I set up the project in such a way that it should be straightforward to provide a different implementation of the `ProductRepository` so that for example a database can be used instead of a local file, and the rest of the application logic would not change much.

### Use of CSV format in plain text "monolithic" request/response
This implementation detail was also somewhat strongly implied from the way the problem was presented. Again I chose to stick as closely to the requirements, but depending on the actual application's needs there are a number of improvements that could be made:
* Exposing a JSON (or alternative) API for ease of integration with third parties
* Using an asynchronous approach
* Using a streaming approach

### Lack of authentication/authorization
It's pretty standard to protect your API from unauthorized access, but this crucial feature was out-of-scope for this exercise.

### Lack of performance monitoring and error/log management
This solution only included a very crude logging solution and absolutely no performance monitoring. There is a number of commercially available solutions to these problems that we could choose from, and a proper solution to this problem is crucial as the application scales for the purposes of security, auditing, triaging, debugging etc.

### No containerization
For a production-ready solution I'd have gone for a containerized approach, but again, that'd require choosing which conainer solution, deployment strategy to use and so on, so I left it as out-of-scope to keep it simple.

### Functional style
In my experience I've done a fair bit of functional/reactive programming so that's my go-to style when I am given the freedom to choose an approach. That said, not all teams and projects are fully on board with this style, and in a real situation this is one of those things that should be discussed with the team first.

### General approach
In general, I tried to stick to a minimalistic, KISS approach, as I believe trying to reduce complexity unless needed is usually a winning strategy. I always try to write concise and readable code, preferring a functional/declarative approach when possible. Whenever I had to make a tradeoff between readability and performance/efficiency, I went with readability and simplicity, as I believe optimizing code for performance is usually a pointless exercise if you don't know exactly what constraints, use cases and targets we have (YAGNI and all that).  

The code was written in a TDD way (outside in) approach. I started from the integration test and gradually added and extracted the necessary components. As a result, the code turned out to have 84% coverage by lines without having optimized for this metric at all, with most of the lines missing belonging to an Exception class. 
