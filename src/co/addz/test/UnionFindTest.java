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

package co.addz.test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import co.addz.UnionFind;

/**
 *  Tests for {@link UnionFind}.
 * 
 *  @author Adam Muhlbauer (admuhlbauer@gmail.com, http://addz.co)
 */
public class UnionFindTest
{
	private static final int SIZE = 10;
	
	/** object under test. */
	private UnionFind unionFindUnderTest;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setup()
	{
		unionFindUnderTest = new UnionFind(SIZE);
	}
	
	@Test
	public void canInitToGivenSize() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		Assert.assertEquals(SIZE, unionFindUnderTest.getSets().size());
	}
	
	@Test
	public void canInitToSingletonSets() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException 
	{
		int data[] = getInternalDataField();
		for (int i = 0; i < data.length; i++)
		{
			Assert.assertEquals(i, data[i]);
		}
	}
	
	@Test
	public void canUnionSingleSets()
	{
		unionFindUnderTest.union(1, 2);
		testContentsOfSets(buildUnionedSets(new int[][]{{1,2}}));
	}
	
	
	@Test
	public void canUnionMultiItemSets()
	{
		unionFindUnderTest.union(1, 2);
		unionFindUnderTest.union(2, 8);
		
		testContentsOfSets(buildUnionedSets(new int[][]{{1,2,8}}));
	}
	
	@Test
	public void canPartitionSpace()
	{
		unionFindUnderTest.union(1, 2);
		unionFindUnderTest.union(2, 3);
		unionFindUnderTest.union(3, 8);
		unionFindUnderTest.union(8, 4);
		
		unionFindUnderTest.union(6, 5);
		unionFindUnderTest.union(9, 10);
		unionFindUnderTest.union(7, 9);
		unionFindUnderTest.union(5, 9);
		
		testContentsOfSets(buildUnionedSets(new int[][]{{1,2,3,4,8}, {10,9,7,5,6}}));	
	}
	
	private void testContentsOfSets(Collection<Set<Integer>> expectedJoins)
	{
		Collection<Set<Integer>> all = unionFindUnderTest.getSets();
		
		for (Set<Integer> set : expectedJoins)
		{
			Assert.assertTrue(all.contains(set));
			all.remove(set);
		}
		
		for (Set<Integer> s : all)
		{
			Assert.assertEquals(1, s.size());
		}
		
	}
	
	@Test
	public void unionOutOfRange()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid set item");
		
		unionFindUnderTest.union(2, 1022);
	}
	
	@Test
	public void findOutOfRange()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid set item");
		
		unionFindUnderTest.find(110103);
	}
	
	@Test
	public void findSetOwnerTest()
	{
		for (int i = 1; i <= SIZE; i++)
			Assert.assertEquals(i, unionFindUnderTest.find(i));
		
		unionFindUnderTest.union(1, 2);
		unionFindUnderTest.union(3, 4);
		
		Assert.assertEquals(1, unionFindUnderTest.find(2));
		Assert.assertEquals(3, unionFindUnderTest.find(4));
		
		unionFindUnderTest.union(1, 3);
		
		Assert.assertEquals(1, unionFindUnderTest.find(4));
		Assert.assertEquals(1, unionFindUnderTest.find(2));
		Assert.assertEquals(1, unionFindUnderTest.find(3));
	}
	
	@Test
	public void canCountSets()
	{
		Assert.assertEquals(10, unionFindUnderTest.countSets());

		for (int i = 2; i < SIZE; i++)
		{
			unionFindUnderTest.union(1, i);
			Assert.assertEquals(SIZE - i + 1, unionFindUnderTest.countSets());
		}
	}
		
	private Collection<Set<Integer>> buildUnionedSets(int items[][])
	{
		List<Set<Integer>> s = new ArrayList<Set<Integer>>();
		
		for (int i = 0; i < items.length; i++)
		{
			Set<Integer> set = new HashSet<Integer>();
			
			for (int j = 0; j < items[i].length; j++)
				set.add(Integer.valueOf(items[i][j]));
			
			s.add(set);
		}
			
		return s;
	}		
	
	private int[] getInternalDataField() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		java.lang.reflect.Field f = unionFindUnderTest.getClass().getDeclaredField("data");
		f.setAccessible(true);
		
		return (int[]) f.get(unionFindUnderTest);
	}
}
