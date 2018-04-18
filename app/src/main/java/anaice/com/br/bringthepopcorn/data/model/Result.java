package anaice.com.br.bringthepopcorn.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Representa um filme no retorno da chamada para a API do MovieDB.
 */
public class Result {

    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SuppressWarnings("unused")
    public Integer getVoteCount() {
        return voteCount;
    }

    @SuppressWarnings("unused")
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(Integer id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public Boolean getVideo() {
        return video;
    }

    @SuppressWarnings("unused")
    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    @SuppressWarnings("unused")
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }

    @SuppressWarnings("unused")
    public Double getPopularity() {
        return popularity;
    }

    @SuppressWarnings("unused")
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @SuppressWarnings("unused")
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @SuppressWarnings("unused")
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    @SuppressWarnings("unused")
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    @SuppressWarnings("unused")
    public String getOriginalTitle() {
        return originalTitle;
    }

    @SuppressWarnings("unused")
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @SuppressWarnings("unused")
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    @SuppressWarnings("unused")
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    @SuppressWarnings("unused")
    public String getBackdropPath() {
        return backdropPath;
    }

    @SuppressWarnings("unused")
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @SuppressWarnings("unused")
    public Boolean getAdult() {
        return adult;
    }

    @SuppressWarnings("unused")
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    @SuppressWarnings("unused")
    public String getOverview() {
        return overview;
    }

    @SuppressWarnings("unused")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @SuppressWarnings("unused")
    public String getReleaseDate() {
        return releaseDate;
    }

    @SuppressWarnings("unused")
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
