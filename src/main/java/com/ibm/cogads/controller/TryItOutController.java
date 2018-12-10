package com.ibm.cogads.controller;

import com.ibm.cogads.domain.model.exceptions.ResponseException;
import com.ibm.cogads.domain.service.AssistantService;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/assistant")
@Api(description = "Set of endpoints for trying out the the Watson Assistant")
public class TryItOutController {

    @Autowired
    private AssistantService assistantService;

    @ApiOperation(value = "Call Watson Assistant with Question", response = MessageResponse.class)
    @RequestMapping(value = "/{projectID}", method= RequestMethod.GET)
    public ResponseEntity<?> conversation(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "projectID") String projectID,
            @RequestParam(value = "text") String text){

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status;

        try {
            assistantService.checkToken(token);
            MessageResponse messageResponse = assistantService.converse(projectID, text);
            status = HttpStatus.OK;
            return new ResponseEntity<MessageResponse>(messageResponse, headers, status);
        }
        catch (CredentialException c){
            status = HttpStatus.UNAUTHORIZED;
            ResponseException responseException = new ResponseException(status.toString(), c.getLocalizedMessage());
            return new ResponseEntity<ResponseException>(responseException, headers, status);
        }

        catch(Exception e){
            status = HttpStatus.CONFLICT;
            ResponseException responseException = new ResponseException(status.toString(), e.getMessage());
            return new ResponseEntity<ResponseException>(responseException, headers, status);
        }
    }

}
