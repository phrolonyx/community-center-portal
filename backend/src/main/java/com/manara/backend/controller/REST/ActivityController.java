package com.manara.backend.controller.REST;

import com.manara.backend.dto.ActivityRequest;
import com.manara.backend.dto.ActivityResponseDto;
import com.manara.backend.model.Activity;
import com.manara.backend.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<ActivityResponseDto> getActivities(){
        return activityService.findAll().stream().map(ActivityResponseDto::from).toList();
    }

    @GetMapping("/{id}")
    public ActivityResponseDto getActivity(@PathVariable Integer id){
        if (activityService.findById(id) == null){
            return null;
        }
        return ActivityResponseDto.from(activityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ActivityResponseDto> addActivity(@RequestBody @Valid ActivityRequest activityRequest){
        Activity activity = activityService.save(activityRequest.toActivity(activityRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(ActivityResponseDto.from(activity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> updateActivity(@PathVariable Integer id, @RequestBody @Valid ActivityRequest activityRequest){
        Activity existingActivity = activityService.findById(id);

        if (existingActivity == null){
            return ResponseEntity.notFound().build();
        }

        existingActivity.setName(activityRequest.getName());
        existingActivity.setDescription(activityRequest.getDescription());
        activityService.save(existingActivity);

        return ResponseEntity.ok(ActivityResponseDto.from(existingActivity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> deleteActivity(@PathVariable Integer id){

        if (activityService.findById(id) == null){
            return ResponseEntity.notFound().build();
        }

        activityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
