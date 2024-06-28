package com.project.npp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.npp.repositories.ComplianceLogsRepository;
import com.project.npp.utilities.AirtelVerificationDetailsService;
import com.project.npp.utilities.JioVerificationDetailsService;
import com.project.npp.utilities.VerificationDetailsConversion;
import com.project.npp.entities.AirtelVerificationDetails;
import com.project.npp.entities.ComplianceLogs;
import com.project.npp.entities.Customer;
import com.project.npp.entities.JioVerificationDetails;
import com.project.npp.entities.NumberStatus;
import com.project.npp.entities.Operator;
import com.project.npp.entities.PortRequest;
import com.project.npp.entities.Status;
import com.project.npp.entities.VerificationDetails;
import com.project.npp.exceptions.CustomerNotFoundException;
import com.project.npp.exceptions.LogNotFoundException;
import com.project.npp.exceptions.OperatorNotFoundException;
import com.project.npp.exceptions.PortRequestNotFoundException;
import com.project.npp.exceptions.VerificationDetailsNotFoundException;
import com.project.npp.exceptionmessages.QueryMapper;

@Service
public class ComplianceLogsServiceImpl implements ComplianceLogsService {

	private static Logger loggers = LogManager.getLogger(ComplianceLogsServiceImpl.class);

	@Autowired
	private ComplianceLogsRepository repo;

	@Autowired
	private PortRequestService portRequestService;

	@Autowired
	private AirtelVerificationDetailsService airtelVerificationDetailsService;

	@Autowired
	private JioVerificationDetailsService jioVerificationDetailsService;

	@Autowired
	private OperatorService operatorService;

	@Autowired
	private VerificationDetailsConversion convert;

	// Method to add a compliance log
	@Override
	public ComplianceLogs addLog(ComplianceLogs complianceLogs)
			throws PortRequestNotFoundException, CustomerNotFoundException {
		complianceLogs.setCheckDate(null);
		complianceLogs.setCheckPassed(false);
		complianceLogs.setNotes(null);
		ComplianceLogs c = repo.save(complianceLogs);
		return c;
	}

	// Method to get a compliance log by its ID
	@Override
	public ComplianceLogs getLog(Integer LogId) throws LogNotFoundException {
		Optional<ComplianceLogs> complianceLog = repo.findById(LogId);
		loggers.info(QueryMapper.GET_LOG);
		if (complianceLog.isPresent())
			return complianceLog.get();
		else
			loggers.error(QueryMapper.CANNOT_GET_LOG);
		throw new LogNotFoundException(QueryMapper.CANNOT_GET_LOG);
	}

	// Method to update a compliance log
	@Override
	public ComplianceLogs UpdateLog(ComplianceLogs complianceLogs)
			throws LogNotFoundException, PortRequestNotFoundException, CustomerNotFoundException {
		Optional<ComplianceLogs> complianceLog = repo.findById(complianceLogs.getLogId());
		if (complianceLog.isPresent()) {

			if (complianceLogs.isCheckPassed()) {
				PortRequest portRequest = portRequestService
						.getPortRequest(complianceLogs.getPortRequest().getRequestId());
				portRequest.setComplianceChecked(true);
				PortRequest portReq = portRequestService.updatePortRequest(portRequest);
				complianceLogs.setPortRequest(portReq);
			} else {
				PortRequest portRequest = portRequestService
						.getPortRequest(complianceLogs.getPortRequest().getRequestId());
				complianceLogs.setPortRequest(portRequest);
			}

			repo.save(complianceLogs);
			loggers.info(QueryMapper.UPDATE_LOG);
			return complianceLog.get();
		} else {
			loggers.error(QueryMapper.CANNOT_UPDATE_LOG);
			throw new LogNotFoundException(QueryMapper.CANNOT_UPDATE_LOG);
		}
	}

	// Method to get all logs
	@Override
	public List<ComplianceLogs> getAllComplianceLogs() throws LogNotFoundException {
		List<ComplianceLogs> complianceLogs = (List<ComplianceLogs>) repo.findAll();
		if (!complianceLogs.isEmpty()) {
			loggers.info(QueryMapper.GET_LOG);
			return complianceLogs;
		} else {
			loggers.error(QueryMapper.CANNOT_GET_LOG);
			throw new LogNotFoundException(QueryMapper.CANNOT_GET_LOG);
		}
	}

	// Method to update and verify log
	@SuppressWarnings("static-access")
	@Override
	public String VerifyAndUpdateLog(Integer logId) throws LogNotFoundException, PortRequestNotFoundException,
			CustomerNotFoundException, VerificationDetailsNotFoundException, OperatorNotFoundException {
		Optional<ComplianceLogs> log = repo.findById(logId);
		Long phoneNumber = log.get().getCustomer().getPhoneNumber();
		if (log.isPresent()) {
			Operator operatorJio = operatorService.getOperatorByOperatorName("jio");
			Operator operatorAirtel = operatorService.getOperatorByOperatorName("airtel");
			if (log.get().getCustomer().getCurrentOperator().equals(operatorJio)) {
				JioVerificationDetails vDetails = jioVerificationDetailsService.getByPhoneNumber(phoneNumber);
				VerificationDetails details = convert.convertToVerificationDetails(vDetails);
				if (details.getCustomerIdentityVerified() && details.getNoOutstandingPayments()
						&& details.getNumberStatus() == NumberStatus.ACTIVE
						&& details.getTimeSinceLastPort() > details.getContractualObligationsMet()) {
					log.get().setCheckPassed(true);
					log.get().setCheckDate(LocalDate.now());
					log.get().setNotes(QueryMapper.VERIFICATION);
					repo.save(log.get());
					PortRequest portRequest = log.get().getPortRequest();
					portRequest.setComplianceChecked(true);
					portRequest.setApprovalStatus(Status.COMPLETED);
					portRequestService.updatePortRequest(portRequest);
					return QueryMapper.LOG_UPDATE_SUCCESSFUL;
				} else {
					if (!details.getCustomerIdentityVerified()) {
						log.get().setCheckPassed(false);
						log.get().setCheckDate(LocalDate.now());
						log.get().setNotes(QueryMapper.CUSTOMER_IDENTITY);
						repo.save(log.get());
						PortRequest portRequest = log.get().getPortRequest();
						portRequest.setComplianceChecked(true);
						portRequest.setApprovalStatus(Status.REJECTED);
						portRequestService.updatePortRequest(portRequest);
						return QueryMapper.CUSTOMER_IDENTITY;
					} else if (details.getNumberStatus() != NumberStatus.ACTIVE) {
						log.get().setCheckPassed(false);
						log.get().setCheckDate(LocalDate.now());
						log.get().setNotes(QueryMapper.NUMBER_STATUS);
						repo.save(log.get());
						PortRequest portRequest = log.get().getPortRequest();
						portRequest.setComplianceChecked(true);
						portRequest.setApprovalStatus(Status.REJECTED);
						portRequestService.updatePortRequest(portRequest);
						return QueryMapper.NUMBER_STATUS;
					}

					else if (details.getContractualObligationsMet() >= details.getTimeSinceLastPort()) {
						log.get().setCheckPassed(false);
						log.get().setCheckDate(LocalDate.now());
						log.get().setNotes(QueryMapper.TIME_SINCE_LAST_PORT);
						repo.save(log.get());
						PortRequest portRequest = log.get().getPortRequest();
						portRequest.setComplianceChecked(true);
						portRequest.setApprovalStatus(Status.REJECTED);
						portRequestService.updatePortRequest(portRequest);
						return QueryMapper.TIME_SINCE_LAST_PORT;
					} else {
						log.get().setCheckPassed(false);
						log.get().setCheckDate(LocalDate.now());
						log.get().setNotes(QueryMapper.OUTSTANDING_BILL);
						repo.save(log.get());
						PortRequest portRequest = log.get().getPortRequest();
						portRequest.setComplianceChecked(true);
						portRequest.setApprovalStatus(Status.REJECTED);
						portRequestService.updatePortRequest(portRequest);
						return QueryMapper.OUTSTANDING_BILL;
					}
				}
			}
			if (log.get().getCustomer().getCurrentOperator().equals(operatorAirtel)) {
				AirtelVerificationDetails vDetails = airtelVerificationDetailsService.getByPhoneNumber(phoneNumber);
				VerificationDetails details = convert.convertToVerificationDetails(vDetails);
				if (details.getCustomerIdentityVerified() && details.getNoOutstandingPayments()
						&& details.getNumberStatus() == NumberStatus.ACTIVE
						&& details.getTimeSinceLastPort() > details.getContractualObligationsMet()) {
					log.get().setCheckPassed(true);
					log.get().setCheckDate(LocalDate.now());
					log.get().setNotes(QueryMapper.VERIFICATION);
					repo.save(log.get());
					PortRequest portRequest = log.get().getPortRequest();
					portRequest.setComplianceChecked(true);
					portRequest.setApprovalStatus(Status.COMPLETED);
					portRequestService.updatePortRequest(portRequest);
					return QueryMapper.LOG_UPDATE_SUCCESSFUL;
				} else {
					if (!details.getCustomerIdentityVerified()) {
						log.get().setCheckPassed(false);
						log.get().setCheckDate(LocalDate.now());
						log.get().setNotes(QueryMapper.CUSTOMER_IDENTITY);
						repo.save(log.get());
						PortRequest portRequest = log.get().getPortRequest();
						portRequest.setComplianceChecked(true);
						portRequest.setApprovalStatus(Status.REJECTED);
						portRequestService.updatePortRequest(portRequest);
						return QueryMapper.CUSTOMER_IDENTITY;
					} else if (details.getNumberStatus() != NumberStatus.ACTIVE) {
						log.get().setCheckPassed(false);
						log.get().setCheckDate(LocalDate.now());
						log.get().setNotes(QueryMapper.NUMBER_STATUS);
						repo.save(log.get());
						PortRequest portRequest = log.get().getPortRequest();
						portRequest.setComplianceChecked(true);
						portRequest.setApprovalStatus(Status.REJECTED);
						portRequestService.updatePortRequest(portRequest);
						return QueryMapper.NUMBER_STATUS;
					}

					else if (details.getContractualObligationsMet() >= details.getTimeSinceLastPort()) {
						log.get().setCheckPassed(false);
						log.get().setCheckDate(LocalDate.now());
						log.get().setNotes(QueryMapper.TIME_SINCE_LAST_PORT);
						repo.save(log.get());
						PortRequest portRequest = log.get().getPortRequest();
						portRequest.setComplianceChecked(true);
						portRequest.setApprovalStatus(Status.REJECTED);
						portRequestService.updatePortRequest(portRequest);
						return QueryMapper.TIME_SINCE_LAST_PORT;
					} else {
						log.get().setCheckPassed(false);
						log.get().setCheckDate(LocalDate.now());
						log.get().setNotes(QueryMapper.OUTSTANDING_BILL);
						repo.save(log.get());
						PortRequest portRequest = log.get().getPortRequest();
						portRequest.setComplianceChecked(true);
						portRequest.setApprovalStatus(Status.REJECTED);
						portRequestService.updatePortRequest(portRequest);
						return QueryMapper.OUTSTANDING_BILL;
					}
				}
			} else
				return null;
		} else
			throw new LogNotFoundException(QueryMapper.CANNOT_GET_LOG);

	}

	// Get log by customer
	@Override
	public ComplianceLogs getLogByCustomer(Customer customer) throws LogNotFoundException {
		List<ComplianceLogs> logs = repo.findByCustomer(customer);
		if (!logs.isEmpty()) {

			return logs.get(logs.size() - 1);

		} else
			throw new LogNotFoundException(QueryMapper.CANNOT_GET_LOG);
	}

}