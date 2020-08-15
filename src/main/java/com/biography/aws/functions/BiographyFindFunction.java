package com.biography.aws.functions;

import com.amazonaws.services.lambda.runtime.Context;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.core.ResponseInputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.google.gson.Gson;
import java.util.List;

public class BiographyFindFunction 
extends InventoryS3Client
implements RequestHandler<HttpQuerystringRequest, HttpPersonResponse> {

    @Override
    public HttpPersonResponse handleRequest(HttpQuerystringRequest request, Context context) {
    	 
   
        String idAsString = (String)request.getQueryStringParameters().get("id");  
        
        if(idAsString.equalsIgnoreCase("all")) {  
        	Person[] persons = getAllPersons();
        	HttpPersonResponse response = new HttpPersonResponse(persons);
        	
    		return response;
        }
        Integer productId = Integer.parseInt(idAsString);  
        
        Person person = getPersonById(productId);
        
        context.getLogger().log("BIOGRAPHY_LAMBDA: person name " + person.getName());

        HttpPersonResponse httpProductResponse = new HttpPersonResponse(person);
        
        httpProductResponse.getHeaders().put("Access-Control-Allow-Origin", "*"); 
        httpProductResponse.getHeaders().put("Access-Control-Allow-Headers", "Content-Type"); 
        httpProductResponse.getHeaders().put("Access-Control-Allow-Methods", "GET"); 
      
        // cors header, you can add another header fields
        
        return httpProductResponse;
    }

    private Person getPersonById(int personId) {
    	
    	Person [] persons = getAllPersons();
		
		for(Person person : persons) {
			if(person.getId()== personId) {
				return person;
			}
		}
		
		return null;
	}

}
