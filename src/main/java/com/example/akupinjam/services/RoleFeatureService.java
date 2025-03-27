package com.example.akupinjam.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.akupinjam.exceptions.ResourceNotFoundException;
import com.example.akupinjam.models.Feature;
import com.example.akupinjam.models.Role;
import com.example.akupinjam.models.RoleFeature;
import com.example.akupinjam.repositories.RoleFeatureRepository;

@Service
public class RoleFeatureService {
    @Autowired
    private RoleFeatureRepository roleFeatureRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private FeatureService featureService;

    public List<RoleFeature> getAlRoleFeatures() {
        return roleFeatureRepository.findAll();
    }

    public RoleFeature getRoleFeatureById(String id) {
        return roleFeatureRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Role Feature not found!"));
    }

    public RoleFeature createRoleFeature(Map<String, Object> payload) {
        String roleId = Objects.toString(payload.get("role_id"), "").trim();
        String featureId = Objects.toString(payload.get("feature_id"), "").trim();
        Role role = roleService.getRoleById(roleId);
        Feature feature = featureService.getFeatureById(featureId);

        RoleFeature roleFeature = new RoleFeature(null, role, feature);
        return roleFeatureRepository.save(roleFeature);
    }

    public void deleteRoleFeature(String id) {
        roleFeatureRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Role Feature not found!"));

        roleFeatureRepository.deleteById(UUID.fromString(id));
    }
}
