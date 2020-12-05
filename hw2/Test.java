package edu.iastate.cs228.hw2;

public class Test {
	public static void main(String[] args) {
		Point[] p = new Point[5];
		p[0] = new Point(1,3);
		p[1] = new Point(1,1);
		p[2] = new Point(2,4);
		p[3] = new Point(5,5);
		p[4] = new Point(8,3);
		SelectionSorter i = new SelectionSorter(p);
		i.setComparator(1);
		i.sort();
		i.getPoints(p);
		System.out.println(p[0].toString());
		System.out.println(p[1].toString());
		System.out.println(p[2].toString());
		System.out.println(p[3].toString());
		System.out.println(p[4].toString());

	}
}
