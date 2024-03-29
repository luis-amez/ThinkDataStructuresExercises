package com.allendowney.thinkdast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Provides sorting algorithms.
 *
 */
public class ListSorter<T> {

    /**
     * Sorts a list using a Comparator object.
     *
     * @param list
     * @param comparator
     * @return
     */
    public void insertionSort(List<T> list, Comparator<T> comparator) {

        for (int i=1; i < list.size(); i++) {
            T elt_i = list.get(i);
            int j = i;
            while (j > 0) {
                T elt_j = list.get(j-1);
                if (comparator.compare(elt_i, elt_j) >= 0) {
                    break;
                }
                list.set(j, elt_j);
                j--;
            }
            list.set(j, elt_i);
        }
    }

    /**
     * Sorts a list using a Comparator object.
     *
     * @param list
     * @param comparator
     * @return
     */
    public void mergeSortInPlace(List<T> list, Comparator<T> comparator) {
        List<T> sorted = mergeSort(list, comparator);
        list.clear();
        list.addAll(sorted);
    }

    /**
     * Sorts a list using a Comparator object.
     *
     * Returns a list that might be new.
     *
     * @param list
     * @param comparator
     * @return
     */
    public List<T> mergeSort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return list;
        }

        int halfIndex = list.size() / 2;
        List<T> first = mergeSort(new LinkedList<T>(list.subList(0, halfIndex)), comparator);
        List<T> second = mergeSort(new LinkedList<T>(list.subList(halfIndex, list.size())), comparator);

        return merge(first, second, comparator);
    }

    /**
     * Merges two sorted lists into a single sorted list.
     *
     * @param first
     * @param second
     * @param comparator
     * @return
     */
    private List<T> merge(List<T> first, List<T> second, Comparator<T> comparator) {
        // NOTE: using LinkedList is important because we need to
        // remove from the beginning in constant time
        List<T> result = new LinkedList<T>();
        int total = first.size() + second.size();
        for (int i=0; i<total; i++) {
            List<T> winner = pickWinner(first, second, comparator);
            result.add(winner.remove(0));
        }
        return result;
    }

    /**
     * Returns the list with the smaller first element, according to `comparator`.
     *
     * If either list is empty, `pickWinner` returns the other.
     *
     * @param first
     * @param second
     * @param comparator
     * @return
     */
    private List<T> pickWinner(List<T> first, List<T> second, Comparator<T> comparator) {
        if (first.size() == 0) {
            return second;
        }
        if (second.size() == 0) {
            return first;
        }
        int res = comparator.compare(first.get(0), second.get(0));
        if (res < 0) {
            return first;
        }
        if (res > 0) {
            return second;
        }
        return first;
    }

    /**
     * Sorts a list using a Comparator object.
     *
     * @param list
     * @param comparator
     * @return
     */
    public void heapSort(List<T> list, Comparator<T> comparator) {
        PriorityQueue<T> heap = new PriorityQueue<>(list.size(), comparator);
        heap.addAll(list);
        list.clear();
        while (!heap.isEmpty()) {
            list.add(heap.poll());
        }
    }


    /**
     * Returns the largest `k` elements in `list` in ascending order.
     *
     * @param k
     * @param list
     * @param comparator
     * @return
     * @return
     */
    public List<T> topK(int k, List<T> list, Comparator<T> comparator) {
        PriorityQueue<T> heap = new PriorityQueue<>(k, comparator);
        for (T element : list) {
            if (heap.size() < k) {
                heap.offer(element);
            } else if (comparator.compare(element, heap.peek()) > 0) {
                heap.poll();
                heap.offer(element);
            }
        }
        List<T> topKList = new ArrayList<>();
        while (!heap.isEmpty()) {
            topKList.add(heap.poll());
        }
        return topKList;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>(Arrays.asList(3, 5, 1, 4, 2));

        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer elt1, Integer elt2) {
                return elt1.compareTo(elt2);
            }
        };

        ListSorter<Integer> sorter = new ListSorter<Integer>();
        sorter.insertionSort(list, comparator);
        System.out.println(list);

        list = new ArrayList<Integer>(Arrays.asList(3, 5, 1, 4, 2));
        sorter.mergeSortInPlace(list, comparator);
        System.out.println(list);

        list = new ArrayList<Integer>(Arrays.asList(3, 5, 1, 4, 2));
        sorter.heapSort(list, comparator);
        System.out.println(list);

        list = new ArrayList<Integer>(Arrays.asList(6, 3, 5, 8, 1, 4, 2, 7));
        List<Integer> queue = sorter.topK(4, list, comparator);
        System.out.println(queue);
    }
}