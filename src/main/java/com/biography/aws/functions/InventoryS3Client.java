package com.biography.aws.functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

public class InventoryS3Client {
	
	protected Person[] getAllPersons() {
		Region region = Region.US_EAST_1;
        S3Client s3Client = S3Client.builder().region(region).build();
        ResponseInputStream<?> objectData = s3Client.getObject(GetObjectRequest.builder()
        		.bucket("biography-bucket")
        		.key("person.json")
        		.build());
        
        InputStreamReader isr = new InputStreamReader(objectData);
        BufferedReader br = new BufferedReader(isr);
        
        Person [] products = null;

		Gson gson = new Gson();
		products = gson.fromJson(br, Person[ ].class);
		return products;
	}
	
	protected ArrayList<Person> getAllPersonList(){
		return new ArrayList<Person>(Arrays.asList(getAllPersons()));
	}	
	
	protected boolean updateAllPersons(Person [] products) {
		
		Gson gson = new Gson(); 
        String jsonString = gson.toJson(products);
		
		
		Region region = Region.US_EAST_1;
        S3Client s3Client = S3Client.builder().region(region).build();
        
        PutObjectResponse putResponse = s3Client.putObject(PutObjectRequest.builder()
        		.bucket("biography-bucket")
        		.key("person.json").build(),
        		RequestBody.fromString(jsonString));
        
        return putResponse.sdkHttpResponse().isSuccessful();
        
	}
	
	protected boolean updateAllPersons(List<Person> personList) {
		Person [] persons = (Person[]) personList.toArray(new Person[personList.size()]);
		return updateAllPersons(persons);
	}
	

}
