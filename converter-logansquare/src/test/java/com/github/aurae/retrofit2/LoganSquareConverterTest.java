package com.github.aurae.retrofit2;

import com.github.aurae.retrofit2.model.BasicModel;
import com.github.aurae.retrofit2.model.CustomEnum;
import com.github.aurae.retrofit2.model.ForeignModel;
import com.github.aurae.retrofit2.model.GenericModel;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@linkplain LoganSquareConverterFactory}.
 */
public class LoganSquareConverterTest {

    interface Service {
        @POST("/")
        Call<BasicModel> callObject(@Body BasicModel body);

        @POST("/")
        Call<List<BasicModel>> callList(@Body List<BasicModel> body);

        @POST("/")
        Call<Map<String, BasicModel>> callMap(@Body Map<String, BasicModel> body);

        @POST("/")
        Call<Map<Integer, BasicModel>> callMapWrongKey(@Body Map<Integer, BasicModel> body);

        @POST("/")
        Call<BasicModel[]> callArray(@Body BasicModel[] body);

        @POST("/")
        Call<GenericModel<Integer>> callGenerics(@Body GenericModel<Integer> body);

        @POST("/")
        Call<BasicModel> callObjectRequestNotSupported(@Body ForeignModel body);

        @POST("/")
        Call<ForeignModel> callObjectResponseNotSupported(@Body BasicModel body);

        @POST("/")
        Call<BasicModel> callListRequestNotSupported(@Body List<ForeignModel> body);

        @POST("/")
        Call<List<ForeignModel>> callListResponseNotSupported(@Body BasicModel body);

        @POST("/")
        Call<BasicModel> callMapRequestNotSupported(@Body Map<String, ForeignModel> body);

        @POST("/")
        Call<Map<String, ForeignModel>> callMapResponseNotSupported(@Body BasicModel body);
    }

    @Rule
    public final MockWebServer mockWebServer = new MockWebServer();

    private Service service;

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger(MockWebServer.class.getName()).setLevel(Level.OFF);
    }

    @Before
    public void setUp() {
        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(LoganSquareConverterFactory.create())
                .build()
                .create(Service.class);
    }

    @Test
    public void testDoesntSupportObjectRequest() throws IOException, InterruptedException {
        ForeignModel requestBody = new ForeignModel();

        try {
            // Call the API and execute it
            service.callObjectRequestNotSupported(requestBody).execute();
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testDoesntSupportObjectResponse() throws IOException, InterruptedException {
        BasicModel requestBody = new BasicModel();

        try {
            // Call the API and execute it
            service.callObjectResponseNotSupported(requestBody).execute();
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testDoesntSupportListRequest() throws IOException, InterruptedException {
        List<ForeignModel> requestBody = new ArrayList<>();
        requestBody.add(new ForeignModel());

        try {
            // Call the API and execute it
            service.callListRequestNotSupported(requestBody).execute();
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testDoesntSupportListResponse() throws IOException, InterruptedException {
        BasicModel requestBody = new BasicModel();

        try {
            // Call the API and execute it
            service.callListResponseNotSupported(requestBody).execute();
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testDoesntSupportMapRequest() throws IOException, InterruptedException {
        Map<String, ForeignModel> requestBody = new HashMap<>();
        requestBody.put("obj", new ForeignModel());

        try {
            // Call the API and execute it
            service.callMapRequestNotSupported(requestBody).execute();
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testDoesntSupportMapResponse() throws IOException, InterruptedException {
        BasicModel requestBody = new BasicModel();

        try {
            // Call the API and execute it
            service.callMapResponseNotSupported(requestBody).execute();
            Assertions.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testObject() throws IOException, InterruptedException {
        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse().setBody("{\"customType\":2,\"name\":\"LOGAN SQUARE IS COOL\",\"list\":[\"value1\",\"value2\"]}"));

        // Setup the mock object
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        BasicModel requestBody = new BasicModel("LOGAN SQUARE IS COOL", "Not serialized", CustomEnum.VAL_2, values);

        // Call the API and execute it
        Call<BasicModel> call = service.callObject(requestBody);
        Response<BasicModel> response = call.execute();
        BasicModel responseBody = response.body();

        // Assert that conversions worked
        // 1) Standard field declaration
        assertThat(responseBody.getName()).isEqualTo("LOGAN SQUARE IS COOL");
        // 2) Named field declaration & List serialization
        assertThat(responseBody.getValues()).containsExactly("value1", "value2");
        // 3) Custom Type adapter serialization
        assertThat(responseBody.getCustomType()).isEqualTo(CustomEnum.VAL_2);
        // 4) Excluded field
        assertThat(responseBody.getNotSerialized()).isNull();

        // Check request body and the received header
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getBody().readUtf8()).isEqualTo("{\"customType\":2,\"name\":\"LOGAN SQUARE IS COOL\",\"list\":[\"value1\",\"value2\"]}");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/json; charset=UTF-8");
    }

    @Test
    public void testGenerics() throws IOException, InterruptedException {
        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse().setBody("{\"name\":\"Object\",\"value\":123}"));

        // Setup the mock object
        GenericModel<Integer> body = new GenericModel<>("Object", 123);

        // Call the API and execute it
        Call<GenericModel<Integer>> call = service.callGenerics(body);
        Response<GenericModel<Integer>> response = call.execute();
        GenericModel<Integer> responseBody = response.body();

        // Assert that conversions worked
        assertThat(responseBody.getName()).isEqualTo("Object");
        assertThat(responseBody.getValue()).isEqualTo(123);
    }

    @Test
    public void testList() throws IOException, InterruptedException {
        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse().setBody("[{\"customType\":2,\"name\":\"LOGAN SQUARE IS COOL\",\"list\":[\"value1\",\"value2\"]},{\"customType\":1,\"name\":\"LOGAN SQUARE IS COOL2\",\"list\":[\"value1\",\"value2\"]}]"));

        // Setup the mock object
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        List<BasicModel> requestBody = new ArrayList<>();
        requestBody.add(new BasicModel("LOGAN SQUARE IS COOL", "Not serialized", CustomEnum.VAL_2, values));
        requestBody.add(new BasicModel("LOGAN SQUARE IS COOL2", "Not serialized", CustomEnum.VAL_1, values));

        // Call the API and execute it
        Call<List<BasicModel>> call = service.callList(requestBody);
        Response<List<BasicModel>> response = call.execute();
        List<BasicModel> responseBody = response.body();

        // Assert that conversions worked
        // Number of objects
        assertThat(responseBody).hasSize(2);
        // Member values of first object
        BasicModel o1 = responseBody.get(0);
        assertThat(o1.getName()).isEqualTo("LOGAN SQUARE IS COOL");
        assertThat(o1.getValues()).containsExactly("value1", "value2");
        assertThat(o1.getCustomType()).isEqualTo(CustomEnum.VAL_2);
        assertThat(o1.getNotSerialized()).isNull();
        // Member values of second object
        BasicModel o2 = responseBody.get(1);
        assertThat(o2.getName()).isEqualTo("LOGAN SQUARE IS COOL2");
        assertThat(o2.getValues()).containsExactly("value1", "value2");
        assertThat(o2.getCustomType()).isEqualTo(CustomEnum.VAL_1);
        assertThat(o2.getNotSerialized()).isNull();

        // Check request body and the received header
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getBody().readUtf8()).isEqualTo("[{\"customType\":2,\"name\":\"LOGAN SQUARE IS COOL\",\"list\":[\"value1\",\"value2\"]},{\"customType\":1,\"name\":\"LOGAN SQUARE IS COOL2\",\"list\":[\"value1\",\"value2\"]}]");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/json; charset=UTF-8");
    }

    @Test
    public void testMap() throws IOException, InterruptedException {
        // Enqueue a mock response
        mockWebServer.enqueue(new MockResponse().setBody("{\"item1\":{\"customType\":2,\"name\":\"LOGAN SQUARE IS COOL\",\"list\":[\"value1\",\"value2\"]},\"item2\":{\"customType\":1,\"name\":\"LOGAN SQUARE IS COOL2\",\"list\":[\"value1\",\"value2\"]}}"));

        // Setup the mock object
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        Map<String, BasicModel> requestBody = new HashMap<>();
        requestBody.put("item1", new BasicModel("LOGAN SQUARE IS COOL", "Not serialized", CustomEnum.VAL_2, values));
        requestBody.put("item2", new BasicModel("LOGAN SQUARE IS COOL2", "Not serialized", CustomEnum.VAL_1, values));

        // Call the API and execute it
        Call<Map<String, BasicModel>> call = service.callMap(requestBody);
        Response<Map<String, BasicModel>> response = call.execute();
        Map<String, BasicModel> responseBody = response.body();

        // Assert that conversions worked
        // Number of objects
        assertThat(responseBody).hasSize(2);
        // Member values of first object
        BasicModel o1 = responseBody.get("item1");
        assertThat(o1.getName()).isEqualTo("LOGAN SQUARE IS COOL");
        assertThat(o1.getValues()).containsExactly("value1", "value2");
        assertThat(o1.getCustomType()).isEqualTo(CustomEnum.VAL_2);
        assertThat(o1.getNotSerialized()).isNull();
        // Member values of second object
        BasicModel o2 = responseBody.get("item2");
        assertThat(o2.getName()).isEqualTo("LOGAN SQUARE IS COOL2");
        assertThat(o2.getValues()).containsExactly("value1", "value2");
        assertThat(o2.getCustomType()).isEqualTo(CustomEnum.VAL_1);
        assertThat(o2.getNotSerialized()).isNull();

        // Check request body and the received header
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getBody().readUtf8()).isEqualTo("{\"item2\":{\"customType\":1,\"name\":\"LOGAN SQUARE IS COOL2\",\"list\":[\"value1\",\"value2\"]},\"item1\":{\"customType\":2,\"name\":\"LOGAN SQUARE IS COOL\",\"list\":[\"value1\",\"value2\"]}}");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/json; charset=UTF-8");
    }

    @Test
    public void testMapWrongKeyType() throws IOException {
        mockWebServer.enqueue(new MockResponse().setBody("{}"));

        // Setup the mock object with an incompatible type argument
        Map<Integer, BasicModel> body = new HashMap<>();
        body.put(1, new BasicModel("name", "not", CustomEnum.VAL_1, null));

        // Setup the API call and fire it
        try {
            service.callMapWrongKey(body).execute();
            Assertions.failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testArray() throws IOException {
        mockWebServer.enqueue(new MockResponse().setBody("{}"));

        // Setup the mock object with an incompatible type argument
        BasicModel[] body = new BasicModel[] { new BasicModel("name", "not", CustomEnum.VAL_2, null)};

        // Setup the API call and fire it
        try {
            service.callArray(body).execute();
            Assertions.failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignored) {
        }
    }
}
