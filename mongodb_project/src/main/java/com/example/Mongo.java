package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Mongo {

    public static void main(String[] args) {
                String url = "mongodb://localhost:27017";
        
       try {
        	MongoClient mongoClient = MongoClients.create(url);
            
            
            MongoDatabase db = mongoClient.getDatabase("sample");
            MongoCollection<Document> collection = db.getCollection("sample");
            
            System.out.println("Connected to database: " + db.getName());
            
            try {
                String content = Files.readString(Path.of("C:\\Users\\sanja\\Desktop\\Ethnotech\\product (1).json"));
                
               
                if (content.trim().startsWith("[")) {
                    
                    List<Document> docList = Document.parse("{\"data\":" + content + "}")
                                                     .getList("data", Document.class);
                    collection.insertMany(docList);
                    System.out.println(docList.size() + " Documents inserted from array...");
                } else {
                    Document abc = Document.parse(content);
                    collection.insertOne(abc);
                    System.out.println("Single document inserted...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            long count = collection.countDocuments();
            System.out.println("Collection '" + collection.getNamespace().getCollectionName() + 
                               "' has: " + count + " documents.");
        
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
