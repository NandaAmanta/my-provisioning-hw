package com.voxloud.provisioning.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.exception.InvalidDeviceModelException;
import com.voxloud.provisioning.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class ProvisioningServiceImpl implements ProvisioningService {

    @Autowired
    private DeviceRepository deviceRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${provisioning.domain}")
    private String domain;

    @Value("${provisioning.port}")
    private String port;

    @Value("${provisioning.codecs}")
    private String codecs;

    @Override
    public String getProvisioningFile(String macAddress) {
        Device device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new DeviceNotFoundException(macAddress));

        switch (device.getModel()){
            case DESK:
                return this.generateDeskDeviceConfig(device);
            case CONFERENCE:
                return this.generateConferenceDeviceConfig(device);
            default:
                throw new InvalidDeviceModelException(device);
        }
    }

    private String generateDeskDeviceConfig(Device device) {
        Properties properties = new Properties();
        properties.setProperty("username", device.getUsername());
        properties.setProperty("password", device.getPassword());

        if (device.getOverrideFragment() != null) {
            try {
                properties.load(new StringReader(device.getOverrideFragment()));
            } catch (IOException e) {
                throw new RuntimeException("Error loading properties from override fragment", e);
            }
        }

        properties.putIfAbsent("domain", domain);
        properties.putIfAbsent("port", port);
        properties.putIfAbsent("codecs", codecs);
        return properties.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
    }

    private String generateConferenceDeviceConfig(Device device) {
        try {
            Map<String, Object> config = objectMapper.readValue(device.getOverrideFragment(), Map.class);
            config.put("username", device.getUsername());
            config.put("password", device.getPassword());

            config.putIfAbsent("domain", domain);
            config.putIfAbsent("port", port);
            config.putIfAbsent("codecs", codecs.split(","));

            return objectMapper.writeValueAsString(config);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON override fragment", e);
        }
    }

}
