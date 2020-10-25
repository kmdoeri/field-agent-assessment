package learn.field_agent.models;

import java.util.ArrayList;
import java.util.List;

public class Agency {

    private int agencyId;
    private String shortName;
    private String longName;
    private List<Location> locations = new ArrayList<>(); //Agency can have multiple locations. One to many relationship
    private List<AgencyAgent> agents = new ArrayList<>(); //Agency can have multiple agents. Many ot many relationship

    public Agency() {
    }

    public Agency(int agentId, String shortName, String longName) { //Constructor only uses id, short and long name. Only columns in schema table
        //You can create an agency without locations or agents
        this.agencyId = agentId;
        this.shortName = shortName;
        this.longName = longName;
    }

    //Getters and setters for all fields
    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public List<Location> getLocations() {
        return new ArrayList<>(locations);
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<AgencyAgent> getAgents() {
        return new ArrayList<>(agents);
    }

    public void setAgents(List<AgencyAgent> agents) {
        this.agents = agents;
    }
}
