package com.zdf.worker.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NftTaskContext {
    private Object[] params;
    private Object[] envs;
    private Class<?>[] clazz;
}
