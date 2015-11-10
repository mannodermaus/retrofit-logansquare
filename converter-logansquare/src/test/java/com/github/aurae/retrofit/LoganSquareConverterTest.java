package com.github.aurae.retrofit;

import com.github.aurae.retrofit.model.BasicModel;
import com.github.aurae.retrofit.model.CustomEnum;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Call<BasicModel> callListWrongType(@Body List<List<BasicModel>> body);

        @POST("/")
        Call<Map<String, BasicModel>> callMap(@Body Map<String, BasicModel> body);

        @POST("/")
        Call<Map<Integer, BasicModel>> callMapWrongKey(@Body Map<Integer, BasicModel> body);

        @POST("/")
        Call<Map<String, List<BasicModel>>> callMapWrongValue(@Body Map<String, List<BasicModel>> body);

        @POST("/")
        Call<BasicModel[]> callArray(@Body BasicModel[] body);
    }

    @Rule
    public final MockWebServer mockWebServer = new MockWebServer();

    private Service service;

    @Before
    public void setUp() {
        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(LoganSquareConverterFactory.create())
                .build()
                .create(Service.class);
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
    public void testListWrongType() throws IOException {
        mockWebServer.enqueue(new MockResponse().setBody("{}"));

        // Setup the mock object with an incompatible type argument
        List<List<BasicModel>> body = new ArrayList<>();

        // Setup the API call and fire it
        try {
            service.callListWrongType(body).execute();
            Assertions.failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignored) {
        }
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

        // Setup the API call and fire it
        try {
            service.callMapWrongKey(body).execute();
            Assertions.failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testMapWrongValueType() throws IOException {
        mockWebServer.enqueue(new MockResponse().setBody("{}"));

        // Setup the mock object with an incompatible type argument
        Map<String, List<BasicModel>> body = new HashMap<>();

        // Setup the API call and fire it
        try {
            service.callMapWrongValue(body).execute();
            Assertions.failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignored) {
        }
    }

    @Test
    public void testArray() throws IOException {
        mockWebServer.enqueue(new MockResponse().setBody("{}"));

        // Setup the mock object with an incompatible type argument
        BasicModel[] body = new BasicModel[0];

        // Setup the API call and fire it
        try {
            service.callArray(body).execute();
            Assertions.failBecauseExceptionWasNotThrown(RuntimeException.class);
        } catch (RuntimeException ignored) {
        }
    }
}
