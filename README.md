# localstack-lambda-java-sdk-v1
Sample app for working with AWS Lambda services on Localstack

- Showcases Create, List, Invoke) on Localstack

- Deploys the java-basic sample-app from the aws-lambda-developer-guide 
			(https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic)
	
- java-basic is built targeting the java8.al2 runtime, 
	- accordingly version & release have been changed within pom.xml
	- HandlerStream.java & test folder has been deleted
	
- Only the example.HandlerDivide::handleRequest is shown