package at.ac.ase.repository.auction.implementation;

import at.ac.ase.entities.*;
import at.ac.ase.repository.auction.AuctionPostQuery;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuctionRepositoryCustomQueriesImpl implements AuctionRepositoryCustomQueries{

    @PersistenceContext
    EntityManager entityManager;

    public List<AuctionPost> query(AuctionPostQuery auctionQuery) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuctionPost> cquery = cb.createQuery(AuctionPost.class);

        Root<AuctionPost> root = cquery.from(AuctionPost.class);
        Join<AuctionPost, User> joinUser = root.join("creator", JoinType.INNER);
        List<Predicate> predicatesAnd = new ArrayList<>();

        /*
         * time constrains
         */

        if (auctionQuery.getAuctionsStartFrom() != null) {
            predicatesAnd.add(cb.greaterThan(root.get("startTime"), auctionQuery.getAuctionsStartFrom()));
        }

        if (auctionQuery.getAuctionsStartUntil() != null) {
            predicatesAnd.add(cb.lessThanOrEqualTo(root.get("startTime"), auctionQuery.getAuctionsStartUntil()));
        }

        if (auctionQuery.getAuctionsEndFrom() != null) {
            predicatesAnd.add(cb.greaterThan(root.get("endTime"), auctionQuery.getAuctionsEndFrom()));
        }

        if (auctionQuery.getAuctionsEndUntil() != null) {
            predicatesAnd.add(cb.lessThanOrEqualTo(root.get("endTime"), auctionQuery.getAuctionsEndFrom()));
        }

        /*
         * categories
         */

        if (!auctionQuery.getCategories().isEmpty()) {

            List<Predicate> predicatesOr = new ArrayList<>();

            for (Category category : auctionQuery.getCategories()) {
                predicatesOr.add(cb.equal(root.get("category"), category));
            }
            predicatesAnd.add(cb.or(predicatesOr.toArray(new Predicate[predicatesOr.size()])));
        }

        /*
         * countries
         */

        if (!auctionQuery.getCountries().isEmpty()) {

            List<Predicate> predicatesOr = new ArrayList<>();

            for (String country : auctionQuery.getCountries()) {
                predicatesOr.add(cb.equal(root.get("address").get("country"), country));
            }
            predicatesAnd.add(cb.or(predicatesOr.toArray(new Predicate[predicatesOr.size()])));
        }



        /*
         * status values
         */

        if (!auctionQuery.getStatus().isEmpty()) {

            List<Predicate> predicatesOr = new ArrayList<>();

            for (String status : auctionQuery.getStatus()) {
                predicatesOr.add(cb.equal(root.get("status"), Status.valueOf(status)));
            }
            predicatesAnd.add(cb.or(predicatesOr.toArray(new Predicate[predicatesOr.size()])));
        }



        /*
         * search key filtering for creator name, auction name and description
         */

        if (!auctionQuery.getSearchKeys().isEmpty()) {

            List<Predicate> predicatesOr = new ArrayList<>();

            for (String searchString : auctionQuery.getSearchKeys()) {

                String searchLike = "%" + searchString.toUpperCase() + "%";

                predicatesOr.add(cb.like(cb.upper(root.get("description")), searchLike));
                predicatesOr.add(cb.like(cb.upper(root.get("name")), searchLike));

                predicatesOr.add(cb.like(cb.upper(cb.treat(joinUser, AuctionHouse.class).get("name")), searchLike));
                predicatesOr.add(cb.like(cb.upper(cb.treat(joinUser, RegularUser.class).get("firstName")), searchLike));
                predicatesOr.add(cb.like(cb.upper(cb.treat(joinUser, RegularUser.class).get("lastName")), searchLike));
            }
            predicatesAnd.add(cb.or(predicatesOr.toArray(new Predicate[predicatesOr.size()])));
        }



        /*
         * sorting
         */

        if (validSortProperties(auctionQuery)) {
            if ("DESC".equals(auctionQuery.getSortOrder())) {
                cquery.orderBy(cb.desc(root.get(auctionQuery.getSortBy())));
            }
            else  {
                cquery.orderBy(cb.asc(root.get(auctionQuery.getSortBy())));
            }
        }


        cquery.select(root).distinct(true);
        cquery.where(cb.and(predicatesAnd.toArray(new Predicate[predicatesAnd.size()])));

        TypedQuery<AuctionPost> finalQuery = entityManager.createQuery(cquery);

        /*
         * paging
         */

        Integer pageSize = auctionQuery.getPageSize();
        Integer pageNumber = auctionQuery.getPageNumber();

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = 50;
        }

        finalQuery.setFirstResult((pageNumber) * pageSize);
        finalQuery.setMaxResults(pageSize);

        return finalQuery.getResultList();
    }

    private boolean validSortProperties(AuctionPostQuery filter) {
        return filter.getSortBy() != null && filter.getSortOrder() != null &&
                (filter.getSortOrder().equals("DESC") || filter.getSortOrder().equals("ASC"));
    }
}
