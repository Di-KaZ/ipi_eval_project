package com.audiolib.persistance.repository;

import com.audiolib.persistance.model.Album;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepo extends CrudRepository<Album, Long> {
    @Modifying
    @Query("delete from Album a where a.id = :id")
    void deleteById(@Param("id") Long id);
}
