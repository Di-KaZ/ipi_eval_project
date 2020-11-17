package com.audiolib.persistance.service;

import com.audiolib.persistance.repository.AlbumRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    @Autowired
    AlbumRepo albumRepo;
}
