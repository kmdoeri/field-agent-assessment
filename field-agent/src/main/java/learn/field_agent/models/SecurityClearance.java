package learn.field_agent.models;

import java.util.Objects;

public class SecurityClearance {

    private int securityClearanceId;
    private String name;

    //Getters and setters for all fields
    public int getSecurityClearanceId() {
        return securityClearanceId;
    }

    public void setSecurityClearanceId(int securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Empty constructor and constructor with all fields
    public SecurityClearance() {
    }

    public SecurityClearance(int securityClearanceId, String name) {
        this.securityClearanceId = securityClearanceId;
        this.name = name;
    }

    //Overrides for equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityClearance that = (SecurityClearance) o;
        return securityClearanceId == that.securityClearanceId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(securityClearanceId, name);
    }
}
