package edu.prz.hopsops.foundation.domain;

public interface StandardFactory<I, T> {

  T create(I input);
}
