package br.com.challange.service.criteria;

import br.com.challange.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@Data
@NoArgsConstructor
public class UserCriteria {

    private String keyword;

    public static Specification<User> searchByUsername(String keyword){
        return (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("username")), "%" + keyword.toLowerCase() + "%");
    }

}
