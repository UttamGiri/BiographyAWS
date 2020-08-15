package com.biography.aws.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class BiographyUpdateFunction 
extends InventoryS3Client
implements RequestHandler<HttpRequest, HttpPersonResponse> {

    @Override
    public HttpPersonResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Input: " + request);

        Gson gson = new Gson();
        String body = request.getBody();
        Person productToAdd = gson.fromJson(body, Person.class);
        
        List<Person> personList = getAllPersonList();
        personList.removeIf(prod -> prod.getId() == productToAdd.getId());
        
        HttpPersonResponse httpResponse = new HttpPersonResponse(productToAdd);
        
        personList.add(productToAdd);
		if(!super.updateAllPersons(personList)) {
			httpResponse.setStatusCode("500");
		}

    	return httpResponse;
    }

}
