package learn.field_agent.domain;

import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {

    private final SecurityClearanceRepository securityClearanceRepository;
    private final AgencyAgentRepository agencyAgentRepository;

    public SecurityClearanceService(SecurityClearanceRepository securityClearanceRepository, AgencyAgentRepository agencyAgentRepository) {
        this.securityClearanceRepository = securityClearanceRepository;
        this.agencyAgentRepository = agencyAgentRepository;
    }

    public List<SecurityClearance> findAll() {
        return securityClearanceRepository.findAll();
    }

    public SecurityClearance findById(int securityClearanceId) {
        return securityClearanceRepository.findById(securityClearanceId);
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("Security clearance id cannot be set for 'add' operation.", ResultType.INVALID);
            return result;
        }

        SecurityClearance sc = securityClearanceRepository.add(securityClearance);
        result.setPayload(sc);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if(!securityClearanceRepository.update(securityClearance)) {
            result.addMessage("Security clearance id " + securityClearance.getSecurityClearanceId() + " not found.", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<SecurityClearance> deleteById(int securityClearanceId) {
        Result<SecurityClearance> result = new Result<>();
        for (AgencyAgent agencyAgent : agencyAgentRepository.findAll()) {
            if (agencyAgent.getSecurityClearance().getSecurityClearanceId() == securityClearanceId) {
                result.addMessage("Security clearance is in use and cannot be deleted.", ResultType.INVALID);
                return result;
            }
        }
        if (!securityClearanceRepository.deleteById(securityClearanceId)) {
            result.addMessage("Security clearance id " + securityClearanceId + " not found.", ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();

        if (securityClearance == null) {
            result.addMessage("Security clearance cannot be null.", ResultType.INVALID);
            return result;
        }

        if(Validations.isNullOrBlank(securityClearance.getName())) {
            result.addMessage("Name is required.", ResultType.INVALID);
        }

        for (SecurityClearance clearance : securityClearanceRepository.findAll()) {
            if(clearance.getName().equalsIgnoreCase(securityClearance.getName())) {
                result.addMessage("Name cannot be duplicated.", ResultType.INVALID);
            }
        }

        return result;
    }
}
