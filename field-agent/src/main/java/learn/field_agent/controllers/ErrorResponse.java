package learn.field_agent.controllers;

import learn.field_agent.domain.Result;
import learn.field_agent.domain.ResultType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponse {
//Response entity = HTTP stuff
    public static <T> ResponseEntity<Object> build(Result<T> result) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; //first error we assume is internal error: our fault
        if (result.getType() == null || result.getType() == ResultType.INVALID) {
            status = HttpStatus.BAD_REQUEST; //when to give bad request: result type is invalid, or there is no result type
        } else if (result.getType() == ResultType.NOT_FOUND) {
            status = HttpStatus.NOT_FOUND; //when to give not found: result type is not found
        }
        return new ResponseEntity<>(result.getMessages(), status); //returns the messages and the status
    }
}
