package com.biography.aws.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class BiographyInsertFunction 
extends InventoryS3Client
implements RequestHandler<HttpRequest, HttpPersonResponse> {

    @Override
    public HttpPersonResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Input: " + request);

        String body = request.getBody();
        context.getLogger().log("BIOGRAPHY_LAMBDA INSERT: " + "body >>>" + body);
        
        Gson gson = new Gson();
        Person productToAdd = gson.fromJson(body, Person.class);
        
        List<Person> productsList = getAllPersonList();
        productsList.add(productToAdd);
        
        if(super.updateAllPersons(productsList)) {
    		return new HttpPersonResponse();
    	}
        
    	HttpPersonResponse response =  new HttpPersonResponse();
    	response.setStatusCode("500");
    	return response;

    }

}
