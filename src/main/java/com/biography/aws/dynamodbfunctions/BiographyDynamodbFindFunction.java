package com.biography.aws.dynamodbfunctions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.biography.aws.functions.HttpProductResponse;
import com.biography.aws.functions.HttpQuerystringRequest;
import com.biography.aws.functions.InventoryS3Client;
import com.biography.aws.functions.Person;

public class BiographyDynamodbFindFunction extends DynamoDBClient implements RequestHandler<HttpQuerystringRequest, HttpProductResponse> {

    @Override
    public HttpProductResponse handleRequest(HttpQuerystringRequest request, Context context) {
        context.getLogger().log("Input: " + request);
        
        String idAsString = (String)request.getQueryStringParameters().get("id");  
        
        if(idAsString.equalsIgnoreCase("all")) {  
        	Person[] products = null;
        	HttpProductResponse response = new HttpProductResponse(products);
        	
    		return response;
        }
        Integer productId = Integer.parseInt(idAsString);  
        
        PersonDB personDB = savePerson();
            
        return null;
    }
    
    private PersonDB getPersonById(int personId) {
    	DynamoDBClient client = new DynamoDBClient();
    	PersonDB person = client.getPerson(personId);
		return person;

	}
    
    private void getPersonByIdQuery(int personId) {
    	try {
        	DynamoDBClient client = new DynamoDBClient();
        	ItemCollection<QueryOutcome> items = client.getPersonByIdByQuery(personId);
        	
        	Iterator<Item> iterator = items.iterator(); 

        	while (iterator.hasNext()) { 
         	   System.out.println(iterator.next().toJSONPretty()); 
        	}
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}

	}
    /* this only works if index created
    private void getPersonByAgeAndAddress(int age, String address) {
    	try {
    	DynamoDBClient client = new DynamoDBClient();
    	ItemCollection<QueryOutcome> items = client.getPersonByAgeAddressQuery(age, address);
    	
    	Iterator<Item> iterator = items.iterator(); 

    	while (iterator.hasNext()) { 
    	   System.out.println(iterator.next().toJSONPretty()); 
    	}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
		

	}
	*/
    
    private void getPersonByAgeAndAddressScan(int age, String address) {
    	try {
    	DynamoDBClient client = new DynamoDBClient();
    	// ItemCollection<ScanOutcome> items = client.getPersonByAgeAddressScan(age, address);
    	Iterator<Item> iterator = client.getPersonByAgeAddressScan(age, address);
    	

//    	   	Iterator<Item> iterator = items.iterator(); 

        	while (iterator.hasNext()) { 
         	   System.out.println(iterator.next().toJSONPretty()); 
        	}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
		

	}

    private PersonDB savePerson() {
    	PersonDB p = new PersonDB();
    	DynamoDBClient client = new DynamoDBClient();
    	
    	p.setId(1);
    	p.setName("uttam");
    	p.setAddress("fl");
    	p.setAge(2);
    	
    	PersonDB person = client.persistPerson(p);	
		return person;

	}
    
    public static void main(String[] args) {
    	BiographyDynamodbFindFunction biographyDynamodbFindFunction = new BiographyDynamodbFindFunction();
    	//biographyDynamodbFindFunction.savePerson();
    	
    	//PersonDB result = biographyDynamodbFindFunction.getPersonById(1);
    	//System.out.println(result.getName());
    	
    	//biographyDynamodbFindFunction.getPersonByIdQuery(1);
    	
    	// biographyDynamodbFindFunction.getPersonByAgeAndAddress(1, "florida");
    	
    	biographyDynamodbFindFunction.getPersonByAgeAndAddressScan(100, "fl");

    }
}
