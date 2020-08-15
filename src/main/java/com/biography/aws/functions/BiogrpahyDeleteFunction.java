package com.biography.aws.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BiogrpahyDeleteFunction 
extends InventoryS3Client
implements RequestHandler<HttpRequest, HttpPersonResponse> {

    @Override
    public HttpPersonResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Input: " + request);

        String idAsString = (String)request.pathParameters.get("id");
        Integer personId = Integer.parseInt(idAsString); 
        
        List<Person> personList = getAllPersonList();
        
        boolean didRemove = personList.removeIf(prod -> prod.getId() == personId);
        
        if(didRemove) {
        	if(updateAllPersons(personList)) {
        		return new HttpPersonResponse();
        	}
        }
        HttpPersonResponse response = new HttpPersonResponse();
        response.setStatusCode("404");
		return response;

    }

}
