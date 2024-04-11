package br.com.challange.service;

import br.com.challange.models.Marking;
import br.com.challange.models.User;
import br.com.challange.repository.MarkingRepository;
import br.com.challange.response.ApiResponse;
import br.com.challange.service.criteria.MarkingCriteria;
import br.com.challange.util.FormatUtil;
import br.com.challange.util.MsgSystem;
import br.com.challange.util.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

import static br.com.challange.util.Constants.MARCACAO;

@Service
@RequiredArgsConstructor
public class MarkingService {

    private final MarkingRepository markingRepository;
    private final UserService userService;

    public ApiResponse<Marking> markingRegistration(MarkingCriteria criteria){
        User userLogged = userService.getUserLogged().getData();
        criteria.setUserId(userLogged.getId());
        criteria.setMarkingDate(FormatUtil.getOnlyDate());
        List<Marking> markingList = markingRepository.findAll(
                createSpecification(criteria),
                Sort.by(Sort.Direction.DESC, "id"));

        if(userLogged.getWorkload().equals("08:00:00"))
            return markingWorkLoadEight(markingList, userLogged);

        return markingWorkLoadSix(markingList, userLogged);
    }

    public Marking createMarking(User user){
        Marking marking = new Marking();
        marking.setUser(user);
        marking.setOpeningMark(FormatUtil.getDateFromLocalDateTime());
        marking.setMarkingDate(FormatUtil.getOnlyDate());
        markingRepository.save(marking);
        return marking;
    }

    public Marking updateMarking(Marking marking){
        marking.setClosingMark(FormatUtil.getDateFromLocalDateTime());
        markingRepository.save(marking);
        return marking;
    }

    public ApiResponse<Marking> markingWorkLoadEight(List<Marking> markingList, User userLogged){
        ApiResponse<Marking> response = new ApiResponse<>();
        if(markingList.isEmpty() || !Validation.isEmptyOrNull(markingList.get(0).getClosingMark()))
            return response.of(HttpStatus.OK, MsgSystem.sucCreate(MARCACAO), createMarking(userLogged));

        return response.of(HttpStatus.OK, MsgSystem.sucUpdate(MARCACAO), updateMarking(markingList.get(0))) ;
    }

    public ApiResponse<Marking> markingWorkLoadSix(List<Marking> markingList, User userLogged){
        ApiResponse<Marking> response = new ApiResponse<>();
        if(markingList.isEmpty())
            return response.of(HttpStatus.OK, MsgSystem.sucCreate(MARCACAO), createMarking(userLogged));
        if(Validation.isEmptyOrNull(markingList.get(0).getClosingMark()))
            return response.of(HttpStatus.OK, MsgSystem.sucUpdate(MARCACAO), updateMarking(markingList.get(0)));

        return response.of(HttpStatus.BAD_REQUEST, "Usuário já encerrou o ponto de hoje");
    }

    private Specification<Marking> createSpecification(MarkingCriteria criteria){
        Specification<Marking> specification = Specification.where(null);

        if (criteria.getUserId() != null)
            specification = specification.and(MarkingCriteria.filterByUser(criteria.getUserId()));

        if (criteria.getMarkingDate() != null)
            specification = specification.and(MarkingCriteria.filterByMarkingDate(criteria.getMarkingDate()));

        return specification;
    }




}
