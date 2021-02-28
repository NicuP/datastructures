package org.datastructures.immutabile.list;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class LinkedList<T> {
    private final Optional<Node<T>> head;

    private LinkedList(Optional<Node<T>> head) {
        this.head = head;
    }

    public static <T> LinkedList<T> empty() {
        return new LinkedList<>(Optional.empty());
    }

    @SafeVarargs
    public static <T> LinkedList<T> of(T... elements) {
        return of(LinkedList.empty(), elements.length - 1, elements);
    }

    private static <T> LinkedList<T> of(LinkedList<T> list, int index, T[] elements) {
        if (index == 0) {
            return list.prepend(elements[0]);
        } else {
            return of(list.prepend(elements[index]), index - 1, elements);
        }
    }

    public LinkedList<T> tail() {
        return new LinkedList<>(head.flatMap(Node::next));
    }

    public Optional<T> head() {
        return head.map(Node::data);
    }

    public boolean isEmpty() {
        return head.isEmpty();
    }

    public int size() {
        return foldLeft(0, (len, e) -> len + 1);
    }

    public LinkedList<T> prepend(T data) {
        Node<T> newNode = Node.forData(data);
        Node<T> newHead = head.map(newNode::withNext).orElse(newNode);
        return new LinkedList<>(Optional.of(newHead));
    }

    public LinkedList<T> prependList(LinkedList<T> list) {
        return list.foldRight(this, LinkedList::prepend);
    }

    public LinkedList<T> append(T data) {
        return foldRight(LinkedList.of(data), LinkedList::prepend);
    }

    public LinkedList<T> appendList(LinkedList<T> list) {
        return foldRight(list, LinkedList::prepend);
    }

    public LinkedList<T> reverse() {
        return foldLeft(LinkedList.empty(), LinkedList::prepend);
    }

    public Optional<T> getAt(int index) {
        if (index < 0) {
            return Optional.empty();
        } else {
            return getAt(0, index, head);
        }
    }

    private Optional<T> getAt(int current, int index, Optional<Node<T>> node) {
        if (current == index) {
            return node.map(Node::data);
        } else {
            return node.flatMap(n -> getAt(current + 1, index, n.next));
        }
    }


    public LinkedList<T> removeAt(int index) {
        if (index < 0) {
            return this;
        } else {
            return removeAt(0, index, head);
        }
    }

    private LinkedList<T> removeAt(int current, int index, Optional<Node<T>> node) {
        if (current == index) {
            return new LinkedList<>(node.flatMap(Node::next));
        } else {
            return node.map(n -> removeAt(current + 1, index, n.next).prepend(n.data)).orElse(this);
        }
    }

    public void forEach(Consumer<T> consumer) {
        forEach(head, consumer);
    }

    private void forEach(Optional<Node<T>> maybeNode, Consumer<T> consumer) {
        maybeNode.ifPresent(node -> {
                    consumer.accept(node.data);
                    forEach(node.next, consumer);
                }
        );
    }


    public <R> R foldLeft(R initial, BiFunction<R, T, R> function) {
        return foldLeft(head, initial, function);
    }

    private <R> R foldLeft(Optional<Node<T>> maybeNode, R initial, BiFunction<R, T, R> function) {
        return maybeNode.map(node -> {
            R partial = function.apply(initial, node.data);
            return foldLeft(node.next, partial, function);
        }).orElse(initial);
    }


    public <R> R foldRight(R initial, BiFunction<R, T, R> function) {
        return foldRight(head, initial, function);
    }

    private <R> R foldRight(Optional<Node<T>> maybeNode, R initial, BiFunction<R, T, R> function) {
        return maybeNode.map(node -> {
            R partial = foldRight(node.next, initial, function);
            return function.apply(partial, node.data);
        }).orElse(initial);
    }

    public LinkedList<T> filter(Predicate<T> predicate) {
        return foldRight(LinkedList.empty(), (list, data) -> predicate.test(data) ? list.prepend(data) : list);
    }

    public <R> LinkedList<R> map(Function<T, R> function) {
        return foldRight(LinkedList.empty(), (list, data) -> list.prepend(function.apply(data)));
    }

    public <R> LinkedList<R> flatMap(Function<T, LinkedList<R>> function) {
        return foldRight(LinkedList.empty(), (list, data) -> list.prependList(function.apply(data)));
    }


    @Override
    public String toString() {
        String string = "[" + foldRight("", (partial, data) -> data + ", " + partial);
        if (string.length() > 2) {
            return string.substring(0, string.length() - 2) + "]";
        } else {
            return "[ ]";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("unchecked")
        LinkedList<T> that = (LinkedList<T>) o;
        return equals(head, that.head);
    }

    private boolean equals(Optional<Node<T>> node1, Optional<Node<T>> node2) {
        if (node1.equals(node2) && node1.isPresent() && node2.isPresent()) {
            return equals(node1.flatMap(Node::next), node2.flatMap(Node::next));
        } else {
            return node1.isEmpty() && node2.isEmpty();
        }
    }

    @Override
    public int hashCode() {
        return foldLeft(13, (acc, i) -> acc + i.hashCode());
    }
}

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class Node<T> {
    final Optional<Node<T>> next;
    final T data;

    private Node(Optional<Node<T>> next, T data) {
        this.next = next;
        this.data = data;
    }

    public Optional<Node<T>> next() {
        return next;
    }

    public T data() {
        return data;
    }

    public Node<T> withNext(Node<T> node) {
        return new Node<>(Optional.of(node), this.data);
    }

    public static <T> Node<T> forData(T data) {
        return new Node<T>(Optional.empty(), data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return data.equals(node.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
