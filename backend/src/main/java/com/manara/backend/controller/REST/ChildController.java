package com.manara.backend.controller.REST;

import com.manara.backend.dto.ChildRequest;
import com.manara.backend.dto.ChildResponseDto;
import com.manara.backend.model.Child;
import com.manara.backend.service.ChildService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/children")
public class ChildController {

    @Autowired
    private ChildService childService;

    @GetMapping
    public List<ChildResponseDto> getChildren() {
        return childService.findAll().stream().map(ChildResponseDto::from).toList();
    }

    @GetMapping("/search")
    public List<ChildResponseDto> getChildren(@RequestParam(required = false) String search) {
        return childService.searchByName(search).stream().map(ChildResponseDto::from).toList();
    }

    @PostMapping
    public ResponseEntity<ChildResponseDto> addChild(@RequestBody @Valid ChildRequest request) {
        Child child = childService.createChild(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ChildResponseDto.from(child));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChildResponseDto> updateChild(@PathVariable Integer id, @RequestBody @Valid ChildRequest request) {
        Child child = childService.updateChild(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(ChildResponseDto.from(child));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChildResponseDto> deleteChild(@PathVariable Integer id) {
        childService.deleteChild(id);
        return ResponseEntity.noContent().build();
    }
}
