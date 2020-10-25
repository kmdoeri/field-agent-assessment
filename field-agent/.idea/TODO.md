[x] Go through all of the code and make sure you understand it
[x] Make notes
[x] Models - 30 min
    Make Alias Model- have agentId in it
    Put list of Alias object in Agent model
[x] Mappers - 30 min
    Create alias mapper - use location for inspo
[x] Repositories - 3 hrs?
    [x] Security Clearance
        Find All
        Find by id
        Add - with keyholder
        Update
        Delete
    Aliases
        Find all
        Find by id?
        Add - with keyholder
        Update - don't allow agent id update
        Delete
    Agent
        Delete - delete from alias
        Find by id: show aliases as well
        private add aliases method
        (see agency and location for inspirtion)
    AgencyAgent
        findAll?
        Delete - secruityclearance first 
    Test test test!
        use the testing methods implemented for other repositories (known good state)
        use the sql already made
        use @BeforeAll for aliases
        add test for any method you add! Test immidiately after you add
    Go through what you've added and add @Transactional when appropriate
[] Services - 4 hrs?
    Security Clearance
        find all
        add - validate
        update - validate
        delete - not sure how to do this validation yet... perhaps add findAll in AgencyAgent?
        validate - name requried, cannot be duplicated (use find all method for name duplication)
    aliases
        add - validate
        update - validate
        delete
        validate - name required, persona not required unless a name is duplicated (use find all method)
    AgencyAgent? (no service right now)
    Test test test!
        Test method immidiately after you add it 
        Use mocking
[] Controllers - 4 hrs?
    Security Clearance
        GET - find all
        POST- add
        PUT - update
        DELETE - delete
    Aliases
        GET - find all
        POST - add
        PUT - update
        DELETE - delete
    AgencyAgent?
        GET - find all?
    Use ErrorResponse when I can!
    Test!
        Not required, but you should do it.
[] Global Error Handling - 2 hrs
    Do your research - find the most precise exception
    Follow the lessons
Total time: 14 hrs

