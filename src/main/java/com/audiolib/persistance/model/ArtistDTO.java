package com.audiolib.persistance.model;

import java.util.Objects;

public class ArtistDTO {
    private Long id;
    private String name;

    public ArtistDTO() {
    }

    public ArtistDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public ArtistDTO id(Long id) {
        this.id = id;
        return this;
    }

    public ArtistDTO name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ArtistDTO)) {
            return false;
        }
        ArtistDTO artistDTO = (ArtistDTO) o;
        return Objects.equals(id, artistDTO.id) && Objects.equals(name, artistDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + "}";
    }
}
