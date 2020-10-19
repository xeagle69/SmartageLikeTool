package com.example.smartageliketool.data.model.likeTable;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LikeTableDao {

//    @Query("Select * from post_table")
//    List<PostDataBaseEntity> getAllPostsFromDataBase();
//
//    @Query("Select * from post_table WHERE is_liked = :isLike AND is_liked_with_cookie =:isLikeByHeader")
//    List<PostDataBaseEntity> getAllPostsFromDataBaseThatIsUnlikeByTwoFactors(boolean isLike, boolean isLikeByHeader);
//
//
//    @Query("SELECT COUNT(*) FROM post_table")
//    int getPostTableSize();
//
//
//    @Query("UPDATE post_table SET is_liked=:isLike WHERE actual_id = :actualId")
//    void setLikeStatusForPost(boolean isLike, int actualId);
//
//
//    @Query("UPDATE post_table SET is_liked_with_cookie=:isLikeWithCookie WHERE actual_id = :actualId")
//    void setLikeStatusForPostLikedViaCookie(boolean isLikeWithCookie, int actualId);
//
//    @Query("DELETE from post_table WHERE actual_id = :actualId")
//    void deletePostByActualId(int actualId);
//
//
//    @Insert
//    void insertPost(PostDataBaseEntity post);
//
//    @Update
//    void updatePost(PostDataBaseEntity post);
//
//    @Delete
//    void deletePost(PostDataBaseEntity post);

    @Query("Select * from like_table WHERE cookie_id = :cookiId")
    List<LikeTable> getLikesByCookie(int cookiId);


    @Insert
    void insertLikeTableEntity(LikeTable likeTable);

    @Query("Delete from like_table where cookie_id = :id")
    void deleteCookieId(Integer id);

    @Query("Select COUNT(*) from like_table WHERE cookie_id= :id")
    int getCountByCookie(Integer id);
}
