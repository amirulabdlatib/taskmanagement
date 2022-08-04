package com.example.taskmanagement.remote;

import com.example.taskmanagement.model.DeleteResponse;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface TaskService {

    @GET("api/task")
    Call<List<Task>> getAllTasks(@Header("api-key") String api_key);



    /**
     * Add book by sending a single task JSON
     * @return book object
     */
    @POST("api/task")
    Call<Task> addTask(@Header ("api-key") String apiKey, @Body Task task);


    @GET("api/task/{taskID}")
    Call<Task> getTask(@Header("api-key")String api_key,@Path("taskID") int taskID );

    @POST("api/task/delete/{taskID}")
    Call<DeleteResponse> deleteTask(@Header ("api-key") String apiKey, @Path("taskID") int taskID);

    /** * Update Task by sending a single Book JSON * @return task object */
    @POST("api/task/update")
    Call<Task> updateTask(@Header ("api-key") String apiKey, @Body Task task);
}
