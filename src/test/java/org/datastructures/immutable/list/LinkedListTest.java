package org.datastructures.immutable.list;

import org.datastructures.immutabile.list.LinkedList;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class LinkedListTest {

    @Test
    public void testEmpty() {
        var empty = LinkedList.<Integer>empty();
        assertEquals("[ ]", empty.toString());
        assertEquals(Optional.<Integer>empty(), empty.head());
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testOf() {
        var list = LinkedList.of(1, 2, 3);
        assertEquals("[1, 2, 3]", list.toString());
        assertFalse(list.isEmpty());
    }

    @Test
    public void testPrepend() {
        var list = LinkedList.of(2, 3).prepend(1);
        assertEquals(LinkedList.of(1, 2, 3), list);
    }


    @Test
    public void testGetAt() {
        var list = LinkedList.of(1, 2, 3);

        assertEquals(Optional.of(2), list.getAt(1));
        assertEquals(Optional.of(1), list.getAt(0));
        assertEquals(list.head(), list.getAt(0));
        assertEquals(Optional.empty(), list.getAt(-42));
        assertEquals(Optional.empty(), list.getAt(42));
    }

    @Test
    public void testHead() {
        var list = LinkedList.of(1, 2, 3);
        assertEquals(Optional.of(1), list.head());
    }

    @Test
    public void testTail() {
        var list = LinkedList.of(1, 2, 3);
        assertEquals(LinkedList.of(2, 3), list.tail());
    }

    @Test
    public void testForEach() {
        var list = LinkedList.of(1, 2, 3);
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(stringBuilder::append);
        assertEquals("123", stringBuilder.toString());
    }

    @Test
    public void testFoldLeft() {
        var list = LinkedList.of(1, 2, 3);
        int sum = list.reduceLeft(0, Integer::sum);
        assertEquals(6, sum);

        var foldedLeft = list.reduceLeft(LinkedList.empty(), LinkedList::prepend);
        assertEquals(LinkedList.of(3, 2, 1), foldedLeft);
    }

    @Test
    public void testFoldRight() {
        var list = LinkedList.of(1, 2, 3);
        int sum = list.reduceRight(0, Integer::sum);
        assertEquals(6, sum);

        var foldedRight = list.reduceRight(LinkedList.empty(), LinkedList::prepend);
        assertEquals(LinkedList.of(1, 2, 3), foldedRight);
    }

    @Test
    public void testEquals() {
        var list = LinkedList.of(1, 2, 3);
        var tail = LinkedList.of(2, 3);
        assertEquals(tail, list.tail());
        assertNotEquals(tail, list);
        assertEquals(List.of(1, 2, 3, 4), List.of(1, 2, 3, 4));
    }

    @Test
    public void testSize() {
        var list = LinkedList.of(1, 2, 3);
        assertEquals(3, list.size());
        assertEquals(0, LinkedList.empty().size());
    }

    @Test
    public void testAppend() {
        var list = LinkedList.of(1, 2).append(3);
        assertEquals(LinkedList.of(1, 2, 3), list);
        assertEquals(LinkedList.of(1), LinkedList.empty().append(1));
    }

    @Test
    public void testReverse() {
        var list = LinkedList.of(1, 2, 3);
        assertEquals(LinkedList.of(3, 2, 1), list.reverse());
    }

    @Test
    public void testMap() {
        var list = LinkedList.of(1, 2, 3);
        assertEquals(LinkedList.of("1", "2", "3"), list.map(Object::toString));
    }

    @Test
    public void testFilter() {
        var list = LinkedList.of(1, 2, 3, 4);
        assertEquals(LinkedList.of(2, 4), list.filter(i -> i % 2 == 0));
    }

    @Test
    public void testRemoveAt() {
        var list = LinkedList.of(1, 2, 3, 4, 5);

        assertEquals(LinkedList.of(2, 3, 4, 5), list.removeAt(0));
        assertEquals(LinkedList.of(1, 2, 4, 5), list.removeAt(2));
        assertEquals(LinkedList.of(1, 2, 3, 4), list.removeAt(4));
        assertEquals(LinkedList.of(1, 2, 3, 4, 5), list.removeAt(5));
        assertEquals(LinkedList.empty(), LinkedList.of(1).removeAt(0));
        assertEquals(LinkedList.empty(), LinkedList.empty().removeAt(0));
    }

    @Test
    public void testPrependList() {
        var list1 = LinkedList.of(1, 2, 3);
        var list2 = LinkedList.of(4, 5, 6);

        assertEquals(LinkedList.of(1, 2, 3, 4, 5, 6), list2.prependList(list1));
    }

    @Test
    public void testAppendList() {
        var list1 = LinkedList.of(1, 2, 3);
        var list2 = LinkedList.of(4, 5, 6);

        assertEquals(LinkedList.of(1, 2, 3, 4, 5, 6), list1.appendList(list2));
    }

    @Test
    public void testFlatMap() {
        var list = LinkedList.of(1, 3, 5);
        var updatedList = list.flatMap(i -> LinkedList.of(i, i + 1));
        assertEquals(LinkedList.of(1, 2, 3, 4, 5, 6), updatedList);
    }
}
