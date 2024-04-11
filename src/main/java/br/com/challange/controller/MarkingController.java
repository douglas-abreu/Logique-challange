package br.com.challange.controller;

import br.com.challange.models.Marking;
import br.com.challange.models.User;
import br.com.challange.response.ApiResponse;
import br.com.challange.service.MarkingService;
import br.com.challange.service.UserService;
import br.com.challange.service.criteria.MarkingCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("marking")
public class MarkingController {

    private final UserService userService;
    private final MarkingService markingService;


    @PostMapping()
    public ResponseEntity<ApiResponse<Marking>> addMarking(MarkingCriteria criteria) {
        ApiResponse<Marking> response = markingService.markingRegistration(criteria);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

//    @GetMapping("/")
//    public ResponseEntity<ApiResponse<Marking>> addMarking(MarkingCriteria criteria) {
//        ApiResponse<Marking> response = markingService.markingRegistration(criteria);
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }
}
