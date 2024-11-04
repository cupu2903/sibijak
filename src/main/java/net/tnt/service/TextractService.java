package net.tnt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ApplicationScoped
public class TextractService {
    private final TextractClient textractClient;

    @Inject
    FileUploadService fileUploadService;

    @Inject
    public TextractService() {
        try {
            Region region = Region.AP_SOUTHEAST_2;
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                    "YOUR_ACCESS_KEY",
                    "YOUR_SECRET_KEY");

            this.textractClient = TextractClient.builder()
                    .region(region)
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();
        } catch (Exception e) {
            Log.error(e);
            throw e;
        }
    }


    public String startDocAnalysisS3(String docName) {
        try {
            List<FeatureType> myList = new ArrayList<>();
            myList.add(FeatureType.TABLES);

            String BUCKET_NAME = "sibijak";
            S3Object s3Object = S3Object.builder()
                    .bucket(BUCKET_NAME)
                    .name(docName)
                    .build();

            DocumentLocation location = DocumentLocation.builder()
                    .s3Object(s3Object)
                    .build();
            StartDocumentAnalysisRequest documentAnalysisRequest = StartDocumentAnalysisRequest.builder()
                    .documentLocation(location)
                    .featureTypes(myList)
                    .build();

            StartDocumentAnalysisResponse response = textractClient.startDocumentAnalysis(documentAnalysisRequest);

            return response.jobId();

        } catch (TextractException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    public String getJobResults(String jobId) {
        boolean finished = false;
        int index = 0;
        String status = "IN_PROGRESS";
        String nextToken = null;
        List<Block> allBlocks = new ArrayList<>();
        String rawText = null;

        while (!finished) {
            GetDocumentAnalysisRequest analysisRequest = GetDocumentAnalysisRequest.builder()
                    .jobId(jobId)
                    .maxResults(1000)
                    .nextToken(nextToken)
                    .build();


            GetDocumentAnalysisResponse response = textractClient.getDocumentAnalysis(analysisRequest);
            status = response.jobStatus().toString();

            if (status.compareTo("SUCCEEDED") == 0) {
                System.out.println("block size: " + response.blocks().size());
                allBlocks.addAll(response.blocks());
                nextToken = response.nextToken();
                System.out.println(index + " next token is: " + nextToken);
                if (nextToken == null) {
                    finished = true;
                    System.out.println("All block size: " + allBlocks.size());
                    rawText = extractText(allBlocks);
                    System.out.println("rawText: " + rawText.length());
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return rawText;
    }

    private String extractText(List<Block> blocks) {
        StringBuilder rawTextBuilder = new StringBuilder();
        for (Block block : blocks) {
            if (block.blockType() == BlockType.LINE) {
                rawTextBuilder.append(block.text()).append("\n");
            }
        }
        return rawTextBuilder.toString();
    }

}
