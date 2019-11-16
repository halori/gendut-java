package org.gendut.collection;

public interface MonoidMap<M, N> {
  N map(M a); 
  N add(N x, N y);
  N zero();
}
