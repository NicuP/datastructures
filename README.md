# Data structures
 
A repository with common data structures implemented in Java
 
1. Immutable LinkedList

- does not use mutable state or null in implementation; each operation returns a new list 

Operations:
```
class LinkedList<T> {

  static <T> LinkedList<T> empty()
  
  static <T> LinkedList<T> of(T... elements)           

  
  LinkedList<T> prepend(T element)
  
  LinkedList<T> append(T element)
  
  Optional<T> head()
  
  LinkedList<T> tail()
  
  Optional<T> getAt(int index)
  
  LinkedList<T> removeAt(index)
  
  <R> LinkedList<R> reduceLeft(R initial, BiFunction<R, T, R> function)
  
  <R> LinkedList<R> reduceLeft(R initial, BiFunction<R, T, R> function)
  
  <R> LinkedList<R> map(Function<T, R> function)
  
  LinkedList<T> filter(Predicate<T> predicate)
  
  <R> LinkedList<R> flatMap(Function<T, LinkedList<R>> function)
  
  LinkedList<T> prependList(LinkedList<T> list)
  
  LinkedList<T> appendList(LinkedList<T> list)
  
  LinkedList<T> reverse()    

  String toString()
}
``` 
