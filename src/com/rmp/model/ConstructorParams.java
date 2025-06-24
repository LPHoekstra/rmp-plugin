package com.rmp.model;

import java.util.List;

public record ConstructorParams(List<String> ParamList, List<Class<?>> ParamTypeList) {
}
