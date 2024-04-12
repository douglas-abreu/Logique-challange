package br.com.challange.service;

import br.com.challange.DTO.ReportDTO;
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
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.com.challange.util.Constants.MARCACAO;

@Service
@RequiredArgsConstructor
public class MarkingService {

    private final MarkingRepository markingRepository;
    private final UserService userService;

    public ApiResponse<Marking> markingRegistration(MarkingCriteria criteria){
        User userLogged = userService.getUserLogged().getData();
        List<Marking> markingList = getMarkingsByDate(criteria, userLogged);

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

    public ApiResponse<ReportDTO> dayReport(MarkingCriteria criteria){
        ApiResponse<ReportDTO> response = new ApiResponse<>();
        User userLogged = userService.getUserLogged().getData();
        List<Marking> markingList = getMarkingsByDate(criteria, userLogged);
        ReportDTO reportDTO = new ReportDTO();
        List<String> totalHours = new ArrayList<>();
        if(markingList.isEmpty())
            return response.of(HttpStatus.BAD_REQUEST, "Nenhuma marcação encontrada para o dia");

        markingList.forEach(marking -> {
            if(!Validation.isEmptyOrNull(marking.getClosingMark()) &&
                !Validation.isEmptyOrNull(marking.getOpeningMark())){
                String hoursWorked = timeSubtract(marking.getOpeningMark(), marking.getClosingMark());
                totalHours.add(hoursWorked);
            }
        });
        reportDTO.totalHours = sumHours(totalHours);
        reportDTO.day = FormatUtil.getOnlyDate();
        reportDTO.missingHours = "--:--:--";
        reportDTO.exceededHours = "--:--:--";
        int compare = parseStrDuration(userLogged.getWorkload()).compareTo(parseStrDuration(reportDTO.totalHours));

        if(compare < 0)
            reportDTO.exceededHours = subHours(reportDTO.totalHours, userLogged.getWorkload());
        else
            reportDTO.missingHours = subHours(userLogged.getWorkload(), reportDTO.totalHours);

        return response.of(HttpStatus.OK, MsgSystem.sucGet("Relatório"), reportDTO);

    }

    public String sumHours(List<String> strDurationArr){
        Duration sum = Duration.ZERO;
        for (String strDuration : strDurationArr) {
            sum = sum.plus(parseStrDuration(strDuration));
        }

        return formatDuration(sum);
    }

    public String subHours(String workload, String totalHours){
        Duration sub = parseStrDuration(workload);
        sub = sub.minus(parseStrDuration(totalHours));

        return formatDuration(sub);
    }



    public Duration parseStrDuration(String strDuration) {
        String[] arr = strDuration.split(":");
        String strIsoDuration = "PT" + arr[0] + "H" + arr[1] + "M" + arr[2] + "S";
        return Duration.parse(strIsoDuration);
    }

    public String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    public String timeSubtract(Date openingDate, Date closingDate){

        long differenceInMilliSeconds = Math.abs(closingDate.getTime() - openingDate.getTime());
        long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;
        long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
        long differenceInSeconds = (differenceInMilliSeconds / 1000) % 60;

        return String.format("%02d:%02d:%02d",differenceInHours, differenceInMinutes, differenceInSeconds);
    }

    public List<Marking> getMarkingsByDate(MarkingCriteria criteria, User userLogged){
        criteria.setUserId(userLogged.getId());
        criteria.setMarkingDate(FormatUtil.getOnlyDate());
        return markingRepository.findAll(
                createSpecification(criteria),
                Sort.by(Sort.Direction.DESC, "id"));
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
