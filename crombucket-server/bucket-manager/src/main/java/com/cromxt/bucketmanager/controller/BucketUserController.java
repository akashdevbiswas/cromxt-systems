package com.cromxt.bucketmanager.controller;


import com.cromxt.common.crombucket.bucketmanager.UsersBucketInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/clients")
public class BucketUserController {


    @GetMapping(value = "/{clientId}")
    public UsersBucketInfo getUserBucketInformation(@PathVariable String clientId,
                                                    @RequestParam String groupId) {
        return null;
    }

}
