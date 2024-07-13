package com.project.npp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.npp.entities.AirtelVerificationDetails;
import com.project.npp.entities.NumberStatus;
import com.project.npp.entities.request.GetVerificationDetailsByPhn;
import com.project.npp.entities.request.UpdateVerificationDetails;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.utilities.AirtelVerificationDetailsService;

@WebMvcTest(AirtelComplianceOfficerController.class)
public class AirtelComplianceOfficerControllerTest {

    @Autowired
    private AirtelComplianceOfficerController controller;

    @MockBean
    private AirtelVerificationDetailsService verificationDetailsService;

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
    void testAddVerificationDetails_ExceptionThrown() throws IOException {
        // Arrange
        when(verificationDetailsService.fetchVerificationDetails()).thenThrow(new IOException("Failed to fetch details"));

        // Act & Assert
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

        AirtelVerificationDetails updatedDetails = new AirtelVerificationDetails();
        updatedDetails.setPhoneNumber(1234567890L);

        when(verificationDetailsService.updateVerificationDetails(any())).thenReturn(updatedDetails);

        // Act
        ResponseEntity<AirtelVerificationDetails> responseEntity = controller.updateVerificationDetails(updateDetails);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1234567890L, responseEntity.getBody().getPhoneNumber());
    }
    
    @Test
    void testUpdateVerificationDetails_VerificationDetailsNotFoundException() throws VerificationDetailsNotFoundException {
        // Arrange
        UpdateVerificationDetails updateDetails = new UpdateVerificationDetails();
        updateDetails.setContractualObligationsMet(85);
        updateDetails.setCustomerIdentityVerified(true);
        updateDetails.setNoOutstandingPayments(true);
        updateDetails.setNotificationToCurrentOperator(true);
        updateDetails.setNumberStatus(NumberStatus.ACTIVE);
        updateDetails.setPhoneNumber(1234567890L);
        updateDetails.setTimeSinceLastPort(6);

        when(verificationDetailsService.updateVerificationDetails(any())).thenThrow(new VerificationDetailsNotFoundException("Details not found"));

        // Act & Assert
        assertThrows(VerificationDetailsNotFoundException.class, () -> {
            controller.updateVerificationDetails(updateDetails);
        });
    }

    @Test
    void testGetAllVerificationDetails_Success() throws VerificationDetailsNotFoundException {
        // Arrange
        List<AirtelVerificationDetails> mockDetailsList = Collections.singletonList(new AirtelVerificationDetails());
        when(verificationDetailsService.getAll()).thenReturn(mockDetailsList);

        // Act
        ResponseEntity<List<AirtelVerificationDetails>> responseEntity = controller.getAllVerificationDetails();

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

        AirtelVerificationDetails mockDetails = new AirtelVerificationDetails();
        mockDetails.setPhoneNumber(1234567890L);

        when(verificationDetailsService.getByPhoneNumber(1234567890L)).thenReturn(mockDetails);

        // Act
        ResponseEntity<AirtelVerificationDetails> responseEntity = controller.getVerificationDetailsByPhn(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1234567890L, responseEntity.getBody().getPhoneNumber());
    }
    
    @Test
    void testGetVerificationDetailsByPhn_VerificationDetailsNotFoundException() throws VerificationDetailsNotFoundException {
        // Arrange
        GetVerificationDetailsByPhn request = new GetVerificationDetailsByPhn();
        request.setPhoneNumber(9876543210L); // Providing an invalid phone number

        when(verificationDetailsService.getByPhoneNumber(9876543210L)).thenThrow(new VerificationDetailsNotFoundException("Details not found"));

        // Act & Assert
        assertThrows(VerificationDetailsNotFoundException.class, () -> {
            controller.getVerificationDetailsByPhn(request);
        });
    }
}
