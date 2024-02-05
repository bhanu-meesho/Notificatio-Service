package org.check1.controller;

import org.check1.entities.BadRequestException;
import org.check1.entities.SuccessResponse;
import org.check1.entities.sendsmsentities.SmsRequestBody;
import org.check1.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class SendSmsController
{
    @Autowired
    private SmsService smsService;

    @GetMapping("")
    public String welcome1() throws Exception
    {
        return "Welcome to SMS service";
    }

    @PostMapping("/sms/send")
    public ResponseEntity<SuccessResponse> sendSms(@Valid @RequestBody SmsRequestBody smsRequestBody) throws Exception
    {
        return new ResponseEntity<>(new SuccessResponse(smsService.send(smsRequestBody)), HttpStatus.CREATED);
    }

    @GetMapping("/sms/{request}")
    public ResponseEntity<SuccessResponse> findSmsData(@Valid @PathVariable String request) throws Exception
    {
        try{
            Long requestId = Long.parseLong(request);
            return new ResponseEntity<>(new SuccessResponse(smsService.find(requestId)),HttpStatus.CREATED);
        }
        catch(Exception e)
        {
            throw new BadRequestException("Invalid Request id");
        }
    }
}
