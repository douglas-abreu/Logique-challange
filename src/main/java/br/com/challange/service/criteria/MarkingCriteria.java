package br.com.challange.service.criteria;

import br.com.challange.models.Marking;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


@Data
@NoArgsConstructor
public class MarkingCriteria {

    private Integer userId;
    private String markingDate;


    public static Specification<Marking> filterByUser(Integer userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Marking> filterByMarkingDate(String markingDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("markingDate"), markingDate);
    }

}
