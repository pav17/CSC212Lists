package edu.smith.cs.csc212.lists;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import me.jjfoley.adt.ListADT;
import me.jjfoley.adt.errors.BadIndexError;
import me.jjfoley.adt.errors.EmptyListError;


@SuppressWarnings("javadoc")
public class ChunkyArrayListTest {
	
	/**
	 * Make a new empty list.
	 * @return an empty list to be tested.
	 */
	private <T> ListADT<T> makeEmptyList() {
		return new ChunkyArrayList<>(3);
	}
	
	/**
	 * Helper method to make a full list.
	 * @return [a, b, c, d] - a small, predictable list for many tests.
	 */
	private ListADT<String> makeABCDList() {
		ListADT<String> data = makeEmptyList();
		data.addBack("a");
		data.addBack("b");
		data.addBack("c");
		data.addBack("d");
		return data;
	}
		
	@Test
	public void testEmpty() {
		ListADT<String> data = makeEmptyList();
		Assert.assertEquals(0, data.size());
		Assert.assertEquals(true, data.isEmpty());
	}
	
	@Test(expected=EmptyListError.class)
	public void testRemoveFrontCrash() {
		ListADT<String> data = makeEmptyList();
		data.removeFront();
	}
	
	@Test(expected=EmptyListError.class)
	public void testRemoveBackCrash() {
		ListADT<String> data = makeEmptyList();
		data.removeBack();
	}
	
	@Test(expected=EmptyListError.class)
	public void testRemoveIndexCrash() {
		ListADT<String> data = makeEmptyList();
		data.removeIndex(3);
	}

	@Test
	public void testAddToFront() {
		ListADT<String> data = makeEmptyList();
		Assert.assertEquals(true, data.isEmpty());
		data.addFront("1");
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("1", data.getIndex(0));
		Assert.assertEquals(false, data.isEmpty());
		data.addFront("0");
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("0", data.getIndex(0));
		Assert.assertEquals("1", data.getIndex(1));
		Assert.assertEquals(false, data.isEmpty());
		data.addFront("-1");
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("-1", data.getIndex(0));
		Assert.assertEquals("0", data.getIndex(1));
		Assert.assertEquals("1", data.getIndex(2));
		Assert.assertEquals(false, data.isEmpty());
		data.addFront("-2");
		Assert.assertEquals("-1", data.getIndex(1));
		Assert.assertEquals("-2", data.getIndex(0));
		Assert.assertEquals("0", data.getIndex(2));
		Assert.assertEquals("1", data.getIndex(3));
		Assert.assertEquals(false, data.isEmpty());
	}
	
	@Test
	public void testAddToBack() {
		ListADT<String> data = makeEmptyList();
		data.addBack("1");
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("1", data.getIndex(0));
		data.addBack("0");
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("0", data.getIndex(1));
		Assert.assertEquals("1", data.getIndex(0));
		data.addBack("-1");
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("-1", data.getIndex(2));
		Assert.assertEquals("0", data.getIndex(1));
		Assert.assertEquals("1", data.getIndex(0));
		data.addBack("-2");
		Assert.assertEquals("-2", data.getIndex(3));
		Assert.assertEquals("-1", data.getIndex(2));
		Assert.assertEquals("0", data.getIndex(1));
		Assert.assertEquals("1", data.getIndex(0));
	}
	
	@Test
	public void testAddBackFull() {
		ListADT<Integer> items = makeEmptyList();
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			items.addBack((i+1)*3);
			Assert.assertEquals(i+1, items.size());
			Assert.assertEquals((i+1)*3, (int) items.getBack()); 
		}
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			Assert.assertEquals((i+1)*3, (int) items.getIndex(i)); 
		}
	}
	
	@Test
	public void testAddFrontFull() {
		final int N = GrowableList.START_SIZE * 5;
		ListADT<Integer> items1 = makeEmptyList();
		for (int i = 0; i < N; i++) {
			items1.addBack((i + 1) * 3);
			Assert.assertEquals(i + 1, items1.size());
			Assert.assertEquals((i + 1) * 3, (int) items1.getBack());
		}
		ListADT<Integer> items2 = makeEmptyList();
		int found = 0;
		while (!items1.isEmpty()) {
			items2.addFront(items1.removeBack());
			if (found++ > N) {
				throw new AssertionError("Something wrong with isEmpty, I think.");
			}
		}
		for (int i = 0; i < GrowableList.START_SIZE * 5; i++) {
			Assert.assertEquals((i + 1) * 3, (int) items2.getIndex(i));
		}
	}
	
	private void insertSorted(ListADT<Integer> items, int num) {
		for (int i=0; i<items.size(); i++) {
			if (items.getIndex(i) >= num) {
				items.addIndex(i, num);
				return;
			}
		}
		items.addBack(num);
	}
	
	@Test
	public void testAddIndexMany() {
		ListADT<Integer> items1 = makeEmptyList();
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			items1.addBack((i+1)*3);
			Assert.assertEquals(i+1, items1.size());
			Assert.assertEquals((i+1)*3, (int) items1.getBack()); 
		}
		
		Random rand = new Random(13);
		ListADT<Integer> items2 = makeEmptyList();
		
		// If this test runs forever, make sure removeIndex actually removes things.
		while(!items1.isEmpty()) {
			int value = items1.removeIndex(rand.nextInt(items1.size()));
			insertSorted(items2, value);
		}
		
		
		for (int i=0; i<GrowableList.START_SIZE*5; i++) {
			Assert.assertEquals((i+1)*3, (int) items2.getIndex(i)); 
		}
	}
	
	@Test
	public void testRemoveFront() {
		ListADT<String> data = makeABCDList();
		Assert.assertEquals(4, data.size());
		Assert.assertEquals("a", data.removeFront());
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("b", data.removeFront());
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("c", data.removeFront());
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("d", data.removeFront());
		Assert.assertEquals(0, data.size());
	}
	
	@Test
	public void testRemoveBack() {
		ListADT<String> data = makeABCDList();
		Assert.assertEquals(4, data.size());
		Assert.assertEquals("d", data.removeBack());
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("c", data.removeBack());
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("b", data.removeBack());
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("a", data.removeBack());
		Assert.assertEquals(0, data.size());
	}
	
	@Test
	public void testRemoveIndex() {
		ListADT<String> data = makeABCDList();
		Assert.assertEquals(4, data.size());
		Assert.assertEquals("c", data.removeIndex(2));
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("d", data.removeIndex(2));
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("b", data.removeIndex(1));
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("a", data.removeIndex(0));
		Assert.assertEquals(0, data.size());
	}
	
	@Test
	public void testAddIndexFront() {
		ListADT<String> data = makeEmptyList();
		data.addBack("A");
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("A", data.getFront());
		data.addIndex(0, "B");
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("B", data.getFront());
		Assert.assertEquals("A", data.getBack());
	}
	
	@Test
	public void testAddIndexBack() {
		ListADT<String> data = makeEmptyList();
		data.addBack("A");
		Assert.assertEquals(1, data.size());
		Assert.assertEquals("A", data.getFront());
		data.addIndex(1, "B");
		Assert.assertEquals(2, data.size());
		Assert.assertEquals("A", data.getFront());
		Assert.assertEquals("B", data.getBack());
	}
	
	@Test
	public void testAddIndexCenter() {
		ListADT<String> data = makeEmptyList();
		data.addBack("A");
		data.addBack("C");
		data.addBack("D");
		data.addBack("E");
		Assert.assertEquals(4, data.size());
		
		data.addIndex(1, "B");
		Assert.assertEquals(5, data.size());
		Assert.assertEquals("B", data.getIndex(1));
	}
	
	@Test
	public void testGetFront() {
		ListADT<String> data = makeABCDList();
		assertEquals("a", data.getFront());
	}
	
	@Test
	public void testGetBack() {
		ListADT<String> data = makeABCDList();
		assertEquals("d", data.getBack());
	}
	
	@Test(expected=EmptyListError.class)
	public void testGetFrontCrash() {
		ListADT<String> data = makeEmptyList();
		data.getFront();
	}
	
	@Test(expected=EmptyListError.class)
	public void testGetBackCrash() {
		ListADT<String> data = makeEmptyList();
		data.getBack();
	}
	
	@Test(expected=BadIndexError.class)
	public void testGetIndexLow() {
		ListADT<String> data = makeABCDList();
		data.getIndex(-2);
	}
	
	@Test(expected=BadIndexError.class)
	public void testGetIndexHigh() {
		ListADT<String> data = makeABCDList();
		data.getIndex(data.size());
	}
	
	@Test(expected=BadIndexError.class)
	public void testGetIndexHighEasy() {
		ListADT<String> data = makeABCDList();
		data.getIndex(data.size()*2);
	}
	
	@Test(expected=BadIndexError.class)
	public void testAddIndexHighEasy() {
		ListADT<String> data = makeABCDList();
		data.addIndex(data.size()*2, "the");
	}
	
	@Test(expected=BadIndexError.class)
	public void testAddIndexHigh() {
		ListADT<String> data = makeABCDList();
		data.addIndex(data.size()+1, "the");
	}
	
	@Test(expected=BadIndexError.class)
	public void testAddIndexLow() {
		ListADT<String> data = makeABCDList();
		data.addIndex(-1, "the");
	}
	
	@Test(expected=BadIndexError.class)
	public void testSetIndexHighEasy() {
		ListADT<String> data = makeABCDList();
		data.setIndex(data.size()*2, "the");
	}
	
	@Test(expected=BadIndexError.class)
	public void testSetIndexHigh() {
		ListADT<String> data = makeABCDList();
		data.setIndex(data.size(), "the");
	}
	
	@Test(expected=BadIndexError.class)
	public void testSetIndexLow() {
		ListADT<String> data = makeABCDList();
		data.setIndex(-1, "the");
	}
	
	@Test
	public void testSetIndexEasy() {
		ListADT<String> data = makeABCDList();
		data.setIndex(0, "z");
		assertEquals("z", data.getIndex(0));
		assertEquals("b", data.getIndex(1));
		assertEquals("c", data.getIndex(2));
		assertEquals("d", data.getIndex(3));

		data.setIndex(1, "y");
		assertEquals("z", data.getIndex(0));
		assertEquals("y", data.getIndex(1));
		assertEquals("c", data.getIndex(2));
		assertEquals("d", data.getIndex(3));
		
		data.setIndex(2, "x");
		assertEquals("z", data.getIndex(0));
		assertEquals("y", data.getIndex(1));
		assertEquals("x", data.getIndex(2));
		assertEquals("d", data.getIndex(3));
		
		data.setIndex(3, "w");
		assertEquals("z", data.getIndex(0));
		assertEquals("y", data.getIndex(1));
		assertEquals("x", data.getIndex(2));
		assertEquals("w", data.getIndex(3));
	}

	
	@Test
	public void testToJava() {
		assertEquals(makeABCDList().toJava(), Arrays.asList("a", "b", "c", "d"));
	}
}
