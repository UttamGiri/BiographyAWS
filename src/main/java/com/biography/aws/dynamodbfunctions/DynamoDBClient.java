package com.biography.aws.dynamodbfunctions;

import java.util.Iterator;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

public class DynamoDBClient {
	AmazonDynamoDB client;
	DynamoDBMapper mapper;
	
	public DynamoDBClient() {
		this.client = AmazonDynamoDBClientBuilder.defaultClient();
		this.mapper = new DynamoDBMapper(this.client);
		
	}
	//UpdateItemSpec updateItemSpec = new UpdateItemSpec()
	//DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
	//@DynamoDBDocument
	
	//Item item = table.getItem("Id", 210);
	//why table scan . it is because dynamo db doesnt allow you to filte on field other than primary key. you got to create index  for that you got to pay
	//global secondary index
	
	//withFilterExpression   can be applied to both scan and query . it is because it is filtered after data is pulled . in QuerySpec
	//withKeyConditionExpression and withKeyConditionExpression doesnt work has to create index
	protected ItemCollection<QueryOutcome>  getPersonByAgeAddressQuery(int age, String address) {
		AmazonDynamoDB amazonDynamoDBclient = AmazonDynamoDBClientBuilder.standard().build();
		DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBclient);
		Table table = dynamoDB.getTable("Person");
		 
		QuerySpec spec = new QuerySpec()
		    .withKeyConditionExpression("age = :age")
		            .withValueMap(new ValueMap()
		            .withNumber(":age", age)) 
		    .withKeyConditionExpression("address = :address")
		           .withValueMap(new ValueMap()
		            .withString(":address", address))
		    .withMaxPageSize(10);
		     
		ItemCollection<QueryOutcome> items = table.query(spec);
		return items;
	}
	//withKeyConditionExpression and withFilterExpression   works
	protected ItemCollection<QueryOutcome>  getPersonByIdAddressQuery(int id, String address) {
		AmazonDynamoDB amazonDynamoDBclient = AmazonDynamoDBClientBuilder.standard().build();
		DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBclient);
		Table table = dynamoDB.getTable("Person");
		 
		QuerySpec spec = new QuerySpec()
		    .withKeyConditionExpression("id = :id")
		    .withFilterExpression("address = :address")
		           .withValueMap(new ValueMap()
		                .withNumber(":id", id)
		                .withString(":address", address))
		    .withMaxPageSize(10);
		     
		ItemCollection<QueryOutcome> items = table.query(spec);
		return items;
	}
	
	//withFilterExpression and withFilterExpression   in query doesnt work
		protected ItemCollection<QueryOutcome>  getPersonByIdAddressQuery2(int id, String address) {
			AmazonDynamoDB amazonDynamoDBclient = AmazonDynamoDBClientBuilder.standard().build();
			DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBclient);
			Table table = dynamoDB.getTable("Person");
			 
			QuerySpec spec = new QuerySpec()
			    .withFilterExpression("id = :id")
			    .withFilterExpression("address = :address")
			           .withValueMap(new ValueMap()
			                .withNumber(":id", id)
			                .withString(":address", address))
			    .withMaxPageSize(10);
			     
			ItemCollection<QueryOutcome> items = table.query(spec);
			return items;
		}
		
		//we can use ScanRequest request as well
		//withFilterExpression and withFilterExpression   in scan  works
				protected  Iterator<Item>  getPersonByAgeAddressScan(int age, String address) {
					AmazonDynamoDB amazonDynamoDBclient = AmazonDynamoDBClientBuilder.standard().build();
					DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBclient);
					Table table = dynamoDB.getTable("Person");

					/*   Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
			        expressionAttributeValues.put(":age", age);
			    

			        ItemCollection<ScanOutcome> items = table.scan("age < :age", // FilterExpression
			            "id, age, address", // ProjectionExpression
			            null, // ExpressionAttributeNames - not used in this example
			            expressionAttributeValues);

                    return items;
                    */
					
				ScanSpec scanSpec = new ScanSpec().withProjectionExpression("age, id, address")
				            .withFilterExpression("age < :age")
				            .withValueMap(new ValueMap().withNumber(":age", age));

				      
				            ItemCollection<ScanOutcome> items = table.scan(scanSpec);

				            Iterator<Item> iter = items.iterator();
					
					
			 return iter;
						
				}
	
	protected ItemCollection<QueryOutcome>  getPersonByIdByQuery(int id) {
		AmazonDynamoDB amazonDynamoDBclient = AmazonDynamoDBClientBuilder.standard().build();
		DynamoDB dynamoDB = new DynamoDB(amazonDynamoDBclient);
		Table table = dynamoDB.getTable("Person");
		 
		QuerySpec spec = new QuerySpec()
		    .withKeyConditionExpression("id = :id")
		            .withValueMap(new ValueMap()
		            .withNumber(":id", id)) 
		    .withMaxPageSize(10);
		     
		ItemCollection<QueryOutcome> items = table.query(spec);
		return items;
	}
	
	protected PersonDB getPerson(int id) {
        
        PersonDB personDB = mapper.load(PersonDB.class, id);

		return personDB;
	}
	
   protected PersonDB persistPerson(PersonDB personDB) {
        
         mapper.save(personDB);

		return personDB;
	}
	
//	protected ArrayList<Person> getAllPersonList(){
//		return new ArrayList<Person>(Arrays.asList(getAllPersons()));
//	}	
//	
//	protected boolean updateAllPersons(Person [] products) {
//		
//		Gson gson = new Gson(); 
//        String jsonString = gson.toJson(products);
//		
//		
//		Region region = Region.US_EAST_1;
//        S3Client s3Client = S3Client.builder().region(region).build();
//        
//        PutObjectResponse putResponse = s3Client.putObject(PutObjectRequest.builder()
//        		.bucket("biography-bucket")
//        		.key("person.json").build(),
//        		RequestBody.fromString(jsonString));
//        
//        return putResponse.sdkHttpResponse().isSuccessful();
//        
//	}
//	
//	protected boolean updateAllPersons(List<Person> personList) {
//		Person [] persons = (Person[]) personList.toArray(new Person[personList.size()]);
//		return updateAllPersons(persons);
//	}
	

}
