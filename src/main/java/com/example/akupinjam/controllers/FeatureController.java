package com.example.akupinjam.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.akupinjam.models.Feature;
import com.example.akupinjam.repositories.FeatureRepository;

@RestController
@RequestMapping("/api/v1/features")
public class FeatureController {
    @Autowired
    private FeatureRepository featureRepository;

    @GetMapping
    public List<Feature> getAllFeatures() {
        return featureRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feature> getFeatureById(@PathVariable int id) {
        Optional<Feature> feature = featureRepository.findById(id);
        return feature.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Feature> createFeature(@RequestBody Feature feature) {
        if (featureRepository.count() == 0) {
            Feature savedFeature = featureRepository.save(feature);
            return ResponseEntity.ok(savedFeature);
        } else if (!featureRepository.existsByName(feature.getName())) {
            Feature savedFeature = featureRepository.save(feature);
            return ResponseEntity.ok(savedFeature);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable int id) {
        Optional<Feature> feature = featureRepository.findById(id);
        if (feature.isPresent()) {
            featureRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
