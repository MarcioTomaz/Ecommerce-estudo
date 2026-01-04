package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.Service.ReturnProcessService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/returnProcess")
public class ReturnProcessController {


    @Autowired
    private ReturnProcessService returnProcessService;

    @PostMapping("/return")
    public ResponseEntity<JsonNode> returnProcess(@RequestParam JsonNode returnProcessFields) {

        Object result = returnProcessService.returnProcess(returnProcessFields);


        return new ResponseEntity<>(returnProcessFields, HttpStatus.OK);
    }
}
