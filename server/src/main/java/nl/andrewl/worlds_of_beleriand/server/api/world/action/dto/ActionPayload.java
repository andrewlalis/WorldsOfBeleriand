package nl.andrewl.worlds_of_beleriand.server.api.world.action.dto;

import java.util.Map;

public record ActionPayload(String name, Map<String, String> options) {}
