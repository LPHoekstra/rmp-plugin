package com.rmp.model;

import java.util.List;

public record TestObject(String prenom, String nom, String age, List<TestObject2> coordinates) {
}
