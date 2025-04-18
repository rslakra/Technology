package com.rslakra.distributedstore.ds;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VersionedValue {
    
    private String value;
    private long version;
    
}
