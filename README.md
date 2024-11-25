# localstack-lambda-java-sdk-v1
Sample app for working with AWS Lambda services on Localstack

- Filling in for the missing AWS Lamda bit in the [localstack-aws-sdk-examples/java-sdk-examples/java-sdk-v1](https://github.com/localstack/localstack-aws-sdk-examples/tree/main/java/java-sdk-examples/java-sdk-v1) repo

- Showcases Create, List, Invoke calls of AWS Lambda on Localstack

- Deploys the java-basic sample-app from the [aws-lambda-developer-guide](https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic)
	
- Java-basic is built targeting the java8.al2 runtime:
	- Accordingly version & release have been changed within pom.xml
	- HandlerStream.java & test folder has been deleted
	
- Only the example.HandlerDivide::handleRequest is shown