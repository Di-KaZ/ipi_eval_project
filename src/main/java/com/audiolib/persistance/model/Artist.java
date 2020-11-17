package com.audiolib.persistance.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ArtistId")
    private Long id;

    @Column(name = "Name")
    private String name;

    @OneToMany(mappedBy = "artist", fetch=FetchType.EAGER)
	private Set<Album> albums;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Album> getAlbums() {
        return this.albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }
}
