package edu.iastate.cs228.hw5;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import jdk.nashorn.api.tree.Tree;

/**
 * 
 * @author  Brad Warren: bawarren@iastate.edu
 *
 */

/**
 * 
 * This class implements a splay tree. Add any helper methods or implementation
 * details you'd like to include.
 *
 */

public class SplayTree<E extends Comparable<? super E>> extends AbstractSet<E> {
	protected Node root;
	protected int size;
	protected Node unlink; // To keep track of Node that is being unlinked

	public class Node // made public for grading purpose
	{
		public E data;
		public Node left;
		public Node parent;
		public Node right;

		public Node(E data) {
			this.data = data;
		}

		@Override
		public Node clone() {
			return new Node(data);
		}
	}

	/**
	 * Default constructor constructs an empty tree.
	 */
	public SplayTree() {
		size = 0;
	}

	/**
	 * Needs to call addBST() later on to complete tree construction.
	 */
	public SplayTree(E data) {
		root = new Node(data);
		size++;
	}

	/**
	 * Copies over an existing splay tree. The entire tree structure must be copied.
	 * No splaying. Calls cloneTreeRec().
	 * 
	 * @param tree
	 */
	public SplayTree(SplayTree<E> tree) {
		root = tree.root; // Copies root
		size = tree.size;
		if (tree.root.left != null) {
			root.left = cloneTreeRec(tree.root.left); // Copies left children of tree
		}
		if (tree.root.right != null) {
			root.right = cloneTreeRec(tree.root.right); // Copies right children of tree
		}
	}

	/**
	 * Recursive method called by the constructor above.
	 * 
	 * @param subTree
	 * @return
	 */
	private Node cloneTreeRec(Node subTree) {
		Node result = subTree;
		if (subTree.left == null && subTree.right == null) { // Base case that stops recursive calls
			return result;
		}
		if (subTree.left != null) {
			result.left = cloneTreeRec(subTree.left); // Copies left node and left nodes subtree's
		} else if (subTree.right != null) {
			result.right = cloneTreeRec(subTree.right); // Copies right node right node's subtree's
		}
		return result;
	}

	/**
	 * This function is here for grading purpose. It is not a good programming
	 * practice.
	 * 
	 * @return element stored at the tree root
	 */
	public E getRoot() {
		return root.data;
	}

	@Override
	public int size() {
		return size;
	}

	/**
	 * Clear the splay tree.
	 */
	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	// ----------
	// BST method
	// ----------

	/**
	 * Adds an element to the tree without splaying. The method carries out a binary
	 * search tree addition. It is used for initializing a splay tree.
	 * 
	 * Calls link().
	 * 
	 * @param data
	 * @return true if addition takes place false otherwise (i.e., data is in the
	 *         tree already)
	 */
	public boolean addBST(E data) {
		if (root == null) { // Empty tree adds to root
			root = new Node(data);
			++size;
			return true;
		}
		Node current = root;
		while (true) {
			int comp = current.data.compareTo(data); // Comparison of current node's data and data
			if (comp == 0) {
				return false;
			} else if (comp > 0) { // If comparison is higher
				if (current.left != null) {
					current = current.left;
				} else { // Adds new node to the left child of current
					current.left = new Node(data);
					link(current, current.left);
					return true;
				}
			} else { // If current is lower
				if (current.right != null) {
					current = current.right;
				} else { // Adds new node to the right child of current
					current.right = new Node(data);
					link(current, current.right);
					return true;
				}
			}
		}
	}

	// ------------------
	// Splay tree methods
	// ------------------

	/**
	 * Inserts an element into the splay tree. In case the element was not
	 * contained, this creates a new node and splays the tree at the new node. If
	 * the element exists in the tree already, it splays at the node containing the
	 * element.
	 * 
	 * Calls link().
	 * 
	 * @param data element to be inserted
	 * @return true if addition takes place false otherwise (i.e., data is in the
	 *         tree already)
	 */
	@Override
	public boolean add(E data) {
		if (size == 0) {
			addBST(data); // Adds node to empty tree
		}
		if (findEntry(data).data.compareTo(data) == 0) { // If a node storing the data already exists splays at that
															// node
			splay(data);
			return false;
		} else { // Adds node if not in tree and splays at that node
			link(findEntry(data), new Node(data));
			splay(data);
			return true;
		}
	}

	/**
	 * Determines whether the tree contains an element. Splays at the node that
	 * stores the element. If the element is not found, splays at the last node on
	 * the search path.
	 * 
	 * @param data element to be determined whether to exist in the tree
	 * @return true if the element is contained in the tree false otherwise
	 */
	public boolean contains(E data) {
		Node temp = findEntry(data);
		splay(temp); // Splays node found by temp
		root = temp;
		if (root.data.compareTo(data) != 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Finds the node that stores the data and splays at it.
	 *
	 * @param data
	 */
	public void splay(E data) {
		contains(data);
	}

	/**
	 * Removes the node that stores an element. Splays at its parent node after
	 * removal (No splay if the removed node was the root.) If the node was not
	 * found, the last node encountered on the search path is splayed to the root.
	 * 
	 * Calls unlink().
	 * 
	 * @param data element to be removed from the tree
	 * @return true if the object is removed false if it was not contained in the
	 *         tree
	 */
	public boolean remove(E data) {
		boolean lefty = true; // Whether left or right child for unlinking purposes
		Node current = findEntry(data);
		Node parent = current.parent;
		Node right = parent.right;
		if (current == right) {
			lefty = false;
		}
		if (current.data.compareTo(data) == 0) {
			if (root.data.compareTo(data) == 0) { // Unlink at the root
				unlink(root);
				--size;
				return true;
			} else { // Unlink at node not at root
				unlink(current);
				if (unlink != null) { // Links unlink to tree
					unlink.parent = parent;
					if (lefty == false) {
						parent.right = unlink;
					} else {
						parent.left = unlink;
					}
				} else { // Empties left or right node of parent accordingly
					if (lefty == false) {
						parent.right = null;
					} else {
						parent.left = null;
					}
				}
				--size;
				splay(parent);
				return true;
			}
		} else {
			splay(current);
			return false;
		}
	}

	/**
	 * This method finds an element stored in the splay tree that is equal to data
	 * as decided by the compareTo() method of the class E. This is useful for
	 * retrieving the value of a pair <key, value> stored at some node knowing the
	 * key, via a call with a pair <key, ?> where ? can be any object of E.
	 * 
	 * Calls findEntry(). Splays at the node containing the element or the last node
	 * on the search path.
	 * 
	 * @param data
	 * @return element such that element.compareTo(data) == 0
	 */
	public E findElement(E data) {
		if (findEntry(data) != null) {
			E node = findEntry(data).data;
			splay(data);
			if (node.compareTo(data) != 0) {
				return null;
			} else {
				return node;
			}
		} else {
			return null;
		}
	}

	/**
	 * Finds the node that stores an element. It is called by methods such as
	 * contains(), add(), remove(), and findElement().
	 * 
	 * No splay at the found node.
	 *
	 * @param data element to be searched for
	 * @return node if found or the last node on the search path otherwise null if
	 *         size == 0.
	 */
	protected Node findEntry(E data) {
		Node current = root;
		if (size == 0) {
			return null;
		} else {
			while (current.left != null || current.right != null) {
				int compare = current.data.compareTo(data); // Comparison of node and data given
				if (compare == 0) { // If comparison is equal
					return current;
				} else if (compare > 0) { // If comparison is higher than moves left
					if (current.left == null) {
						return current;
					} else {
						current = current.left;
					}
				} else if (compare < 0) {
					if (current.right == null) { // If comparison if lower then moves right
						return current;
					} else {
						current = current.right;
					}
				}
			}
			return current; // Returns either node with same value or last node in the search
		}
	}

	/**
	 * Join the two subtrees T1 and T2 rooted at root1 and root2 into one. It is
	 * called by remove().
	 * 
	 * Precondition: All elements in T1 are less than those in T2.
	 * 
	 * Access the largest element in T1, and splay at the node to make it the root
	 * of T1. Make T2 the right subtree of T1. The method is called by remove().
	 * 
	 * @param root1 root of the subtree T1
	 * @param root2 root of the subtree T2
	 * @return the root of the joined subtree
	 */
	protected Node join(Node root1, Node root2) {
		if (root1 == null) {
			unlink = root2;
			return unlink;
		}
		if (root2 == null) {
			unlink = root1;
			return unlink;
		}
		Node current = root1;
		while (current.right != null) {
			current = current.right;
		}
		splay(current); // Splays at the largest node of root1
		current.right = root2;
		root2.parent = current;
		return current;
	}

	/**
	 * Splay at the current node. This consists of a sequence of zig, zigZig, or
	 * zigZag operations until the current node is moved to the root of the tree.
	 * 
	 * @param current node to splay
	 */
	protected void splay(Node current) {
		while (current.parent != null) {
			if (current.parent.parent == null) {
				zig(current);
			} else if (current == current.parent.left && current.parent == current.parent.parent.left) { // If current
																											// is the
																											// left
																											// child of
																											// a left
																											// child
				zigZig(current);
			} else if (current == current.parent.left && current.parent == current.parent.parent.right) { // If current
																											// is the
																											// right
																											// child of
																											// a left
																											// child
				zigZag(current);
			} else if (current == current.parent.right && current.parent == current.parent.parent.right) { // If current
																											// is the
																											// right
																											// child of
																											// a right
																											// child
				zigZig(current);
			} else if (current == current.parent.right && current.parent == current.parent.parent.left) { // If current
																											// is the
																											// left
																											// child of
																											// a right
																											// child
				zigZag(current);
			}
		}
	}

	/**
	 * This method performs the zig operation on a node. Calls leftRotate() or
	 * rightRotate().
	 * 
	 * @param current node to perform the zig operation on
	 */
	protected void zig(Node current) {
		if (current == current.parent.left) { // If current is a left child
			rightRotate(current);
		} else if (current == current.parent.right) { // If current is a right child
			leftRotate(current);
		}
	}

	/**
	 * This method performs the zig-zig operation on a node. Calls leftRotate() or
	 * rightRotate().
	 * 
	 * @param current node to perform the zig-zig operation on
	 */
	protected void zigZig(Node current) {
		if (current == current.parent.left && current.parent == current.parent.parent.left) { // If current is the left
																								// child of a left child
			rightRotate(current.parent);
			rightRotate(current);
		} else if (current == current.parent.right && current.parent == current.parent.parent.right) { // If current is
																										// the right
																										// child of a
																										// right child
			leftRotate(current.parent);
			leftRotate(current);
		}
	}

	/**
	 * This method performs the zig-zag operation on a node. Calls leftRotate() and
	 * rightRotate().
	 * 
	 * @param current node to perform the zig-zag operation on
	 */
	protected void zigZag(Node current) {
		zig(current);
		zig(current);
	}

	/**
	 * Carries out a left rotation at a node such that after the rotation its former
	 * parent becomes its left child.
	 * 
	 * Calls link().
	 * 
	 * @param current
	 */
	private void leftRotate(Node current) {
		if (current.parent != null) { // If currents parent is not the root
			if (current.parent.parent != null) { // If currents grandparent is not the root
				if (current == current.parent.right) { // Current is a right child
					Node parent = current.parent;
					Node parentP = current.parent.parent;
					Node left = current.left;
					link(current, parent); // Links parent to be currents child
					if (left != null) {
						link(current.left, left); // Links currents old left node to be new left nodes child
					}
					link(parentP, current); // Links grandparent to be currents new parent
				}
			} else {
				if (current == current.parent.right) { // Current is a right child
					Node parent = current.parent;
					Node left = current.left;
					link(current, parent); // Links parent to be currents child
					if (left != null) {
						link(current.left, left); // Links currents old left node to be new left nodes child
					}
					current.parent = null;
					root = parent;
				}
			}
		}
	}

	/**
	 * Carries out a right rotation at a node such that after the rotation its
	 * former parent becomes its right child.
	 * 
	 * Calls link().
	 * 
	 * @param current
	 */
	private void rightRotate(Node current) {
		if (current.parent != null) { // If currents parent is not the root
			if (current.parent.parent != null) { // If currents grandparent is not the root
				if (current == current.parent.left) { // Current is a left child
					Node parent = current.parent;
					Node parentP = current.parent.parent;
					Node right = current.right;
					link(current, parent); // Links parent to be the currents child
					if (right != null) {
						link(current.right, right); // Links currents old right node to be the new right nodes child
					}
					link(parentP, current); // Links currents grandparent to be currents new parent

				}
			} else { // Current's grandparent is the root
				if (current == current.parent.left) { // Current is a left child
					Node parent = current.parent;
					Node right = current.right;
					link(current, parent); // Links parent to be the currents child
					if (right != null) {
						link(current.right, right); // Links currents old right node to be the new right nodes child
					}
					current.parent = null;
					root = current;
				}
			}
		}
	}

	/**
	 * Establish the parent-child relationship between two nodes.
	 * 
	 * Called by addBST(), add(), leftRotate(), and rightRotate().
	 * 
	 * @param parent
	 * @param child
	 */
	private void link(Node parent, Node child) {
		int comparison = parent.data.compareTo(child.data); // Determines if left or right child
		if (comparison > 0) { // Child is lower than parent (the left child)
			parent.left = child;
			child.parent = parent;
			if (child.left != null) {
				if (child.left.data.compareTo(child.parent.data) == 0) {
					child.left = null;
				}
			}
			if (child.right != null) {
				if (child.right.data.compareTo(child.parent.data) == 0) {
					child.right = null;
				}
			}
			++size;
		}
		if (comparison < 0) { // Child is higher than parent (the right child)
			parent.right = child;
			child.parent = parent;
			if (child.left != null) {
				if (child.left.data.compareTo(child.parent.data) == 0) {
					child.left = null;
				}
			}
			if (child.right != null) {
				if (child.right.data.compareTo(child.parent.data) == 0) {
					child.right = null;
				}
			}
			++size;
		}
	}

	/**
	 * Removes a node n by replacing the subtree rooted at n with the join of the
	 * node's two subtrees.
	 * 
	 * Called by remove().
	 * 
	 * @param n
	 */
	private void unlink(Node n) {
		if (n.parent != null) { // Detaches n from tree
			n.parent = null;
		}
		unlink = join(n.left, n.right); // Updates unlink value
	}

	/**
	 * Perform BST removal of a node.
	 * 
	 * Called by the iterator method remove().
	 * 
	 * @param n
	 */
	private void unlinkBST(Node n) {
		if (n.left != null && n.right != null) { // Deletes successor node
			Node s = successor(n);
			n.data = s.data;
			n = s;
		}
		Node replacement = null;
		// Copies left or right node to be the replacement
		if (n.left != null) {
			replacement = n.left;
		} else if (n.right != null) {
			replacement = n.right;
		}
		if (n.parent == null) { // If we are replacing the root
			root = replacement;
		} else {
			if (n == n.parent.left) { // n is a left child
				n.parent.left = replacement;
			} else {
				n.parent.right = replacement; // n is a right child
			}
		}
		if (replacement != null) {
			replacement.parent = n.parent;
		}
		--size;
	}

	/**
	 * Called by unlink() and the iterator method next().
	 * 
	 * @param n
	 * @return successor of n
	 */
	private Node successor(Node n) {
		if (n == null) { // Base case
			return null;
		} else if (n.right != null) {
			Node current = n.right;
			while (current.left != null) { // Find the lowest node to the left of the right subtree
				current = current.left;
			}
			return current;
		} else {
			Node current = n.parent;
			Node child = n;
			while (current != null && current.right == child) { // Finds the closest left child ancestor
				child = current;
				current = current.parent;
			}
			return current;
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new SplayTreeIterator();
	}

	/**
	 * Write the splay tree according to the format specified in Section 2.2 of the
	 * project description.
	 * 
	 * Calls toStringRec().
	 *
	 */
	@Override
	public String toString() {
		String result = toStringRec(root, 0); // Recursive call to build the string
		return result;
	}

	private String toStringRec(Node n, int depth) {
		String result = "";
		int indent = depth * 4; // Determines how much the string is indented
		String in = ""; // Indentation
		if (n == null) { // Base case
			return result;
		} else {
			String data = n.data.toString(); // Nodes data
			int count = 0;
			while (count != indent) {
				in += " ";
				++count;
			}
			result += in + data + "\n"; // Adds Node to result
			if (n.left != null) {
				result += toStringRec(n.left, depth + 1) + "\n"; // Recursive call for next Node to the left
				if (n.right == null) {
					indent = (depth + 1) * 4;
					in = "";
					int count2 = 0;
					while (count2 != indent) {
						in += " ";
						++count2;
					}
					result += in + "null\n"; // Prints "null" if this object doesn't exist
				}
			}
			if (n.right != null) {
				if (n.left == null) {
					indent = (depth + 1) * 4;
					in = "";
					int count2 = 0;
					while (count2 != indent) {
						in += " ";
						++count2;
					}
					result += in + "null\n"; // Prints "null" if this object doesn't exist
				}
				// After left print for order purposes
				result += toStringRec(n.right, depth + 1) + "\n"; // Recursive call for next Node to the right
			}
		}
		return result;
	}

	/**
	 *
	 * Iterator implementation for this splay tree. The elements are returned in
	 * ascending order according to their natural ordering. The methods hasNext()
	 * and next() are exactly the same as those for a binary search tree --- no
	 * splaying at any node as the cursor moves. The method remove() behaves like
	 * the class method remove(E data) --- after the node storing data is found.
	 * 
	 */
	private class SplayTreeIterator implements Iterator<E> {
		Node cursor;
		Node pending;

		public SplayTreeIterator() {
			cursor = root;
			if (cursor != null) {
				while (cursor.left != null) {
					cursor = cursor.left;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return cursor != null;
		}

		@Override
		public E next() {
			pending = cursor;
			cursor = successor(cursor);
			return pending.data;
		}

		/**
		 * This method will join the left and right subtrees of the node being removed,
		 * and then splay at its parent node. It behaves like the class method remove(E
		 * data) after the node storing data is found. Place the cursor at the parent
		 * (or the new root if removed node was the root).
		 * 
		 * Calls unlinkBST().
		 * 
		 */
		@Override
		public void remove() {
			if (pending == null) {
				throw new IllegalStateException();
			}
			unlinkBST(pending);
			pending = null;
		}
	}
}
