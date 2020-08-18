package com.example.picturemaker.support;

@FunctionalInterface
public interface Function2<One, Two> {
    public void apply(One one, Two two);
}