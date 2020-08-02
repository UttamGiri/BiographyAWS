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
implements RequestHandler<HttpQuerystringRequest, HttpProductResponse> {

    @Override
    public HttpProductResponse handleRequest(HttpQuerystringRequest request, Context context) {
        context.getLogger().log("Input: " + request);
        
        String idAsString = (String)request.getQueryStringParameters().get("id");  
        
        if(idAsString.equalsIgnoreCase("all")) {  
        	Person[] persons = getAllPersons();
        	HttpProductResponse response = new HttpProductResponse(persons);
        	
    		return response;
        }
        Integer productId = Integer.parseInt(idAsString);  
        
        Person person = getPersonById(productId);
            
        return new HttpProductResponse(person);
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
