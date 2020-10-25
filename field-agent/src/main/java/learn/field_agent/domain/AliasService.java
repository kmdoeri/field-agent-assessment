package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasService {

    private final AliasRepository aliasRepository;
    private final AgentRepository agentRepository;

    public AliasService(AliasRepository aliasRepository, AgentRepository agentRepository) {
        this.aliasRepository = aliasRepository;
        this.agentRepository = agentRepository;
    }

    public List<Alias> findAll() {
        return aliasRepository.findAll();
    }

    public Alias findById(int aliasId) {
        return aliasRepository.findById(aliasId);
    }

    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() != 0) {
            result.addMessage("Alias id cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        alias = aliasRepository.add(alias);
        result.setPayload(alias);
        return result;
    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() <= 0) {
            result.addMessage("Alias id must be set for `update` operation.", ResultType.INVALID);
            return result;
        }

        if (!aliasRepository.update(alias)) {
            String msg = String.format("Alias id %s not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {
        return aliasRepository.deleteById(aliasId);
    }

    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();

        if (alias == null) {
            result.addMessage("Alias cannot be null.", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("Alias name is required.", ResultType.INVALID);
        }

//        for (Alias a : aliasRepository.findAll()) {
//            if (a.getAliasId() == alias.getAliasId()) {
//                result.addMessage("Alias id must be unique.", ResultType.INVALID);
//                break;
//            }
//        }

//        if (alias.getAgentId() == 0) {
//            result.addMessage("Alias must be attached to an Agent.", ResultType.INVALID);
//        }

//        boolean agentExists = agentRepository.findAll().stream()
//                .anyMatch(agent -> agent.getAgentId() == alias.getAgentId());

//        for (Agent agent : agentRepository.findAll()) {
//            if (agent.getAgentId() == alias.getAgentId()) {
//                agentExists = true;
//                break;
//            }
//        }

//        if (!agentExists) {
//            result.addMessage("Agent associated with agent id does not exist.", ResultType.INVALID);
//        }

        for (Alias a : aliasRepository.findAll()) {
            if (a.getName().equalsIgnoreCase(alias.getName())) {
                if (Validations.isNullOrBlank(alias.getPersona())) {
                    result.addMessage("Persona is required for a duplicate name.", ResultType.INVALID);
                    break;
                }
            }
        }

        return result;
    }
}
