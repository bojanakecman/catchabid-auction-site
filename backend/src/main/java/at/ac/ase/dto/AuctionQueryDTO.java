package at.ac.ase.dto;

import at.ac.ase.entities.Category;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionQueryDTO {

    List<String> searchKeys = new ArrayList<>();
    List<Category> categories = new ArrayList<>();
    List<String> countries = new ArrayList<>();
    List<String> status = new ArrayList<>();
    LocalDateTime auctionsStartFrom;
    LocalDateTime auctionsStartUntil;
    LocalDateTime auctionsEndFrom;
    LocalDateTime auctionsEndUntil;
    Integer pageNumber;
    Integer pageSize;
    String sortBy;
    String sortOrder;
    String userEmail;
    boolean useUserPreferences;

    public List<String> getSearchKeys() {
        return searchKeys;
    }

    public void addSearchKey(String searchString) {
        searchKeys.add(searchString);
    }

    public void setSearchKeys(List<String> searchKeys) {
        this.searchKeys = searchKeys;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public void addCountries(String country) {
        this.countries.add(country);
    }

    public LocalDateTime getAuctionsStartFrom() {
        return auctionsStartFrom;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setAuctionsStartFrom(LocalDateTime auctionsStartFrom) {
        this.auctionsStartFrom = auctionsStartFrom;
    }

    public LocalDateTime getAuctionsStartUntil() {
        return auctionsStartUntil;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setAuctionsStartUntil(LocalDateTime auctionsStartUntil) {
        this.auctionsStartUntil = auctionsStartUntil;
    }

    public LocalDateTime getAuctionsEndFrom() {
        return auctionsEndFrom;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setAuctionsEndFrom(LocalDateTime auctionsEndFrom) {
        this.auctionsEndFrom = auctionsEndFrom;
    }

    public LocalDateTime getAuctionsEndUntil() {
        return auctionsEndUntil;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public void setAuctionsEndUntil(LocalDateTime auctionsEndUntil) {
        this.auctionsEndUntil = auctionsEndUntil;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategories(Category category) {
        this.categories.add(category);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isUseUserPreferences() {
        return useUserPreferences;
    }

    public void setUseUserPreferences(boolean useUserPreferences) {
        this.useUserPreferences = useUserPreferences;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }
}
