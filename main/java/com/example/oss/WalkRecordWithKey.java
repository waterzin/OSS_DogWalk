package com.example.oss;

public class WalkRecordWithKey {
    private WalkRecord record;
    private String key;

    public WalkRecordWithKey(WalkRecord record, String key) {
        this.record = record;
        this.key = key;
    }

    public WalkRecord getRecord() { return record; }
    public String getKey() { return key; }
}

