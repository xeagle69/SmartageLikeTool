package com.example.smartageliketool.data.model.cookie;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smartageliketool.data.model.post.PostDataBaseEntity;

import java.util.List;

@Dao
public interface CookieTableDao {

    @Query("Select * from post_table")
    List<PostDataBaseEntity> getAllPostsFromDataBase();

    @Query("Select * from post_table WHERE is_liked = :isLike AND is_liked_with_cookie =:isLikeByHeader")
    List<PostDataBaseEntity> getAllPostsFromDataBaseThatIsUnlikeByTwoFactors(boolean isLike, boolean isLikeByHeader);


    @Query("SELECT COUNT(*) FROM post_table")
    int getPostTableSize();


    @Query("UPDATE post_table SET is_liked=:isLike WHERE actual_id = :actualId")
    void setLikeStatusForPost(boolean isLike, int actualId);


    @Query("UPDATE post_table SET is_liked_with_cookie=:isLikeWithCookie WHERE actual_id = :actualId")
    void setLikeStatusForPostLikedViaCookie(boolean isLikeWithCookie, int actualId);

    @Query("DELETE from post_table WHERE actual_id = :actualId")
    void deletePostByActualId(int actualId);


    @Insert
    void insertPost(PostDataBaseEntity post);

    @Update
    void updatePost(PostDataBaseEntity post);

    @Delete
    void deletePost(PostDataBaseEntity post);
}
