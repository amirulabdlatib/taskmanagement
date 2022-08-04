package com.example.taskmanagement.remote;

import com.example.taskmanagement.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @FormUrlEncoded
    @POST("api/users/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);


    @GET("api/users/{id}")
    Call<User> getUser(@Header("api-key") String api_key, @Path("id") int id);

}
