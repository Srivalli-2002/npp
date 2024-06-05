package com.project.npp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.request.ComplianceLogsRequest;
import com.project.npp.entities.request.UpdateComplianceLogsRequest;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.service.ComplianceLogsService;
import com.project.npp.service.PortRequestService;

@ExtendWith(MockitoExtension.class)
public class ComplianceOfficerControllerTest {

    @InjectMocks
    private ComplianceOfficerController complianceOfficerController;

    @Mock
    private PortRequestService portRequestService;

    @Mock
    private ComplianceLogsService complianceLogsService;

    private ComplianceLogsRequest complianceLogsRequest;
    private ComplianceLogs complianceLogs;
    private PortRequest portRequest;
    private UpdateComplianceLogsRequest updateComplianceLogsRequest;

    @BeforeEach
    void setUp() {
        complianceLogsRequest = new ComplianceLogsRequest();
        complianceLogsRequest.setPortRequestId(1);
        complianceLogsRequest.setCheckPassed(true);
        complianceLogsRequest.setNotes("Test notes");
        complianceLogsRequest.setCheckDate(LocalDate.of(2023, 06, 05));

        complianceLogs = new ComplianceLogs();
        complianceLogs.setLogId(1);
        complianceLogs.setPortRequest(portRequest);
        complianceLogs.setCheckPassed(true);
        complianceLogs.setNotes("Test notes");
        complianceLogs.setCheckDate(LocalDate.of(2023, 06, 05));

        portRequest = new PortRequest();
        portRequest.setRequestId(1);

        updateComplianceLogsRequest = new UpdateComplianceLogsRequest();
        updateComplianceLogsRequest.setLogId(1);
        updateComplianceLogsRequest.setPortRequestId(1);
        updateComplianceLogsRequest.setCheckPassed(true);
        updateComplianceLogsRequest.setNotes("Updated notes");
        updateComplianceLogsRequest.setCheckDate(LocalDate.of(2023, 06, 05));
    }

    @Test
    void testAddLog() throws CustomerNotFoundException, PortRequestNotFoundException {
        when(portRequestService.getPortRequest(1)).thenReturn(portRequest);
        when(complianceLogsService.addLog(any(ComplianceLogs.class))).thenReturn(complianceLogs);

        ResponseEntity<ComplianceLogs> response = complianceOfficerController.addLog(complianceLogsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complianceLogs, response.getBody());
    }

    @Test
    void testAddLogPortRequestNotFound() throws PortRequestNotFoundException {
        when(portRequestService.getPortRequest(1)).thenThrow(new PortRequestNotFoundException("Port request not found"));

        assertThrows(PortRequestNotFoundException.class, () -> {
            complianceOfficerController.addLog(complianceLogsRequest);
        });
    }

    @Test
    void testGetLog() throws LogNotFoundException {
        when(complianceLogsService.getLog(1)).thenReturn(complianceLogs);

        ResponseEntity<ComplianceLogs> response = complianceOfficerController.getLog(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complianceLogs, response.getBody());
    }

    @Test
    void testGetLogNotFound() throws LogNotFoundException {
        when(complianceLogsService.getLog(1)).thenThrow(new LogNotFoundException("Log not found"));

        assertThrows(LogNotFoundException.class, () -> {
            complianceOfficerController.getLog(1);
        });
    }

    @Test
    void testUpdateLog() throws CustomerNotFoundException, PortRequestNotFoundException, LogNotFoundException {
        when(portRequestService.getPortRequest(1)).thenReturn(portRequest);
        when(complianceLogsService.UpdateLog(any(ComplianceLogs.class))).thenReturn(complianceLogs);

        ResponseEntity<ComplianceLogs> response = complianceOfficerController.updateLog(updateComplianceLogsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(complianceLogs, response.getBody());
    }

    @Test
    void testUpdateLogPortRequestNotFound() throws PortRequestNotFoundException {
        when(portRequestService.getPortRequest(1)).thenThrow(new PortRequestNotFoundException("Port request not found"));

        assertThrows(PortRequestNotFoundException.class, () -> {
            complianceOfficerController.updateLog(updateComplianceLogsRequest);
        });
    }

    @Test
    void testUpdateLogNotFound() throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException {
        when(portRequestService.getPortRequest(1)).thenReturn(portRequest);
        when(complianceLogsService.UpdateLog(any(ComplianceLogs.class))).thenThrow(new LogNotFoundException("Log not found"));

        assertThrows(LogNotFoundException.class, () -> {
            complianceOfficerController.updateLog(updateComplianceLogsRequest);
        });
    }
}
