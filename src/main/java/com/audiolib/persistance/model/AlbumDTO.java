package com.audiolib.persistance.model;

import java.util.Objects;

/**
 * On passe par un data object pour eviter les probleme de reference sur les relations avec l'arstist
 */
public class AlbumDTO {
    private Long id;
    private String title;
    private Long artistId;

    public AlbumDTO() {
    }

    public AlbumDTO(Long id, String title, Long artistId) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public AlbumDTO id(Long id) {
        this.id = id;
        return this;
    }

    public AlbumDTO title(String title) {
        this.title = title;
        return this;
    }

    public AlbumDTO artistId(Long artistId) {
        this.artistId = artistId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AlbumDTO)) {
            return false;
        }
        AlbumDTO albumDTO = (AlbumDTO) o;
        return Objects.equals(id, albumDTO.id) && Objects.equals(title, albumDTO.title) && Objects.equals(artistId, albumDTO.artistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artistId);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", artistId='" + getArtistId() + "'" +
            "}";
    }

}
