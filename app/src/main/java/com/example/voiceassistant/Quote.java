package com.example.voiceassistant;

import java.util.function.Consumer;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Quote {
    public static class Note {
        @SerializedName("text")
        public String text;
    }

    public static class Blockquote {
        @SerializedName("quoteText")
        public Note quoteText;

        @SerializedName("quoteAuthor")
        public Note quoteAuthor;
    }

    public static class ApiResult {
        @SerializedName("current")
        public Blockquote current;
    }

    public interface RandomQuote {
        @GET("/api/1.0/?method=getQuote&format=json&lang=ru")
        Call<ApiResult> getResult();
    }

    public static void get(final Consumer<String> callback) {
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://api.forismatic.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<ApiResult> call = retrofit
                .create(RandomQuote.class)
                .getResult();

        call.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult apiResult = response.body();
                String result = apiResult.current.quoteText.text + " " +
                        apiResult.current.quoteAuthor.text;
                callback.accept(result);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {

            }
        });
    }
}

