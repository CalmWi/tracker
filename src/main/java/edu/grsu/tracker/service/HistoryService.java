package edu.grsu.tracker.service;

import edu.grsu.tracker.exception.TrackerExceptoin;
import edu.grsu.tracker.storage.entity.History;
import edu.grsu.tracker.storage.repo.HistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepo historyRepo;

    public History getHistory(final Long id) {
        return historyRepo.findById(id).orElseThrow(() ->
                new TrackerExceptoin("History not found")
        );
    }

    public List<History> getHistories() {
        return historyRepo.findAll();
    }

    public History save(final History history) {
        return historyRepo.save(history);
    }

    public History update(final Long id, History history) {
        History get = getHistory(id);
        get.setChanges(history.getChanges());
        get.setUpdateDate(LocalDate.now());
        return historyRepo.save(get);
    }

    public void delete(final Long id) {
        historyRepo.deleteById(id);
    }
}
