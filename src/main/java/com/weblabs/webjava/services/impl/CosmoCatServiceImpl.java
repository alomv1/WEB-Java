package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.annotation.CheckFeature;
import com.weblabs.webjava.services.CosmoCatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmoCatServiceImpl implements CosmoCatService {

    @Override
    @CheckFeature("cosmoCats")
    public List<String> getCosmoCats() {
        return List.of("Murzik The Astronaut", "Barsik The Alien", "Elon Musk's Cat");
    }

    @Override
    @CheckFeature("kittyProducts")
    public String getKittyProducts() {
        return "Space Whiskas (Now with Moon dust!)";
    }
}