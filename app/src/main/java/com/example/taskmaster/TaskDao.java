package com.example.taskmaster;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task ORDER BY id DESC")
    List<Task> getAll();


    @Query("SELECT * FROM task WHERE id = :id")
    Task getOne(long id);

    @Insert
    void save(Task task);


    @Delete
    void delete(Task task);
}





