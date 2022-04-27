Implement API server using SpringBoot which will allow to create hierarchical structure for employees in the company.

Rules:

    1. One supervisor can have multiple subordinates.

    2. Any subordinate can be also a supervisor

    3. Number of levels in the hierarchy is infinity

    4. Service should be authenticated by JWT,

    6. Data about logged in user should be returned by endpoint /me.

    6. I want to have paginated list of emploeyes,

    7. Possbility to add new employee to the hierarchy

    8. move employee in the hierarchy should be possible

    9. in case of deleting supervisor, exception should be thrown if it have children

    10. expecting to have flatten json structure for visualistaion the tree by react-sortable-plugin (https://github.com/frontend-collective/react-sortable-tree) 
