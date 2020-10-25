package learn.field_agent.models;

import java.time.LocalDate;

public class AgencyAgent {

    private int agencyId; //agency id, but no agent object
    private String identifier;
    private LocalDate activationDate;
    private boolean active;

    private Agent agent; //agent object, but no agent id
    private SecurityClearance securityClearance; //security clearance object, but no security clearance id

    //No constructors
    //Getters and setters for all fields
    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public SecurityClearance getSecurityClearance() {
        return securityClearance;
    }

    public void setSecurityClearance(SecurityClearance securityClearance) {
        this.securityClearance = securityClearance;
    }
}
