package com.project.npp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.PortRequest;
import com.project.npp.exceptionmessages.QueryMapper;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.repositories.ComplianceLogsRepository;

class ComplianceLogsServiceImplTest {

    @Mock
    private ComplianceLogsRepository complianceLogsRepository;

    @Mock
    private PortRequestService portRequestService;

    @InjectMocks
    private ComplianceLogsServiceImpl complianceLogsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddLogCheckPassed() throws PortRequestNotFoundException, CustomerNotFoundException {
        ComplianceLogs complianceLog = new ComplianceLogs();
        complianceLog.setCheckPassed(true);
        PortRequest portRequest = new PortRequest();
        portRequest.setRequestId(1);
        complianceLog.setPortRequest(portRequest);

        when(portRequestService.getPortRequest(portRequest.getRequestId())).thenReturn(portRequest);
        when(portRequestService.updatePortRequest(any(PortRequest.class))).thenReturn(portRequest);
        when(complianceLogsRepository.save(any(ComplianceLogs.class))).thenReturn(complianceLog);

        ComplianceLogs result = complianceLogsService.addLog(complianceLog);

        assertEquals(complianceLog, result);
        verify(portRequestService, times(1)).getPortRequest(portRequest.getRequestId());
        verify(portRequestService, times(1)).updatePortRequest(portRequest);
        verify(complianceLogsRepository, times(1)).save(complianceLog);
    }

    @Test
    void testAddLogCheckNotPassed() throws PortRequestNotFoundException, CustomerNotFoundException {
        ComplianceLogs complianceLog = new ComplianceLogs();
        complianceLog.setCheckPassed(false);
        PortRequest portRequest = new PortRequest();
        portRequest.setRequestId(1);
        complianceLog.setPortRequest(portRequest);

        when(portRequestService.getPortRequest(portRequest.getRequestId())).thenReturn(portRequest);
        when(complianceLogsRepository.save(any(ComplianceLogs.class))).thenReturn(complianceLog);

        ComplianceLogs result = complianceLogsService.addLog(complianceLog);

        assertEquals(complianceLog, result);
        verify(portRequestService, times(1)).getPortRequest(portRequest.getRequestId());
        verify(portRequestService, times(0)).updatePortRequest(any(PortRequest.class));
        verify(complianceLogsRepository, times(1)).save(complianceLog);
    }

    @Test
    void testGetLogSuccess() throws LogNotFoundException {
        Integer logId = 1;
        ComplianceLogs complianceLog = new ComplianceLogs();
        complianceLog.setLogId(logId);

        when(complianceLogsRepository.findById(logId)).thenReturn(Optional.of(complianceLog));

        ComplianceLogs result = complianceLogsService.getLog(logId);

        assertEquals(complianceLog, result);
        verify(complianceLogsRepository, times(1)).findById(logId);
    }

    @Test
    void testGetLogNotFound() {
        Integer logId = 1;

        when(complianceLogsRepository.findById(logId)).thenReturn(Optional.empty());

        LogNotFoundException exception = assertThrows(LogNotFoundException.class, () -> {
            complianceLogsService.getLog(logId);
        });

        assertEquals(QueryMapper.CANNOT_GET_LOG, exception.getMessage());
        verify(complianceLogsRepository, times(1)).findById(logId);
    }

    @Test
    void testUpdateLogCheckPassed() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException {
        Integer logId = 1;
        ComplianceLogs complianceLog = new ComplianceLogs();
        complianceLog.setLogId(logId);
        complianceLog.setCheckPassed(true);
        PortRequest portRequest = new PortRequest();
        portRequest.setRequestId(1);
        complianceLog.setPortRequest(portRequest);

        when(complianceLogsRepository.findById(logId)).thenReturn(Optional.of(complianceLog));
        when(portRequestService.getPortRequest(portRequest.getRequestId())).thenReturn(portRequest);
        when(portRequestService.updatePortRequest(any(PortRequest.class))).thenReturn(portRequest);
        when(complianceLogsRepository.save(any(ComplianceLogs.class))).thenReturn(complianceLog);

        ComplianceLogs result = complianceLogsService.UpdateLog(complianceLog);

        assertEquals(complianceLog, result);
        verify(portRequestService, times(1)).getPortRequest(portRequest.getRequestId());
        verify(portRequestService, times(1)).updatePortRequest(portRequest);
        verify(complianceLogsRepository, times(1)).save(complianceLog);
    }

    @Test
    void testUpdateLogCheckNotPassed() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException {
        Integer logId = 1;
        ComplianceLogs complianceLog = new ComplianceLogs();
        complianceLog.setLogId(logId);
        complianceLog.setCheckPassed(false);
        PortRequest portRequest = new PortRequest();
        portRequest.setRequestId(1);
        complianceLog.setPortRequest(portRequest);

        when(complianceLogsRepository.findById(logId)).thenReturn(Optional.of(complianceLog));
        when(portRequestService.getPortRequest(portRequest.getRequestId())).thenReturn(portRequest);
        when(complianceLogsRepository.save(any(ComplianceLogs.class))).thenReturn(complianceLog);

        ComplianceLogs result = complianceLogsService.UpdateLog(complianceLog);

        assertEquals(complianceLog, result);
        verify(portRequestService, times(1)).getPortRequest(portRequest.getRequestId());
        verify(portRequestService, times(0)).updatePortRequest(any(PortRequest.class));
        verify(complianceLogsRepository, times(1)).save(complianceLog);
    }

    @Test
    void testUpdateLogNotFound() {
        Integer logId = 1;
        ComplianceLogs complianceLog = new ComplianceLogs();
        complianceLog.setLogId(logId);

        when(complianceLogsRepository.findById(logId)).thenReturn(Optional.empty());

        LogNotFoundException exception = assertThrows(LogNotFoundException.class, () -> {
            complianceLogsService.UpdateLog(complianceLog);
        });

        assertEquals(QueryMapper.CANNOT_UPDATE_LOG, exception.getMessage());
        verify(complianceLogsRepository, times(1)).findById(logId);
        verify(complianceLogsRepository, times(0)).save(any(ComplianceLogs.class));
    }
}
