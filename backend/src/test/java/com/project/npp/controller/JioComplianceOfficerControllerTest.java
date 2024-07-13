package com.project.npp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.npp.entities.JioVerificationDetails;
import com.project.npp.entities.NumberStatus;
import com.project.npp.entities.request.GetVerificationDetailsByPhn;
import com.project.npp.entities.request.UpdateVerificationDetails;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.utilities.JioVerificationDetailsService;

@WebMvcTest(JioComplianceOfficerController.class)
public class JioComplianceOfficerControllerTest {

    @Autowired
    private JioComplianceOfficerController controller;

    @MockBean
    private JioVerificationDetailsService verificationDetailsService;

    @Test
    void testAddVerificationDetails_Success() throws IOException {
        // Arrange
        when(verificationDetailsService.fetchVerificationDetails()).thenReturn("Details fetched successfully");

        // Act
        ResponseEntity<String> responseEntity = controller.addVerificationDetails();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Details fetched successfully", responseEntity.getBody());
    }
    
    @Test
    void testAddVerificationDetails_Failure() throws IOException {
        when(verificationDetailsService.fetchVerificationDetails()).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> {
            controller.addVerificationDetails();
        });
    }

    @Test
    void testUpdateVerificationDetails_Success() throws VerificationDetailsNotFoundException {
        // Arrange
        UpdateVerificationDetails updateDetails = new UpdateVerificationDetails();
        updateDetails.setContractualObligationsMet(85);
        updateDetails.setCustomerIdentityVerified(true);
        updateDetails.setNoOutstandingPayments(true);
        updateDetails.setNotificationToCurrentOperator(true);
        updateDetails.setNumberStatus(NumberStatus.ACTIVE);
        updateDetails.setPhoneNumber(1234567890L);
        updateDetails.setTimeSinceLastPort(6);

        JioVerificationDetails updatedDetails = new JioVerificationDetails();
        updatedDetails.setPhoneNumber(1234567890L);

        when(verificationDetailsService.updateVerificationDetails(any())).thenReturn(updatedDetails);

        // Act
        ResponseEntity<JioVerificationDetails> responseEntity = controller.updateVerificationDetails(updateDetails);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1234567890L, responseEntity.getBody().getPhoneNumber());
    }
    
    @Test
    void testUpdateVerificationDetails_NotFound() throws VerificationDetailsNotFoundException {
        UpdateVerificationDetails updateDetails = new UpdateVerificationDetails();
        updateDetails.setPhoneNumber(1234567890L); // Providing an invalid phone number

        when(verificationDetailsService.updateVerificationDetails(any())).thenThrow(VerificationDetailsNotFoundException.class);

        assertThrows(VerificationDetailsNotFoundException.class, () -> {
            controller.updateVerificationDetails(updateDetails);
        });
    }

    @Test
    void testGetAllVerificationDetails_Success() throws VerificationDetailsNotFoundException {
        // Arrange
        List<JioVerificationDetails> mockDetailsList = Collections.singletonList(new JioVerificationDetails());
        when(verificationDetailsService.getAll()).thenReturn(mockDetailsList);

        // Act
        ResponseEntity<List<JioVerificationDetails>> responseEntity = controller.getAllVerificationDetails();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
    }

    @Test
    void testGetVerificationDetailsByPhn_Success() throws VerificationDetailsNotFoundException, LogNotFoundException {
        // Arrange
        GetVerificationDetailsByPhn request = new GetVerificationDetailsByPhn();
        request.setPhoneNumber(1234567890L);

        JioVerificationDetails mockDetails = new JioVerificationDetails();
        mockDetails.setPhoneNumber(1234567890L);

        when(verificationDetailsService.getByPhoneNumber(1234567890L)).thenReturn(mockDetails);

        // Act
        ResponseEntity<JioVerificationDetails> responseEntity = controller.getVerificationDetailsByPhn(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1234567890L, responseEntity.getBody().getPhoneNumber());
    }
    
    @Test
    void testGetVerificationDetailsByPhn_NotFound() throws VerificationDetailsNotFoundException {
        GetVerificationDetailsByPhn request = new GetVerificationDetailsByPhn();
        request.setPhoneNumber(9876543210L); // Providing an invalid phone number

        when(verificationDetailsService.getByPhoneNumber(9876543210L)).thenThrow(VerificationDetailsNotFoundException.class);

        assertThrows(VerificationDetailsNotFoundException.class, () -> {
            controller.getVerificationDetailsByPhn(request);
        });
    }
}
