package com.zhenhui.demo.artemis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode
public class Command implements Serializable {

    private static final long serialVersionUID = 100940125994786694L;

    @EqualsAndHashCode.Include
    private String uuid;

    private String type;

    private Map<String, Object> attributes = new HashMap<>();


}
