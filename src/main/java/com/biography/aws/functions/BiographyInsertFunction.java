package com.biography.aws.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class BiographyInsertFunction 
extends InventoryS3Client
implements RequestHandler<HttpRequest, HttpProductResponse> {

    @Override
    public HttpProductResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Input: " + request);

        String body = request.getBody();
        
        Gson gson = new Gson();
        Person productToAdd = gson.fromJson(body, Person.class);
        
        List<Person> productsList = getAllPersonList();
        productsList.add(productToAdd);
        
        if(super.updateAllPersons(productsList)) {
    		return new HttpProductResponse();
    	}
        
    	HttpProductResponse response =  new HttpProductResponse();
    	response.setStatusCode("500");
    	return response;

    }

}
