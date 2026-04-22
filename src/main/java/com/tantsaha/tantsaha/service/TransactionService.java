package com.tantsaha.tantsaha.service;

import com.tantsaha.tantsaha.entity.collectivity.CollectivityTransaction;
import com.tantsaha.tantsaha.exception.AppBadRequestException;
import com.tantsaha.tantsaha.exception.AppConflictException;
import com.tantsaha.tantsaha.exception.AppNotFoundException;
import com.tantsaha.tantsaha.repository.CollectivityRepository;
import com.tantsaha.tantsaha.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;
    private CollectivityRepository collectivityRepository;
    public List<CollectivityTransaction> getTransactions(String collectivityId, LocalDate from, LocalDate to){
        if(from == null|| to == null){
            throw new AppBadRequestException("from and to cannot be null");
        }
        if(from.isAfter(to)){
            throw new AppBadRequestException("from cannot be after to");
        }
        if (!collectivityRepository.existsById(collectivityId)) {
            throw new AppNotFoundException("Collectivity not found");
        }
        return transactionRepository.getByCollectivityAndPeriod(collectivityId,from,to);
    }
}
