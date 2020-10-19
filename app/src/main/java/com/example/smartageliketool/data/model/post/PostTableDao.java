package com.example.smartageliketool.data.model.post;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostTableDao {

    @Query("Select * from post_table")
    List<PostDataBaseEntity> getAllPostsFromDataBase();

    @Query("Select DISTINCT mediaId from post_table")
    List<String> getDistinctPost();


    @Query("SELECT COUNT(*) FROM post_table")
    int getPostTableSize();


    @Query("DELETE from post_table WHERE id = :postId")
    void deletePostById(int postId);

    @Query("Select * from post_table WHERE mediaId = :mediaId")
    List<PostDataBaseEntity> getPostByMediaId(String mediaId);

    @Query("Select * from post_table WHERE id = :id")
    List<PostDataBaseEntity> getPostById(int id);


    @Insert
    void insertPost(PostDataBaseEntity post);

    @Update
    void updatePost(PostDataBaseEntity post);

    @Delete
    void deletePost(PostDataBaseEntity post);
}
