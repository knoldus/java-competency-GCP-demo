package com.nashtech.service;

import org.springframework.stereotype.Service;

/**
 * Service class responsible for fetching and sending vehicle data.
 */
@Service
public interface ReactiveDataService {
    public void fetchAndSendData();
}
