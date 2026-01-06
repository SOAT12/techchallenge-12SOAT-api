package com.fiap.soat12.tc_group_7.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SecretsManager {

    private static SecretsManager instance;

    private static SecretsManagerClient secretsManagerClient;

    private Map<String, JsonNode> secretList = new HashMap<String, JsonNode>();

    private String secretName;


    private SecretsManager() {
        /*
         *
         */
    }


    public static SecretsManager getInstance() {

        if (instance == null) {
            instance = new SecretsManager();
        }

        return instance;

    }

    public String get(String key) throws SecretsManagerException {

        return secretList.get(secretName).get(key).textValue();

    }

    public SecretsManager builder( String accessKeyId, String secretAccessKey, String region, String secretName ) throws Exception {

        this.secretName = secretName;

        if ( accessKeyId == null || accessKeyId.isEmpty() || secretAccessKey == null || secretAccessKey.isEmpty() ) {

            secretsManagerClient = SecretsManagerClient.builder()
                    .region( Region.of( region ) )
                    .build();

        } else {

            AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

            secretsManagerClient = SecretsManagerClient.builder()
                    .region( Region.of( region ) )
                    .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                    .build();

        }

        initialize( secretName );

        return this;
    }

    private void initialize( String secretName ){

        String secret;

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();

        GetSecretValueResponse getSecretValueResponse = null;

        try {
            getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException e) {
            // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (InternalServiceErrorException e) {
            // An error occurred on the server side.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (InvalidParameterException e) {
            // You provided an invalid value for a parameter.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (InvalidRequestException e) {
            // You provided a parameter value that is not valid for the current state of the resource.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (ResourceNotFoundException e) {
            // We can't find the resource that you asked for.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        }

        // Decrypts secret using the associated KMS key.
        // Depending on whether the secret is a string or binary, one of these fields will be populated.
        if (getSecretValueResponse.secretString() != null) {
            secret = getSecretValueResponse.secretString();
        }
        else {
            secret = new String(Base64.getDecoder().decode(getSecretValueResponse.secretBinary().asByteBuffer()).array());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode secretsJson = null;
        try {
            secretsJson = objectMapper.readTree(secret);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        secretList.put(secretName, secretsJson);
    }
}