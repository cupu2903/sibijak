package net.tnt.analysis;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.Context;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.SearchClientBuilder;
import com.azure.search.documents.models.QueryType;
import com.azure.search.documents.models.SearchOptions;
import com.azure.search.documents.models.SearchResult;
import com.azure.search.documents.util.SearchPagedIterable;
import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import net.tnt.entity.ProgramRelatedDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RegisterForReflection
@ApplicationScoped
public class PriceAnalysis {

    private static final String SEARCH_SERVICE_NAME = "YOUR-SERVICE-NAME";
    private static final String INDEX_NAME = "YOUR-INDEX";
    private static final String SEARCH_API_KEY = "SEARCH_API_KEY";
    private static final String OPENAI_API_KEY = "OPENAI_API_KEY";
    private static final String OPENAI_RESOURCE_NAME = "OPENAI_RESOURCE_NAME-openAI";
    private static final String DEPLOYMENT_NAME = "DEPLOYMENT_NAME";
    private static SearchClient searchClient = new SearchClientBuilder()
            .endpoint("https://" + SEARCH_SERVICE_NAME + ".search.windows.net")
            .credential(new AzureKeyCredential(SEARCH_API_KEY))
            .indexName(INDEX_NAME)
            .buildClient();

    private static OpenAIClient openAIClient = new OpenAIClientBuilder()
            .endpoint("https://" + OPENAI_RESOURCE_NAME + ".openai.azure.com")
            .credential(new AzureKeyCredential(OPENAI_API_KEY))
            .buildClient();


    public String analyzePrice(String searchText, String docToAnalyze) {

        List<ChatRequestMessage> chatMessages = new ArrayList<>();
        SearchOptions options = new SearchOptions()
                .setTop(2)
                .setIncludeTotalCount(true)
                .setQueryType(QueryType.SIMPLE);

        Log.info("search text: " + searchText);
        SearchPagedIterable searchResults = searchClient.search(searchText, options, Context.NONE);
        StringBuilder retrievedData = new StringBuilder();
        searchResults.forEach(doc -> {
            ProgramRelatedDocument document = doc.getDocument(ProgramRelatedDocument.class);
            Log.info("document result: "+document.toString());
            String content = document.getContent();
            retrievedData.append(content).append("\n");
        });
        Log.info("search result: " + searchResults.getTotalCount());
        if (!retrievedData.isEmpty()) {
            String promptSystem = String.format("Gunakan data ini sebagai referensi dan pembanding harga untuk item yang sama, gunakan harga termahal: %s", retrievedData);
            chatMessages.add(new ChatRequestSystemMessage(promptSystem));
        }
        String promptUser = String.format("%s analisa document ini, berapa harga pagu dan hps nya. " +
                        "kemudian mengacu pada uraian item yg ada, apakah harga tersebut memenuhi kelayakan sesuai dengan standard pasar. " +
                        "lakukan analisa menyeluruh untuk keseluruhan item dan berikan harga wajar dalam rentang minimum dan maximum " +
                        "kemudian bandingkan dengan pagu dan hps. buat dalam format:\n" +
                        "1. Harga pagu dan hps\n" +
                        "2. Uraian Item dan Spesifikasi\n" +
                        "3. Jumlah dan VOlume\n" +
                        "4. Harga Pasar per Item\n" +
                        "5. Kalkulasi Harga Pasar\n" +
                        "6. Perbandingan Harga Wajar dengan Pagu dan HPS\n" +
                        "7. Kesimpulan atau Scoring" +
                        "untuk response, gunakan format markdown",
                docToAnalyze);

        chatMessages.add(new ChatRequestUserMessage(promptUser));


        ChatCompletions chatCompletions = openAIClient.getChatCompletions(DEPLOYMENT_NAME, new ChatCompletionsOptions(chatMessages));

        System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatResponseMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
        }
        return chatCompletions.getChoices().get(0).getMessage().getContent();
    }
}
