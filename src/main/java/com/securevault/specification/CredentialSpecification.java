package com.securevault.specification;

import com.securevault.entity.Category;
import com.securevault.entity.Credential;
import org.springframework.data.jpa.domain.Specification;

public class CredentialSpecification {

    public static Specification<Credential> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                );
    }

    public static Specification<Credential> hasUsername(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        "%" + username.toLowerCase() + "%"
                );
    }

    public static Specification<Credential> hasWebsite(String website) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("website")),
                        "%" + website.toLowerCase() + "%"
                );
    }

    public static Specification<Credential> hasCategory(Category category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("category"),
                        category
                );
    }
}