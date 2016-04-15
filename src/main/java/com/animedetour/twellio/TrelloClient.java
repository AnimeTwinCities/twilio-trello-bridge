package com.animedetour.twellio;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit client for Trello operations.
 */
public interface TrelloClient {

    @POST("lists/{listId}/cards")
    Call<TrelloCardCreationResponse> addCard(
            @Path("listId") String listId,
            @Query("key") String trelloKey,
            @Query("token") String trelloToken,
            @Query("name") String name,
            @Query("due") String due
    );

    @POST("cards/{cardId}/actions/comments")
    Call<ResponseBody> addComment(
            @Path("cardId") String cardId,
            @Query("key") String trelloKey,
            @Query("token") String trelloToken,
            @Query("text") String text
    );

}
