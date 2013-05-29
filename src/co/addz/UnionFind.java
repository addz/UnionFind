/*
 * UnionFind
 * 
 * Java Implementation of the union-find datastructure for efficiently representing and merging set partitions of
 * a contiguous integer range.
 * 
 * Copyright (c) 2013 Adam Muhlbauer (admuhlbauer@gmail.com, http://addz.co)
 * 
 * Available on Github https://github.com/addz
 */

package co.addz;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the union-find datastructure for efficiently representing and merging set partitions of
 * a contiguous integer set.
 * 
 * @author Adam Muhlbauer (admuhlbauer@gmail.com, http://addz.co)
 */
public class UnionFind
{
	private int data[];
	private int size[];
	
	/**
	 * Create a new {@link UnionFind} instance.
	 * @param elements the number of elements to represent by the set partition.
	 */
	public UnionFind(int elements)
	{
		data = new int[elements];
		size = new int[elements];
		
		for (int i = 0; i < elements; i++)
		{
			data[i] = i;
			size[i] = 1;
		}
	}
	
	/**
	 * Find the root set index for the given element.
	 * 
	 * @param n the element to search for.
	 * @return the root set element. If the parameter is a singleton set, the value returned will be the same as the parameter.
	 */
	public int find(int n)
	{
		// shift for 0-indexing internally.
		return findIndex(n) + 1;
	}
	
	/**
	 * Check if two elements belong to the same set partition.
	 * 
	 * @param i first element
	 * @param j second element
	 * @return true if both {@code i} and {@code j} are in the same set partition.
	 */
	public boolean isInSameSet(int i, int j)
	{
		return (find(i) == find(j));
	}
	
	/**
	 * Check if two elements are disjoint (i.e., in different set partitions)
	 * 
	 * @param i first element
	 * @param j second element
	 * @return true if {@code i} and {@code j} are in different partitions
	 */
	public boolean isDisjoint(int i, int j)
	{
		return !isInSameSet(i, j);
	}
	
	/**
	 * Count the number of set partitions represented by this structure.
	 * 
	 * @return the number of distinct set partitions.
	 */
	public int countSets()
	{
		BitSet s = new BitSet();
		int count = 0;
		
		for (int i = 0; i < data.length; i++)
		{
			if (!s.get(data[i]))
			{
				count++;
				s.set(data[i]);
			}
		}
		return count;		
	}
	
	@Override
	public String toString()
	{
		return super.toString() + " " + countSets() +  " sets (" + getSets() + ")";
	}
	
	/**
	 * Find the root index of a given element by a recursive search.
	 * 
	 * @param n element
	 * @return index within {@link UnionFind#data}.
	 */
	private int findIndex(int n)
	{
		if (n < 1 || n > data.length)
			throw new IllegalArgumentException("Invalid set item");
		
		int i = n - 1; // offset to array-index.
		
		if (i == data[i])
			return i;
	
		return findIndex(data[i] + 1);
	}
	
	public void union(int i, int j)
	{
		int r1 = findIndex(i);
		int r2 = findIndex(j);
	
		// merge smaller tree into bigger tree
		if (size[r1] >= size[r2])
		{
			size[r1] += size[r2];
			data[r2] = r1;
		}
		else
		{
			size[r2] += size[r1];
			data[r1] = r2;
		}
	}
	
	/**
	 * build and return a representation of the set partition in terms of {@link Set} instances.
	 * 
	 * @return a full representation of the set partition.
	 */
	public Collection<Set<Integer>> getSets()
	{
		Map<Integer, Set<Integer>> sets = new HashMap<Integer, Set<Integer>>();
		
		for (int i = 1; i <= data.length; i++)
		{
			Integer key = Integer.valueOf(findIndex(i));
			Set<Integer> items = sets.get(key);
		
			if (items == null)
				items = new HashSet<Integer>();
			
			items.add(Integer.valueOf(i));
			sets.put(key, items);
		}
		
		return sets.values();
	}
}
