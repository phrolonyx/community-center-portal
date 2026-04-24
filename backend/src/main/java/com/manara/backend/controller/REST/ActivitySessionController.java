package com.manara.backend.controller.REST;

import com.manara.backend.dto.ActivitySessionRequest;
import com.manara.backend.dto.ActivitySessionResponseDto;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.service.ActivitySessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class ActivitySessionController {

    @Autowired
    private ActivitySessionService activitySessionService;

    @GetMapping
    public List<ActivitySessionResponseDto> getSessions(){
        return activitySessionService.findAll().stream().map(ActivitySessionResponseDto::from).toList();
    }

    @GetMapping("/{id}")
    public ActivitySessionResponseDto getSession(@PathVariable Integer id){
        ActivitySession activitySession = activitySessionService.findById(id);
        return ActivitySessionResponseDto.from(activitySession);
    }

    @PostMapping
    public ResponseEntity<ActivitySessionResponseDto> addSession(@RequestBody @Valid ActivitySessionRequest activitySessionRequest){
        ActivitySession activitySession = activitySessionService.createSession(activitySessionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ActivitySessionResponseDto.from(activitySession));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivitySessionResponseDto> updateSession(@PathVariable Integer id, @RequestBody @Valid ActivitySessionRequest activitySessionRequest){
        ActivitySession activitySession = activitySessionService.updateSession(id, activitySessionRequest);
        return ResponseEntity.ok(ActivitySessionResponseDto.from(activitySession));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActivitySessionResponseDto> deleteSession(@PathVariable Integer id){
        activitySessionService.findById(id);
        activitySessionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
