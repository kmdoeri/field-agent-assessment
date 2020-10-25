package learn.field_agent.domain;

import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.AgencyRepository;
import learn.field_agent.models.Agency;
import learn.field_agent.models.AgencyAgent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final AgencyAgentRepository agencyAgentRepository;

    public AgencyService(AgencyRepository agencyRepository, AgencyAgentRepository agencyAgentRepository) {
        this.agencyRepository = agencyRepository;
        this.agencyAgentRepository = agencyAgentRepository;
    }

    //nothing to validate for the find methods
    public List<Agency> findAll() {
        return agencyRepository.findAll();
    }

    public Agency findById(int agencyId) {
        return agencyRepository.findById(agencyId);
    }


    public Result<Agency> add(Agency agency) {
        Result<Agency> result = validate(agency); //validate first
        if (!result.isSuccess()) { //result is success if result type = SUCCESS
            return result;
        }

        if (agency.getAgencyId() != 0) { //only allow agency to be added if the id is the default
            result.addMessage("agencyId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        agency = agencyRepository.add(agency);
        result.setPayload(agency);
        return result; //result has agency being added and the
    }

    public Result<Agency> update(Agency agency) {
        Result<Agency> result = validate(agency); //validate it first
        if (!result.isSuccess()) {
            return result;
        }

        if (agency.getAgencyId() <= 0) { //id has to exist for agency to be updated
            result.addMessage("agencyId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!agencyRepository.update(agency)) { //if it didn't update, it wasn't found
            String msg = String.format("agencyId: %s, not found", agency.getAgencyId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int agencyId) {
        return agencyRepository.deleteById(agencyId);
    } // no validation

    public Result<Void> addAgent(AgencyAgent agencyAgent) { //adding agencyAgent //bad name
        Result<Void> result = validate(agencyAgent); //validate agency agent
        if (!result.isSuccess()) {
            return result;
        }

        if (!agencyAgentRepository.add(agencyAgent)) {
            result.addMessage("agent not added", ResultType.INVALID); //bad message
        }

        return result;
    }

    public Result<Void> updateAgent(AgencyAgent agencyAgent) { //updating agencyAgent
        Result<Void> result = validate(agencyAgent);
        if (!result.isSuccess()) {
            return result;
        }

        if (!agencyAgentRepository.update(agencyAgent)) {
            String msg = String.format("update failed for agency id %s, agent id %s: not found",
                    agencyAgent.getAgencyId(),
                    agencyAgent.getAgent().getAgentId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteAgentByKey(int agencyId, int agentId) {
        return agencyAgentRepository.deleteByKey(agencyId, agentId); // no validation delete an agencyAgent
    }

    private Result<Agency> validate(Agency agency) {
        Result<Agency> result = new Result<>();
        if (agency == null) {
            result.addMessage("agency cannot be null", ResultType.INVALID); //agency can't be null
            return result;
        }

        if (Validations.isNullOrBlank(agency.getShortName())) {
            result.addMessage("shortName is required", ResultType.INVALID); //names can't be null
        }

        if (Validations.isNullOrBlank(agency.getLongName())) {
            result.addMessage("longName is required", ResultType.INVALID);
        }

        return result;
    }

    private Result<Void> validate(AgencyAgent agencyAgent) { //validation method for agencyAgent
        Result<Void> result = new Result<>();
        if (agencyAgent == null) { //agencyAgent can't be null
            result.addMessage("agencyAgent cannot be null", ResultType.INVALID);
            return result;
        }

        if (agencyAgent.getAgent() == null) { //agent can't be null
            result.addMessage("agent cannot be null", ResultType.INVALID);
        }

        if (agencyAgent.getSecurityClearance() == null) { //security clearance can't be null
            result.addMessage("securityClearance cannot be null", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(agencyAgent.getIdentifier())) { //identifier cannot be null or blank
            result.addMessage("identifier is required", ResultType.INVALID);
        }

        if (agencyAgent.getActivationDate() == null) { //activation date can't be null
            result.addMessage("activationDate is required", ResultType.INVALID);
        }

        return result;
    }
}
