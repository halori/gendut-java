package org.gendut.collection;


import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;

import org.gendut.iterator.ForwardIterator;

import junit.framework.TestCase;


public class HashMapAsTupleTest extends TestCase {
  
  public static final class Attribute<V> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final V value;
    
    V getValue() {
      return value;
    }

    public static<V> Attribute<V> createDesignator(String name) {
      return new Attribute<V>(name);
    }
    
    private Attribute(String name) {
      this.name = name.intern();
      this.value = null;
    }
    
    private Attribute(String name, V value) {
      this.name = name.intern();
      this.value = value;
    }

    public String getName() {
      return name;
    }

    Attribute<V> is(V value) {
      return new Attribute<V>(name, value);
    }

    public V of(Tuple tuple) {
      return tuple.get(this);
    }

    @Override
    public int hashCode() {
      return System.identityHashCode(name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Attribute other = (Attribute) obj;
      return name.equals(other.name);
    }
  }
  
  static public final class Tuple implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private volatile long hashValue = Long.MAX_VALUE;
    
    private final ImmutableHashMap<Attribute<?>, Object> map;
    
    private static final Tuple emptyTuple = new Tuple();
    

    private Tuple() {
      map = ImmutableHashMap.create();
    }
    
    private Tuple(ImmutableHashMap<Attribute<?>, Object> map) {
      this.map = map;
    }

    @SuppressWarnings("unchecked")
    public<V> V get(Attribute<V> attribute) {
      return (V) map.get(attribute);
    }
    
    static Tuple create(Attribute<?>[] e) {
      if (e.length == 0)
        return emptyTuple;
      ImmutableHashMap<Attribute<?>, Object> map = ImmutableHashMap.create();
      for (int i = 0; i < e.length; i++)
        map = map.put(e[i], e[i].getValue());
      return new Tuple(map);
    }

    static Tuple create(Attribute<?> e0) {
      Attribute<?>[] e = {e0};
      return create(e);
    }
    
    static Tuple create(Attribute<?> e0, Attribute<?> e1) {
      Attribute<?>[] e = {e0, e1};
      return create(e);
    }
    
    static Tuple create(Attribute<?> e0, Attribute<?> e1, Attribute<?> e2) {
      Attribute<?>[] e = {e0,e1,e2};
      return create(e);
    }
    
    static Tuple create(Attribute<?> e0, Attribute<?> e1, Attribute<?> e2, Attribute<?> e3) {
      Attribute<?>[] e = {e0,e1,e2,e3};
      return create(e);
    }
    
    static Tuple create(Attribute<?> e0, Attribute<?> e1, Attribute<?> e2, Attribute<?> e3, Attribute<?> e4) {
      Attribute<?>[] e = {e0,e1,e2,e3,e4};
      return create(e);
    }
    
    static Tuple create(Attribute<?> e0, Attribute<?> e1, Attribute<?> e2, Attribute<?> e3, Attribute<?> e4, Attribute<?> e5) {
      Attribute<?>[] e = {e0,e1,e2,e3,e4,e5};
      return create(e);
    }

    static Tuple create(Attribute<?> e0, Attribute<?> e1, Attribute<?> e2, Attribute<?> e3, Attribute<?> e4, Attribute<?> e5, Attribute<?> e6) {
      Attribute<?>[] e = {e0,e1,e2,e3,e4,e5,e6};
      return create(e);
    }

    static Tuple create(Attribute<?> e0, Attribute<?> e1, Attribute<?> e2, Attribute<?> e3, Attribute<?> e4, Attribute<?> e5, Attribute<?> e6, Attribute<?> e7) {
      Attribute<?>[] e = {e0,e1,e2,e3,e4,e5,e6,e7};
      return create(e);
    }
    
    @Override
    public int hashCode() {
      if (hashValue == Long.MAX_VALUE) {
        hashValue = map.hashCode();
      }
      return (int) hashValue;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Tuple other = (Tuple) obj;
      
      if (this.map.keys().equals(other.map.keys()))
        return this.map.equals(other.map);
      else
        return false;
    }
    
    @Override
    public String toString() {
      TreeMap<String, Object> str = new TreeMap<String, Object>();
      ForwardIterator<Attribute<?>> iter = map.keyIterator();
      while (iter.hasNext()) {
        Attribute<?> a = iter.next();
        str.put(a.getName(), map.get(a));
      }
      
      StringBuffer buf = new StringBuffer();
      boolean first = true;
      buf.append('(');
      for (String a : str.keySet()) {
        if (!first)
          buf.append(", ");
        else
          first = false;
        buf.append(a);
        Object val = str.get(a);
        buf.append("->");
        if (val != null)
          buf.append(val.toString());
        else
          buf.append("<null>");
      }
      buf.append(')');
      return buf.toString();
    }
    
    public Tuple set(Attribute<?> attribute) {
      ImmutableHashMap<Attribute<?>, Object> newMap = map.put(attribute, attribute.getValue());
      return new Tuple(newMap);
    }
  }
  
  static Attribute<String> Name = Attribute.createDesignator("Name");
  static Attribute<Integer> Year = Attribute.createDesignator("Year");
  static Attribute<Integer> Salary = Attribute.createDesignator("Salary");
  
  public void testCreation() {
    Tuple person = Tuple.create(Name.is("Heinz"),Year.is(1965));
    
    assertEquals("Heinz", person.get(Name));
    assertEquals(Integer.valueOf(1965), person.get(Year));
    
    Attribute<?>[] entries = {Name.is("Wolfgang"),Year.is(1962)};
    Tuple person2 = Tuple.create(entries);
    
    assertEquals("Wolfgang", Name.of(person2));
    assertEquals(Integer.valueOf(1962), Year.of(person2));
    assertEquals("(Name->Wolfgang, Year->1962)", person2.toString());
  }
  
  public void testEquals() { 
    Tuple heinz = Tuple.create(Name.is("Heinz"),Year.is(1965));
    Tuple wolfgang = Tuple.create(Name.is("Wolfgang"),Year.is(1962));
    Tuple heinzClone = Tuple.create(Name.is("Heinz"),Year.is(1965));
    Tuple wolfgangTheYounger = Tuple.create(Name.is("Wolfgang"),Year.is(1982));
    
    assertTrue(heinz.equals(heinzClone));
    assertFalse(heinz.equals(wolfgang));
    assertFalse(heinz.equals(wolfgangTheYounger));
    assertEquals(heinz.hashCode(),heinzClone.hashCode());
  }
  
  
  public void testSet() {
    Tuple heinz = Tuple.create(Name.is("Heinz"),Year.is(1965));
    Tuple wolfgang = heinz.set(Name.is("Wolfgang"));
    assertEquals("(Name->Wolfgang, Year->1965)", wolfgang.toString());
    wolfgang = wolfgang.set(Year.is(null));
    assertEquals("(Name->Wolfgang, Year-><null>)", wolfgang.toString());
  }
  
  static class Person {
    String name;
    int year;
    int salary;
    public Person(String name, int year, int salary) {
      this.name = name;
      this.year = year;
      this.salary = salary;
    }
  }
  
  static int N = 100000;
  
  public void testManyTuples() {
    Tuple[] person = new Tuple[N];
    for (int i = 0; i < N; i++) {
      Tuple heinz = Tuple.create(Name.is("Heinz"+i),Year.is(1965),Salary.is(i));
      person[i] = heinz;
      assertNotNull(heinz);
    }
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testManyHashmaps() {
    
    HashMap[] person = new HashMap[N]; 
    for (int i = 0; i < N; i++) {
      HashMap heinz = new HashMap(7);
      heinz.put(Name, "Heinz");
      heinz.put(Year, 1965);
      heinz.put(Salary, i);
      person[i] = heinz;
      assertNotNull(heinz);
    }
  }
  
  public void testManyPojos() {
    Person[] person = new Person[N];
    for (int i = 0; i < N; i++) {
      Person heinz = new Person("Heinz"+i,1965,i);
      person[i] = heinz;
      assertNotNull(heinz);
    }
  }
}
