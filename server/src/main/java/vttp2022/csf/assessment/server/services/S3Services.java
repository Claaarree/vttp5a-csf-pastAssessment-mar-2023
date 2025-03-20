package vttp2022.csf.assessment.server.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Services {
    
    @Autowired
    private AmazonS3 s3Client;
    
    public String upload(byte[] image) throws IOException {
        //User Data
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "drewkwan");

       //metadata
       ObjectMetadata metadata = new ObjectMetadata();
    //    metadata.setContentLength(file.getSize());
    //    metadata.setContentType(file.getContentType());
       metadata.setUserMetadata(userData);
       
       String key = UUID.randomUUID().toString().substring(0,8);
       System.out.println(key);

       //create put request
       PutObjectRequest putReq = new PutObjectRequest("drewkwan", "csfAssessment/%s".formatted(key), new ByteArrayInputStream(image), metadata);

       //create with public access
       putReq.withCannedAcl(CannedAccessControlList.PublicRead);
       s3Client.putObject(putReq);
       System.out.println(">>>>>s3 file upload instance created");
       return key;
    }
}
