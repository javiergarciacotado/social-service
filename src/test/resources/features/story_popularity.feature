Feature: Test behaviour for handling popularity of social media stories

  Background:
    Given the Server is listening

  Scenario Outline: Retrieve through an invalid url should return a bad request
    Given a Story is stored with
      | id | popularity |
      | 1  | 10         |
    When the Client does a "GET" request on "<URI>"
    Then the Client should get a 400 response
    And the Response contains
      | PROBLEM TYPE  | PROBLEM TITLE           | PROBLEM DETAIL                        |
      | INVALID_PARAM | Invalid {uri} parameter | Invalid value for {uri} path supplied |
    Examples:
      | URI       |
      |           |
      | /story/-1 |

  Scenario: Retrieve a non stored story should return resource not found
    Given a Story is stored with
      | id | popularity |
      | 1  | 10         |
    When the Client does a "GET" request on "/story/2"
    Then the Client should get a 404 response
    And the Response contains
      | PROBLEM TYPE  | PROBLEM TITLE      | PROBLEM DETAIL                      |
      | INVALID_PARAM | Resource not found | There is no popularity for story: 2 |

  Scenario Outline: Retrieve a valid stored story
    Given a Story is stored with
      | id | popularity   |
      | 1  | <POPULARITY> |
    When the Client does a "GET" request on "/story/1"
    Then the Client should get a 200 response
    And the Response contains popularity of <POPULARITY>
    Examples:
      | POPULARITY |
      | -1         |
      | 0          |
      | 1          |

  Scenario Outline: Post through an invalid url should return a bad request
    When the Client does a "POST" request on "<URI>"
    Then the Client should get a 400 response
    And the Response contains
      | PROBLEM TYPE  | PROBLEM TITLE           | PROBLEM DETAIL                        |
      | INVALID_PARAM | Invalid {uri} parameter | Invalid value for {uri} path supplied |
    Examples:
      | URI       |
      |           |
      | /story/-1 |

  Scenario: Create a new story without JSON body should create it with 0 popularity
    When the Client does a "POST" request on "/story/1"
    Then the Client should get a 201 response
    And the Response contains popularity of 0

  Scenario: Create a new story without JSON body should create it with that popularity
    When the Client does a "POST" request on "/story/1" and body
    """
    { popularity : 10 }
    """
    Then the Client should get a 201 response
    And the Response contains popularity of 10

  Scenario Outline: Increment/Decrement likes through an invalid url should return a bad request
    Given a Story is stored with
      | id | popularity |
      | 1  | 10         |
    When the Client does a "PUT" request on "<URI>"
    Then the Client should get a 400 response
    And the Response contains
      | PROBLEM TYPE  | PROBLEM TITLE           | PROBLEM DETAIL                        |
      | INVALID_PARAM | Invalid {uri} parameter | Invalid value for {uri} path supplied |
    Examples:
      | URI               |
      |                   |
      | /story/-1/like    |
      | /story/-1/dislike |

  Scenario Outline: Increment/Decreament like through a valid url should return popularity increment/decrement it by 1
    Given a Story is stored with
      | id | popularity |
      | 1  | 10         |
    When the Client does a "PUT" request on "<URI>"
    Then the Client should get a 200 response
    And the Response contains popularity of <POPULARITY>
    Examples:
      | URI              | POPULARITY |
      | /story/1/like    | 11         |
      | /story/1/dislike | 9          |


