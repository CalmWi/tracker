package edu.grsu.tracker.rest;

import edu.grsu.tracker.model.HistoryModel;
import edu.grsu.tracker.service.HistoryService;
import edu.grsu.tracker.storage.entity.History;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Histories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/histories")
public class HistoryController {
    private final HistoryService historyService;
    private final ModelMapper modelMapper;

    @Operation(description = "Get all 'Histories'.")
    @GetMapping(value = "")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<List<HistoryModel>> getHistories() {
        List<HistoryModel> historyModels = historyService.getHistories().stream()
                .map(issue -> modelMapper.map(issue, HistoryModel.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(historyModels);
    }

    @Operation(description = "Get 'History' by id.")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<HistoryModel> getHistory(@Parameter(description = "Issue ID", required = true)
                                                   @PathVariable("id") Long id) {
        History history = historyService.getHistory(id);
        return ResponseEntity.ok(modelMapper.map(history, HistoryModel.class));
    }

    @Operation(description = "Delete 'History' by id.")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('member')")
    public ResponseEntity<Void> deleteHistory(@Parameter(description = "Issue ID", required = true)
                                              @PathVariable("id") Long id) {
        historyService.delete(id);
        return ResponseEntity.ok().build();
    }
}
