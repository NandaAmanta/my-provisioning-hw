/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voxloud.provisioning.exception;

import com.voxloud.provisioning.entity.Device;

/**
 *
 * @author nansa
 */
public class InvalidDeviceModelException extends RuntimeException {
    public InvalidDeviceModelException(Device device) {
        super(device.getMacAddress() + " has invalid device model");
    }
}