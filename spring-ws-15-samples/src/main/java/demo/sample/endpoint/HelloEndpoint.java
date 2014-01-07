package demo.sample.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import demo.sample.entity.HelloRequest;
import demo.sample.entity.HelloResponse;

@Endpoint
public class HelloEndpoint {

	@PayloadRoot(localPart = "HelloRequest", namespace = "http://demo.sample/hello/schema")
	public HelloResponse greeting(HelloRequest request) {
		System.out.println("--------- HelloEndpoint -------------");
		HelloResponse result = new HelloResponse();
		result.setMessage("request.getMessage() was: " + request.getMessage());
		return result;
	}
}
