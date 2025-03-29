package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProvisioningController{

    // TODO Implement controller method
    
    @Autowired
    private ProvisioningService provisioningService;
    
    @GetMapping("/{macAddress}")
    public String getProvisioning(@PathVariable String macAddress){
        return this.provisioningService.getProvisioningFile(macAddress);
    }
}