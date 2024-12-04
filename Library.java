
import com.sun.net.httpserver.Headers;
import java.util.Scanner;
import java.util.Iterator;
import java.io.File;
import java.io.FileNotFoundException;
import javax.sound.sampled.Line;
import java.util.NoSuchElementException;

/**
 * A library management class. Has a simple shell that users can interact with
 * to add/remove/checkout/list books in the library. Also allows saving the
 * library state to a file and reloading it from the file.
 */
public class Library {

    /**
     * Adds a book to the library. If the library already has this book then it
     * adds to the number of copies the library has.
     *
     * @throws IllegalArgumentException no parts of book object can be empty or
     * null
     * @param book a book object to be added
     * @author Anzac Houchen
     * @author anzac.shelby@gmail.com Print statements are for Debugging.
     */
    public void addBook(Book book) {
        // Prevent bad data from being added.
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null value.");
        }
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("This book is missing a Title.");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new IllegalArgumentException("Book is missing Author.");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("ISBN can't be emtpy");
        }
        if (book.getPublicationYear() > 2025
                || book.getPublicationYear() < 0) {
            throw new IllegalArgumentException("Book is published in future?");
        }
        if (book.getNumberOfCopies() < 1) {
            throw new IllegalArgumentException("Number of copies must be 1 or more");
        }
        // Search for the book using binary search tree(not yet implemented)
        Book alreadyAddedBook = bst.findByISBN(book.getIsbn());
        if (alreadyAddedBook != null) { // Book is already added
            // Update copies
            alreadyAddedBook.addCopies(book.getNumberOfCopies());
        } else { // Adding new book not already in Library to linked list
            Node newNode = new Node(book);
            newNode.next = head;
            head = newNode;
            // Add to binary search tree (not yet implemented)
            bst.add(book);
        }
        // Uncomment to Debug.
        // System.out.println();
        // System.out.println(book.getNumberOfCopies());
        // System.out.print(" copies of " + book.getTitle());
        // System.out.print(" were added. Total copies now ");
        // System.out.print(alreadyAddedBook.getNumberOfCopies() + ".");
        // System.out.println("ISBN: " + book.getIsbn());
    }

    /**
     * Checks out the given book from the library. Throw the appropriate
     * exception if book doesnt exist or there are no more copies available.
     *
     * @param isbn The ISBN of the book to check out
     * @throws IllegalArgumentException if the ISBN is null or empty.
     * @throws IllegalArgumentException if the book with the specified ISBN is
     * not found in the library.
     * @throws IllegalArgumentException if there are no available copies of the
     * book to check out
     * @Author Elizabeth Martinez Mendoza
     */
    public void checkout(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        // search for the book 
        Book bookToCheckout = bst.findByISBN(isbn);

        if (bookToCheckout == null) {
            // we dont carry this book in our library
            throw new IllegalArgumentException(" We dont carry the book with ISBN " + isbn + " .");
        }

        // checking if we have enough copies avaliable. 
        if (bookToCheckout.getNumberOfCopies() <= 0) {
            throw new IllegalArgumentException("No copies of the book with ISBN " + isbn + " are available for checkout.");
        }

        // checkout the book, reduce the number of copies 
        bookToCheckout.addCopies(-1); // decrease copies by 1
        System.out.println("Successfully checked out: " + bookToCheckout.getTitle() + " by " + bookToCheckout.getAuthor());

    }

    /**
     * Returns a book to the library
     */
    public void returnBook(String isbn) {
        Book book = findByISBN(isbn);

        if (book == null) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " does not exist.");
        }

        if (book.getCheckedOutCopies() <= 0) {
            throw new IllegalStateException("No checked-out copies of the book with ISBN " + isbn + " to return.");
        }

        book.setCheckedOutCopies(book.getCheckedOutCopies() - 1);
    }

    /**
     * Finds this book in the library. Throws appropriate exception if the book
     * doesnt exist.
     *
     * @param title the title of the book the program is to find
     * @param author the author of the book the program is to find
     * @return the book object that specifically matches the provided title and
     * author
     * @throws IllegalArgumentException if author or title is incorrectly
     * provided
     * @throws UnsupportedOperationException if no book is found with the user's
     * input
     * @author Sreyas Kishore
     * @author sreyas.kishore@gmail.com
     */
    public Book findByTitleAndAuthor(String title, String author) {
        // If statements validate the user's input to the program and returns appropriate exceptions.
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }

        // Correct inputs redirected towards the UnorderedLinkedList with the list of books.
        UnorderedLinkedList.Node<Book> current = bookList.head; // Access the head of the linked list
        while (current != null) {
            Book book = current.data; // Access the book object
            if (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author)) {
                return book; // Returns or outputs book if a match with author or title is found.
            }
            current = current.next; // Move to the next node
        }

        // If no match is found with the user's input, throw exception. 
        throw new UnsupportedOperationException("Book not found: Title = " + title + ", Author = " + author);
    }

    /**
     * Finds a book in the library by its ISBN.
     *
     * @param isbn the ISBN of the book to find
     * @return the book object if found, or null if no book matches the given
     * ISBN
     * @throws IllegalArgumentException if the provided ISBN is null or empty
     * @throws UnsupportedOperationException if no book with the given ISBN is
     * found
     * @author Ethan Tran
     * @author ethantran0324@gmail.com
     */
    public Book findByISBN(String isbn) {
        // Validate input
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        // Search through the library's linked list for the book
        UnorderedLinkedList.Node<Book> current = bookList.head; // Access the head of the linked list
        while (current != null) {
            Book book = current.data; // Access the book object
            if (book.getIsbn().equalsIgnoreCase(isbn)) { // Match ISBN (case-insensitive)
                return book; // Book found
            }
            current = current.next; // Move to the next node
        }

        // If no book is found, throw an exception
        throw new UnsupportedOperationException("Book not found with ISBN: " + isbn);
    }

/**
 * Saves the contents of this library to the given file.
 *
 * @param filename the name of the file to save the library data into
 * @throws IllegalArgumentException if the filename is null or empty
 * @throws RuntimeException if an error occurs during file writing
 * @author Ethan Tran
 * @author ethantran0324@gmail.com
 */
public void save(String filename) {
    // Validate the filename
    if (filename == null || filename.isEmpty()) {
        throw new IllegalArgumentException("Filename cannot be null or empty.");
    }

    java.io.File outputFile = new java.io.File(filename); // Create the file object

    java.io.PrintWriter writer = null; // Initialize the writer outside try-catch
    try {
        writer = new java.io.PrintWriter(outputFile); // Create the PrintWriter

        // Iterate through the library's linked list
        UnorderedLinkedList.Node<Book> current = bookList.head; // Access the head of the linked list
        while (current != null) {
            Book book = current.data;

            // Format the book data as a comma-separated string
            String bookData = book.getTitle() + "," +
                    book.getAuthor() + "," +
                    book.getIsbn() + "," +
                    book.getPublicationYear() + "," +
                    book.getNumberOfCopies();

            // Write the book data to the file
            writer.println(bookData);

            // Move to the next node in the list
            current = current.next;
        }
        System.out.println("Library saved successfully to " + filename);
    } catch (java.io.FileNotFoundException e) {
        throw new RuntimeException("An error occurred while saving the library to file: " + filename, e);
    } finally {
        // Ensure the writer is closed to free our resources
        if (writer != null) {
            writer.close();
        }
    }
}

    /**
     * Loads the contents of this library from the given file. All existing data
     * in this library is cleared before loading from the file.
     *
     * @author Diya Prasanth
     * @param filename
     * @throws FileNotFoundException if the provided filename is invalid
     * @throws NumberFormatException if publication year or number of copies are
     * invalid
     */
    public void load(String filename) {
        /**
         * TBD: clear bst/linked list
         */
        File fIn = new File(filename);
        try (Scanner inputFile = new Scanner(fIn)) {
            while (inputFile.hasNextLine()) {
                String content = inputFile.nextLine();
                /**
                 * Assume the different book info is seperated by commas
                 */
                String[] bookArray = content.split(",");
                System.out.println("Adding Book. Title: %s, Author: %s, ISBN: %s, Publication Year: %d, Copies: %d");
                Book newBook = new Book(bookArray[0], bookArray[1], bookArray[2],
                        Integer.parseInt(bookArray[3]), Integer.parseInt(bookArray[4]));
                addBook(newBook);
            }
        } catch (FileNotFoundException e) {
            /**
             * File exception: Input file was not found. Return error.
             */
            System.err.println("File not found.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid data in input file.");
        }
    }

    /**
     * An unordered linked list class. This list allows for adding elements to
     * the front of the list
     *
     * @param<T> generic type elements in this list.
     * @author Anzac Houchen
     * @author anzac.shelby@gmail.com
     */
    public class UnorderedLinkedList<T> {

        private Node<T> head;

        /**
         * Constructor. Creates empty list.
         *
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public UnorderedLinkedList() {
            head = null;
        }

        /**
         * Adds a Node to the front of the list.
         *
         * @param data A book or other element
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public void add(T data) {
            Node<T> newNode = new Node<>(data);
            newNode.next = head;
            head = newNode;
        }

        /**
         * Inner inner class to create nodes for list.
         *
         * @param <T> data stored in the node ie. book
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        private class Node {

            T data;
            Node next;

            /**
             * Constructor. Makes nodes with data as input.
             *
             * @param data will be a book object in the Library
             * @author Anzac Houchen
             * @author anzac.shelby@gmail.com
             */
            public Node(T data) {
                this.data = data;
                this.next = null;
            }
        }
    }

    /**
     * A binary search tree (bst) implementation from the text book but 
     * with without generic types because it is only for the library.
     * Each node stores a book identified by its ISBN and keeps track 
     * of the number of copies available.
     * 
     * @author Anzac Houchen
     * @author anzac.shelby@gmail.com
     */
    public class BinarySearchTree {

        private TreeNode root; // The root node of the tree
        private int size; // The number of nodes in the tree

        /** 
         * Constructs an empty BinarySearchTree.
         * 
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public BinarySearchTree() {
            this.root = null;
            size = 0;
        }

        /**
         * Returns the number of nodes in the tree.
         * 
         * @return the size of the tree
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public int size() {
            return size;
        }

        /**
         * Inserts a new book into the tree or updates the number of copies
         * if the book already exists.
         * 
         * @param key   the ISBN of the book
         * @param value the Book object to be inserted
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public void put(String key, Book value) {
            if (this.root != null) {
                put(key, value, this.root);
            } else {
                this.root = new TreeNode(key, value, value.getNumberOfCopies());
                size++;
            }
        }

        /**
         * Helper method to insert a new book or update an existing book's copies.
         * 
         * @param key          the ISBN of the book
         * @param value        the Book object to be inserted
         * @param currentNode  the current node in the traversal
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        private void put(String key, Book value, TreeNode currentNode) {
            int cmp = key.compareTo(currentNode.key);
            if (cmp < 0) {
                if (currentNode.leftChild != null) {
                    put(key, value, currentNode.leftChild);
                } else {
                    currentNode.leftChild = new TreeNode(key, value, value.getNumberOfCopies(), currentNode);
                    size++;
                }
            } else if (cmp > 0) {
                if (currentNode.rightChild != null) {
                    put(key, value, currentNode.rightChild);
                } else {
                    currentNode.rightChild = new TreeNode(key, value, value.getNumberOfCopies(), currentNode);
                    size++;
                }
            } else {
                // Update existing book's copies
                currentNode.value.addCopies(value.getNumberOfCopies());
                currentNode.numberOfCopiesAvailable += value.getNumberOfCopies(); // Update copies available
            }
        }

        /**
         * Retrieves a book by its ISBN.
         * 
         * @param key the ISBN of the book to retrieve
         * @return the Book object if found, or null if not found
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public Book get(String key) {
            if (this.root != null) {
                TreeNode result = get(key, this.root);
                if (result != null) {
                    return result.value;
                }
            }
            return null;
        }

        /**
         * Helper method to find a book in the tree.
         * 
         * @param key          the ISBN of the book to find
         * @param currentNode  the current node in the traversal
         * @return the TreeNode containing the book or null if not found
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        private TreeNode get(String key, TreeNode currentNode) {
            if (currentNode == null) {
                return null;
            }
            int cmp = key.compareTo(currentNode.key);
            if (cmp == 0) {
                return currentNode;
            } else if (cmp < 0) {
                return get(key, currentNode.leftChild);
            } else {
                return get(key, currentNode.rightChild);
            }
        }

        /**
         * Checks if the tree contains a book with the specified ISBN.
         * 
         * @param key the ISBN of the book to check
         * @return true if the tree contains the book, false otherwise
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public boolean containsKey(String key) {
            return get(key) != null;
        }

        /**
         * A private inner class representing a node in the binary search tree.
         * 
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        class TreeNode {
            String key; // ISBN
            Book value; // Book object
            int numberOfCopiesAvailable; // Number of copies available
            TreeNode leftChild; // Left child node
            TreeNode rightChild; // Right child node
            TreeNode parent; // Parent node

            /**
             * Constructs a new TreeNode with the specified key and value.
             * 
             * @param key                the ISBN of the book
             * @param value              the Book object
             * @param numberOfCopies     the number of copies available
             * @author Anzac Houchen
             * @author anzac.shelby@gmail.com
             */
            TreeNode(String key, Book value, int numberOfCopies) {
                this(key, value, numberOfCopies, null);
            }

            /**
             * Constructs a new TreeNode with the specified key, value, and parent.
             * 
             * @param key                the ISBN of the book
             * @param value              the Book object
             * @param numberOfCopies     the number of copies available
             * @param parent             the parent node
             * @author Anzac Houchen
             * @author anzac.shelby@gmail.com
             */
            TreeNode(String key, Book value, int numberOfCopies, TreeNode parent) {
                this.key = key;
                this.value = value;
                this.numberOfCopiesAvailable = numberOfCopies;
                this.parent = parent;
            }

            // Additional methods for finding minimum child and successor as needed
        }

        /**
         * Finds the minimum child of a given node.
         * 
         * @param node the node to search from
         * @return the TreeNode with the minimum key
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        private TreeNode findMinimumChild(TreeNode node) {
            while (node.leftChild != null) {
                node = node.leftChild;
            }
            return node;
        }

        /**
         * Finds the successor of a given node in the binary search tree.
         * 
         * @param node the node to find the successor for
         * @return the successor TreeNode or null if none exists
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        private TreeNode findSuccessor(TreeNode node) {
            if (node.rightChild != null) {
                return findMinimumChild(node.rightChild);
            }
            TreeNode parent = node.parent;
            while (parent != null && node == parent.rightChild) {
                node = parent;
                parent = parent.parent;
            }
            return parent;
        }

	    /**
     * Removes a book from the tree by its ISBN.
     * 
     * @param isbn the ISBN of the book to remove
     * @return the Book object that was removed
     * @throws NoSuchElementException if the book with the specified ISBN is not found
     * @author Anzac Houchen
     */
    public Book remove(String isbn) {
        TreeNode nodeToRemove = get(isbn, root);
        if (nodeToRemove == null) {
            throw new NoSuchElementException(isbn + " not in tree.");
        }
        Book oldValue = nodeToRemove.value; // Store the value for return
        removeNode(nodeToRemove);
        size--;
        return oldValue;
    }

    /**
     * Helper method to remove a node from the tree.
     * Handles three cases: no children, one child, or two children.
     * 
     * @param currentNode the node to remove
     * @author Anzac Houchen
     */
    private void removeNode(TreeNode currentNode) {
        if (currentNode.leftChild == null && currentNode.rightChild == null) {
            // Case 1: No children
            if (currentNode.parent.leftChild == currentNode) {
                currentNode.parent.leftChild = null;
            } else {
                currentNode.parent.rightChild = null;
            }
        } else if (currentNode.leftChild != null && currentNode.rightChild != null) {
            // Case 3: Two children
            TreeNode successor = findSuccessor(currentNode);
            currentNode.key = successor.key;
            currentNode.value = successor.value;
            removeNode(successor);
        } else {
            // Case 2: One child
            TreeNode child = (currentNode.leftChild != null) ? currentNode.leftChild : currentNode.rightChild;
            adjustParent(currentNode, child);
        }
    }

    /**
     * Adjusts the parent node's child reference when removing a node.
     * 
     * @param nodeToRemove the node that is being removed
     * @param childOfRemoved the child of the removed node
     * @author Anzac Houchen
     */
    private void adjustParent(TreeNode nodeToRemove, TreeNode childOfRemoved) {
        if (nodeToRemove.parent != null) {
            if (nodeToRemove.parent.leftChild == nodeToRemove) {
                nodeToRemove.parent.leftChild = childOfRemoved;
            } else {
                nodeToRemove.parent.rightChild = childOfRemoved;
            }
            if (childOfRemoved != null) {
                childOfRemoved.parent = nodeToRemove.parent;
            }
        } else {
            root = childOfRemoved; // If the removed node is root
            if (childOfRemoved != null) {
                childOfRemoved.parent = null;
            	}
            }
    	}
    }



	
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.print("library> ");
            String line = scanner.nextLine();

            // TODO: Implement code 
            if (line.startsWith("add")) {
                // TODO: Implement this case.
                // The format of the line is
                // add title author isbn publicationYear numberOfCopies
                // e.g. add Star_Trek Gene_Roddenberry ISBN-1234 1965 10
                // NOTE: If a book already exists in the library, then the number of copies should be incremented by this amount.
                // Do appropriate error checking here.
            } else if (line.startsWith("checkout")) { // @Author Elizabeth Martinez Mendoza 
                try {
                    String[] parts = line.split(" "); // split  the input into 2 parts
                    if (parts.length != 2) { // if the input does not have 2 parts, return a error message 
                        System.out.println("Invalid input. Please use the format: checkout <isbn>");
                    } else {
                        String isbn = parts[1]; // if correct ussage then use the ISBN portion
                        try {
                            library.checkout(isbn); // find the book via findByISBN
                            System.out.println("Book with ISBN " + isbn + " checked out successfully."); // if successful, this message 
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage()); // handles invalid ISBNs
                        }
                    }
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage()); // other errors that might occur 
                }
            } else if (line.startsWith("findByTitleAndAuthor")) {
                try {
                    /**
                     * Separates and interprets the title and author from the
                     * input. Processes the "findByTitleAndAuthor" command from
                     * the user input.
                     *
                     * @throws IllegalArgumentException if the title or author
                     * is not provided or the book is not found in the library.
                     * @throws UnsupportedOperationException if the method in
                     * the library is not implemented.
                     * @param line the input line containing the command and
                     * parameters.
                     * @author Sreyas Kishore
                     * @author sreyas.kishore@gmail.com
                     */

                    // Split the input into command, title, and author
                    String[] parts = line.split(" ", 3);
                    if (parts.length < 3) {
                        throw new IllegalArgumentException("Usage: findByTitleAndAuthor <title> <author>");
                    }

                    String title = parts[1];
                    String author = parts[2];

                    // Call the findByTitleAndAuthor method
                    Book foundBook = library.findByTitleAndAuthor(title, author);

                    // Prints the details about the found book
                    System.out.println("Book has been found:");
                    System.out.println("ISBN: " + foundBook.getIsbn());
                    System.out.println("Number of Copies in Library: " + foundBook.getNumberOfCopies());
                    System.out.println("Number of Copies Available: "
                            + (foundBook.getNumberOfCopies() - foundBook.getCheckedOutCopies()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (UnsupportedOperationException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }else if (line.startsWith("return")) {
		// Returns ISBN 
 		// Outputs if book is not found or if no checked out copies
		// THrow's Illegal Exceptions to output if book is not found or no checked out copies 
		// @author Sreyas Kishore
		// @author sreyas.kishore@gmail.com

    // Split the user's input into the command and ISBN
     String[] parts = line.split(" ");
    
    // Validate the user's input format and outputs invalid if incorrect
    if (parts.length != 2) {
        System.out.println("Invalid input. Please use the format: return <isbn>");
    } else {
        String isbn = parts[1];

        try {
            // Calls the returnBook method to return the book
            library.returnBook(isbn);
            System.out.println("Book with ISBN " + isbn + " has been returned successfully.");
        } catch (IllegalArgumentException e) {
            // Outputs if book is not found
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            // Outputs if there are no checked-out copies
            System.out.println("Error: " + e.getMessage());
        }
    }
}
			} else if (line.startsWith("list")) {
				// TODO: Implement this case.
				// Format of the line is 
				// list <isnb>
				// e.g. list ISBN-1234
				// NOTE: This code should print out the number of copies in the library and the number of copies available.
			} else if (line.startsWith("save")) {
				// TODO: Implement this case.
				// Format of the line is
				// save <filename>
				// e.g. save LbraryFile.dat
			} else if (line.startsWith("load")) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.err.println("Invalid input. Use the format 'load <filename>'");
                } else {
                    library.load(parts[1]);
                }
			} else if (line.startsWith("exit")) {
				break;
			}
    }
}
