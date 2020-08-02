package com.biography.aws.functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

public class NormalStringFindFunction implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

      
        Region region = Region.US_EAST_1;
        S3Client s3Client = S3Client.builder().region(region).build();
        ResponseInputStream<?> objectData = s3Client.getObject(GetObjectRequest.builder()
        		.bucket("biography-bucket")
        		.key("testdata.txt")
        		.build());
        
        InputStreamReader isr = new InputStreamReader(objectData);
        BufferedReader br = new BufferedReader(isr);
        
         String outputString = null;
         
         try {
        	 outputString = br.readLine();
        	 br.close();
         }catch(Exception e) {
        	 context.getLogger().log("Error : " + e.getMessage());
         }
          
        
        return outputString;
    }

}
