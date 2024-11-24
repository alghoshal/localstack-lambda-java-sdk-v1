package v1.lambda;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.CreateFunctionRequest;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.amazonaws.services.lambda.model.GetFunctionRequest;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.ListFunctionsResult;
import com.amazonaws.services.lambda.model.ResourceNotFoundException;

/**
 * Sample AWS Lambda Service (Create, List, Invoke) on Localstack.
 * 
 * - Deploys the java-basic sample-app from the aws-lambda-developer-guide 
 * 		(https://github.com/awsdocs/aws-lambda-developer-guide/tree/main/sample-apps/java-basic)
 * 
 * - java-basic is built targeting the java8.al2 runtime, 
 * 		- accordingly version & release have been changed within pom.xml & 
 * 		- HandlerStream.java & test folder has been deleted
 * 
 * - Only the example.HandlerDivide::handleRequest is shown
 * 
 * @algo
 */
public class LambdaService {

	static final String ROLE = "arn:aws:iam::000000000000:role/lambda-test";
	static final String RUNTIME = "java8.al2";
	static final String HANDLER = "example.HandlerDivide::handleRequest";
	
	// credentials that can be replaced with real AWS values
	static final String ACCESS_KEY = "test";
	static final String SECRET_KEY = "test";
	static final String FUNC_NAME = "test-fromj-div";

	// create BasicAWSCredentials using the access key and secret key
	static BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

	static AWSLambda lambdaClient = AWSLambdaClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withEndpointConfiguration(new EndpointConfiguration("http://localhost:4566", Regions.US_EAST_1.getName()))
			.build();

	public static void main(String[] args) throws IOException, URISyntaxException {
		if (!functionExists())
			createFunction(AWSLambda.class.getResource("/original-java-basic-1.0-SNAPSHOT.jar").toURI().getPath());
		System.out.println(listFunctions());
		System.out.println(invokeFunction());
	}

	static ListFunctionsResult listFunctions() {
		return lambdaClient.listFunctions();
	}
	
	static boolean functionExists() {
		try {
			return null != lambdaClient.getFunction(new GetFunctionRequest().withFunctionName(FUNC_NAME));
		} catch (ResourceNotFoundException rnf) {
			System.out.println("Function does not exist");
			return false;
		}
	}

	static void createFunction(String zipJarPath) throws IOException {
		FunctionCode code = new FunctionCode();
		try (FileChannel fchan = (FileChannel) Files.newByteChannel(Paths.get(zipJarPath))) {
			code.setZipFile(fchan.map(FileChannel.MapMode.READ_ONLY, 0, fchan.size()));

			CreateFunctionRequest funcRequest = new CreateFunctionRequest()
					.withHandler(HANDLER).withRuntime(RUNTIME)
					.withRole(ROLE).withFunctionName(FUNC_NAME).withCode(code);

			System.out.println(funcRequest);
			lambdaClient.createFunction(funcRequest);
		}
	}

	static String invokeFunction() {
		InvokeRequest request = new InvokeRequest().withFunctionName(FUNC_NAME).withPayload("[235241,17]");

		InvokeResult result = lambdaClient.invoke(request);
		return new String(result.getPayload().array());
	}
}
