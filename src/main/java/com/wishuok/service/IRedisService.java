package com.wishuok.service;

import java.io.UnsupportedEncodingException;

public interface IRedisService {

    String testString();

    String testHash();

    String testList() throws UnsupportedEncodingException;

    String testSet();

    String testSortedSet();
}
