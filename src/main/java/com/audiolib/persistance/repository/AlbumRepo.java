package com.audiolib.persistance.repository;


import com.audiolib.persistance.model.Album;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepo extends PagingAndSortingRepository<Album, Long> {

}
