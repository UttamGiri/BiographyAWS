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

public class BiographyFindTestFunction extends InventoryS3Client
implements  RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
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
		return products[0].toString();
    }



}
