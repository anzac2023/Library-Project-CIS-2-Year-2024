
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

/**
* The Library Management Project is a complex circuit composed of different data structures and
* methods that allow the user to search and add books in the database. The two data structures we
* used to add and search for books include the Binary Search Tree and the Unordered Linked List. 
* When the user inputs certain information such as the book's author, title and ISBN number through 
* the addBook method. The program verifies if the book exists in the database and if it does, add’s to the number 
* of copies already held by the library. If the library has 0 copies, the program add’s the new appropriate number.
* The checkout method is used to stabilize this process. By first calling for the Binary Search Tree to search for
* the desired book. The program verifies how many available copies are left and deducts a copy for the checkout. 
* If no available copies are found by the BST, an appropriate message is outputted to indicate the user of it. 
* If a user decides to return the book. The use of the return method allows the user to search by the use of an ISBN, 
* title of book or author's name to match the book to the library's catalog, and successfully return it. 
* Increasing the number of available copies as per the user’s quantity.
* The use of the Binary Search Tree and Unordered Linked List allow the user to search and add books to the library’s 
* catalog/database. While the use of the methods allow the user to checkout and return books.
*/
public class Library {

    private BST bst = new BST();
    public UnorderedLinkedList<Book> BookList;

    public BST getBst() {
        return bst;  // initialize the BST
    }

    public Library() {
        // Creates a new empty linkedlist to hold book objects
        this.BookList = new UnorderedLinkedList<Book>();
    }

    public UnorderedLinkedList<Book> getBookList() { // provides acess to BookList 
        return BookList;
    }

    /**
     * Adds a book to the library by adding it to the binary search tree and the
     * linked list. If the library already has this book in the binary search
     * tree then it adds to the number of copies the library has. If the book
     * updates the number of copies in the bst the book is not added again to
     * the linked list but assumed to already be there from the first time it
     * must have been added.
     *
     * @throws IllegalArgumentException no parts of book object can be empty or
     * null
     * @param book a book object to be added
     * @author Anzac Houchen
     * @author anzac.shelby@gmail.com Print statements are for Debugging.
     * @bigO O(log n) for searching the bst
     * @bigO O(1) for adding to the linked list
     * @bigO analysis added by @author Anzac Houchen
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
        Book alreadyAddedBook = bst.get(book.getIsbn());
        if (alreadyAddedBook != null) { // Book is already added
            alreadyAddedBook.addCopies(book.getNumberOfCopies());
        } else { // Adding new book not already in Library to linked list
            bst.add(book.getIsbn(), book);
            BookList.add(book); // added to make sure we are 
            // adding to the list and the tree
        }
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
     * @bigO O(log n) for searching for a book in the bst.
     * @bigO analysis added by @author Anzac Houchen
     */
    public void checkout(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        // search for the book 
        Book bookToCheckout = bst.get(isbn);

        if (bookToCheckout == null) {
            // we dont carry this book in our library
            throw new IllegalArgumentException(" We dont carry the book with ISBN "
                    + isbn + " .");
        }

        // checking if we have enough copies avaliable. 
        if (bst.numberOfCopiesAvailable(isbn, 0) <= 0) {
            throw new IllegalArgumentException("No copies of the book with ISBN "
                    + isbn + " are available for checkout.");
        }

        // checkout the book, reduce the number of copies 
        bst.numberOfCopiesAvailable(isbn, -1);// decrease copies by 1 
        // this needs to change the number of copies avaible in the branch 
        System.out.println("Successfully checked out: " + bookToCheckout.getTitle()
                + " by " + bookToCheckout.getAuthor());

    }

    /**
     * Returns a book to the library
     *
     * @ Orginal Author Please Fill
     * @Edited By Elizabeth Martinez Mendoza changed the logic we were using
     * differntaining numberofCopies vs numberofcopies Avaliable. Code formating
     * is orginal to @Orignal Author
     * @param isbn string isbn to find book in bst
     * @throws IllegalArgumentException if the book does not exist
     * @thows IllegalStateException if none of the copies are check out
     * @bigO O(log n) for searching the bst
     * @bigO @param @throws @throws added by @author Anzac Houchen
     */
    public void returnBook(String isbn) {
        Book book = getBst().get(isbn);

        if (book == null) {
            throw new IllegalArgumentException("Book with ISBN " + isbn
                    + " does not exist.");
        }

        int checkedOutCopies
                = book.getNumberOfCopies() - bst.numberOfCopiesAvailable(isbn, 0);

        if (checkedOutCopies > 0) {
            ///
            bst.numberOfCopiesAvailable(isbn, 1);
            System.out.println("Book with ISBN " + isbn
                    + " has been successfully returned. Available copies: "
                    + bst.numberOfCopiesAvailable(isbn, 0));

        } else {
            throw new IllegalStateException("No checked-out copies of the book with ISBN "
                    + isbn + " to return.");
        }
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
     * @bigO O(n) searching linked list for isbn
     * @bigO analysis added by @author Anzac Houchen
     */
    public Book findByTitleAndAuthor(String title, String author) {
        // If statements validate the user's input to the program and returns 
        // appropriate exceptions.
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }

        // Correct inputs redirected towards the UnorderedLinkedList with the list of 
        // books.
        // Access the head of the linked list.
        UnorderedLinkedList<Book>.Node<Book> current = BookList.getHead();
        while (current != null) {
            Book book = current.data; // Access the book object
            if (book.getTitle().equalsIgnoreCase(title)
                    && book.getAuthor().equalsIgnoreCase(author)) {
                return book; // Returns or outputs book if a match with 
                // author or title is found.
            }
            current = current.next; // Move to the next node
        }

        // If no match is found with the user's input, throw exception. 
        throw new UnsupportedOperationException("Book not found: Title = "
                + title + ", Author = " + author);
    }

    /**
     * Finds a book in the library by its ISBN using the BST.
     *
     * @param isbn the ISBN of the book to find
     * @return the book object if found
     * @throws IllegalArgumentException if the provided ISBN is null or empty
     * @throws NoSuchElementException if no book with the given ISBN is found
     *
     * @author Ethan Tran
     * @author ethantran0324@gmail.com
     *
     * Big O analysis: The time complexity is O(log n) because the method
     * searches for the book in the BST, which has a logarithmic search time in
     * the best case and average scenarios.
     *
     * The space complexity is O(1) because no additional space is used aside
     * from variables to store the book reference.
     */
    public Book findByISBN(String isbn) {
        // Validate input
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty.");
        }

        // Search for the book in the BST
        Book book = bst.get(isbn);

        // If no book is found, throw an exception
        if (book == null) {
            throw new NoSuchElementException("Book not found with ISBN: " + isbn);
        }

        System.out.println("Book found: " + book.getTitle() + " by " + book.getAuthor());
        return book; // Return the book
    }

    /**
     * Saves the contents of this library to the given file.
     *
     * @param filename the name of the file to save the library data into
     * @throws IllegalArgumentException if the filename is null or empty
     * @throws RuntimeException if an error occurs during file writing
     *
     * Big O analysis: The time complexity is O(n) because the method iterates
     * through the linked list to retrieve and write each book's data.
     *
     * The space complexity is O(1) because aside from variables for the current
     * node, book data, and writer, no extra space is used.
     */
    public void save(String filename) {
        // Validate the filename
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty.");
        }
        //java.io.File outputFile = new java.io.File(filename); // Create the file object
        //java.io.PrintWriter writer = null; // Initialize the writer outside try-catch
        // Edited @Anzac Houchen to get save method to work
        try (PrintWriter writer = new PrintWriter(new File(filename))){
            //writer = new java.io.PrintWriter(outputFile); // Create the PrintWriter
            // Iterate through the library's linked list
            // Access the head of the linked list
            UnorderedLinkedList<Book>.Node<Book> current = BookList.getHead();
            while (current != null) {
                Book book = current.data;
                // Format the book data as a comma-separated string
                String bookData = book.getTitle() + ","
                        + book.getAuthor() + ","
                        + book.getIsbn() + ","
                        + book.getPublicationYear() + ","
                        + book.getNumberOfCopies();
                // Write the book data to the file
                writer.println(bookData);
                // Move to the next node in the list
                current = current.next;
            }
            System.out.println("Library saved successfully to " + filename);
        } catch (FileNotFoundException ex) {
            System.err.println("Error: File not found - " +
            ex.getMessage());
        } 
        catch (Exception ex) {
            System.err.println("Unexpected error occured: " + 
            ex.getMessage());
            ex.printStackTrace();
            //System.out.println(ex.printStackTrace());
            System.out.println("Test 1.");
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
     *
     * We read entries from the file which is only 1 operation, and for each
     * line in the file, addBook is called As addBook is a O(logn) operation
     * performed n times, the load function wil be O(nlogn)
     */
    public void load(String filename) {
        /**
         * Clear the binary search tree and linked list
         */
        BookList = new UnorderedLinkedList<Book>();
        bst = new BST();

        /**
         * Load the information from the file
         */
        File fIn = new File(filename);
        try (Scanner inputFile = new Scanner(fIn)) {
            int loadedBooks = 0;
            int failedBooks = 0;
            while (inputFile.hasNextLine()) {
                String content = inputFile.nextLine();
                /**
                 * Assume the different book info is seperated by commas
                 */
                String[] bookArray = content.split(",");
                System.out.printf("Adding Book. Title: %s, Author: %s, ISBN: %s, Publication Year: %d, Copies: %d%n",
                        bookArray[0].replaceAll(" ", ""), bookArray[1].replaceAll(" ", ""), bookArray[2], Integer.parseInt(bookArray[3]), Integer.parseInt(bookArray[4]));
                Book newBook = new Book(bookArray[0], bookArray[1], bookArray[2],
                        Integer.parseInt(bookArray[3]), Integer.parseInt(bookArray[4]));
                try {
                    addBook(newBook);
                    loadedBooks++;
                } catch (IllegalArgumentException e) {
                    failedBooks++;
                }
            }
            System.out.println("Load completed. Total books processed: " + (loadedBooks + failedBooks));
            System.out.println("Books successfully loaded: " + loadedBooks);
            System.out.println("Books failed to load: " + failedBooks);
        } catch (FileNotFoundException e) {
            /**
             * File exception: Input file was not found. Return error.
             */
            System.err.println("File not found.");
        } catch (NumberFormatException e) {
            System.err.println("Invalid data in input file.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.print("library> ");
            String line = scanner.nextLine();
            /**
             * Add title author isbn, pub. year and number of copies e.g. add
             * Star_Trek Gene_Roddenberry ISBN-1234 1965 10
             *
             * @Author Elizabeth Martinez Mendoza
             */
            if (line.startsWith("add")) {
                String[] parts = line.split(" "); // split the input 
                if (parts.length != 6) { // if the input does not have 6 
                    // parts, return a error message 
                    System.out.println("Invalid input Use the format: add <title> <author> <isbn> <publicationYear> <numberOfCopies>. Ex: add WhereTheRedFernGrows WilsonRawls 780395775288 1961 5");
                } else {
                    String title = parts[1];
                    String author = parts[2];
                    String isbn = parts[3];

                    try {
                        int publicationYear = Integer.parseInt(parts[4]);
                        int numberOfCopies = Integer.parseInt(parts[5]);
                        // Correct the addBook method call by creating a Book 
                        // object first
                        Book book = new Book(title, author, isbn, publicationYear, numberOfCopies);
                        library.addBook(book);

                        ///library.addBook(title, author, isbn, publicationYear, 
                        /// numberOfCopies);
                        System.out.println("Book added/updated successfully.");
                    } catch (NumberFormatException e) { // invalid number of copies or 
                        // pub. year 
                        System.out.println("Invalid number format for publicationYear or numberOfCopies.");
                    } catch (Exception e) {// any other excpetions 
                        System.out.println("Could Not Add Book: " + e.getMessage());
                    }
                }
            } else if (line.startsWith("checkout")) { // @Author Elizabeth Martinez Mendoza 
                try {
                    String[] parts = line.split(" "); // split  the input into 2 parts
                    if (parts.length != 2) { // if the input does not have 2 parts, return a error message 
                        System.out.println("Invalid input. Please use the format: checkout <isbn>. Ex: checkout 000000000000");
                    } else {
                        String isbn = parts[1]; // if correct ussage then use the ISBN portion
                        try {
                            library.checkout(isbn); // find the book via findByISBN
                            // if successful, this message
                            System.out.println("Book with ISBN " + isbn + " checked out successfully.");
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage()); // handles invalid ISBNs
                        }
                    }
                } catch (Exception e) {
                    // other errors that might occur
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            } else if (line.startsWith("findByTitleAndAuthor")) {
                /**
                 * @author Diya Prasanth Find the isbn and other details given
                 * the title and author
                 */
                String[] parts = line.split(" ");
                if (parts.length != 3) {
                    System.out.println("Invalid input. Please use the format: findByTitleAndAuthor <title> <author>");
                } else {
                    Book book = library.findByTitleAndAuthor(parts[1], parts[2]);
                    if (book != null) {
                        System.out.println("ISBN: " + book.isbn + ", Copies in the Library: " + book.numberOfCopies + ", Copies Currently Available:" + library.bst.getNumOfCopiesAvailable(book.isbn));
                    } else {
                        System.err.println("Book not found");
                    }
                }

            } else if (line.startsWith("return")) {
                /**
                 * Returns ISBN Outputs if book is not found or if no checked
                 * out copies THrow's Illegal Exceptions to output if book is
                 * not found or no checked out copies
                 *
                 * @Author Sreyas Kishore
                 * @author sreyas.kishore@gmail.com
                 */
                String[] parts = line.split(" "); // Split the user's input into the command and ISBN
                // Validate the user's input format and outputs invalid if incorrect
                if (parts.length != 2) {
                    System.out.println("Invalid input. Please use the format: return <isbn>");
                } else {
                    String isbn = parts[1];
                    try { // Calls the returnBook method to return the book
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
                /**
                 * @author Rania
                 * @author Diya Prasanth (rewrote most of the else if to account
                 * for errors) This code should print out the number of copies
                 * in the library and the number of copies available.
                 */
            } else if (line.startsWith("list")) {
                try {
                    String[] parts = line.split(" "); // split  the input into 2 parts
                    if (parts.length != 2) { // if the input does not have 2 parts, return a error message 
                        System.out.println("Invalid input. Please use the format: list <isbn>");
                    } else {
                        String isbn = parts[1]; // if correct ussage then use the ISBN portion
                        Book book = library.bst.get(isbn);
                        if (book != null) {
                            System.out.println("ISBN: " + isbn + ", Copies in the Library: " + book.numberOfCopies + ", Copies Currently Available:" + library.bst.getNumOfCopiesAvailable(isbn));
                        }
                    }
                } catch (Exception e) {
                    // other errors that might occur
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }

            } else if (line.startsWith("save")) {
                // @author Anzac Houchen fix save bug
                // Check if command line entry save has less than 5 char
                if (line.length() <= 5) {
                    System.err.println("Invalid input." +
                    " Use the format: save <filename>");
                }
                else {
                String filename = line.substring(5).trim();
                    if (filename.isEmpty()) {
                        System.err.println("Invalid filename. " +
                        "Please provide valid filename.");
                    }
                    else {             
                        try {
                            library.save(filename);
                            System.out.println("Library data saved to " + filename);
                        } 
                        catch (Exception ex) {
                            System.err.println("Unexpected error occured: " + ex.getMessage());
                        }
                    }
                }// @author Anzac Houchen fix save bug ^^^^^^^^^^^^^
            } else if (line.startsWith("load")) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.err.println("Invalid input. Use the format 'load <filename>'");
                } else {
                    library.load(parts[1]);
                }
            } else if (line.startsWith("exit")) { //@Author Elizabeth Martinez Mendoza 
                System.out.print("Are you sure you want to exit? (yes/no): ");
                String answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("yes")) {
                    System.out.println("Exiting the library.");
                    System.exit(0);
                } else if (answer.equals("no")) {
                    System.out.println("You are still in the library");
                } else {
                    System.out.println("Invalid Answer. Type 'yes' or 'no' to continue");
                }
            }
        }
    }

    /**
     * An unordered linked list class. This list allows for adding elements to
     * the front of the list.
     *
     * @param <T> generic type elements in this list.
     * @author Anzac Houchen
     * @author anzac.shelby@gmail.com
     */
    public class UnorderedLinkedList<T> {

        private Node<T> head;  // The head node of the linked list

        public Node<T> getHead() {
            return head;
        }

        /**
         * Constructor. Creates an empty list.
         *
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public UnorderedLinkedList() {
            head = null;  // Initialize the list with no nodes
        }

        /**
         * Adds a Node to the front of the list.
         *
         * @param data A book or other element to add to the list
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public void add(T data) {
            Node<T> newNode = new Node<>(data);  // Create a new node with the data
            newNode.next = head;  // Set the new node's next reference to the current head
            head = newNode;  // Update the head to point to the new node
        }

        /**
         * Inner class to create nodes for the linked list.
         *
         * @param <T> Data stored in the node (e.g., a book)
         * @author Anzac Houchen
         * @author anzac.shelby@gmail.com
         */
        public class Node<T> {  // Add the generic <T> to the Node class

            T data;  // Data held by the node
            Node<T> next;  // Reference to the next node in the list

            /**
             * Constructor. Makes a node with data as input.
             *
             * @param data The data for this node (e.g., a book object in the
             * Library)
             * @author Anzac Houchen
             * @author anzac.shelby@gmail.com
             */
            public Node(T data) {
                this.data = data;  // Initialize the node with the provided data
                this.next = null;  // By default, the next reference is null
            }

        }
    }

    public class BST {

        private TreeNode root; // The root node of the tree
        private int size; // The number of nodes in the tree

        public BST() {
            this.root = null;
            size = 0;
        }

        public int size() {
            return size;
        }

        public void add(String key, Book value) {
            if (this.root != null) {
                put(key, value, this.root);
            } else {
                this.root = new TreeNode(key, value, value.getNumberOfCopies());
                size++;
            }
        }

        private void put(String key, Book value, TreeNode currentNode) {
            int cmp = key.compareTo(currentNode.key);
            if (cmp < 0) {
                if (currentNode.leftChild != null) {
                    put(key, value, currentNode.leftChild);
                } else {
                    currentNode.leftChild = new TreeNode(key, value, value.getNumberOfCopies(),
                            currentNode);
                    size++;
                }
            } else if (cmp > 0) {
                if (currentNode.rightChild != null) {
                    put(key, value, currentNode.rightChild);
                } else {
                    currentNode.rightChild = new TreeNode(key, value, value.getNumberOfCopies(),
                            currentNode);
                    size++;
                }
            } else {
                currentNode.value.addCopies(value.getNumberOfCopies());
                currentNode.numberOfCopiesAvailable += value.getNumberOfCopies();
            }
        }

        public Book get(String key) {
            if (this.root != null) {
                TreeNode result = get(key, this.root);
                if (result != null) {
                    return result.value;
                }
            }
            return null;
        }

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

        public boolean containsKey(String key) {
            return get(key) != null;
        }

        class TreeNode {

            String key;
            Book value;
            int numOfCopiesInLibrary;
            int numberOfCopiesAvailable;
            TreeNode leftChild;
            TreeNode rightChild;
            TreeNode parent;

            TreeNode(String key, Book value, int numberOfCopies) {
                this(key, value, numberOfCopies, null);
            }

            TreeNode(String key, Book value, int numberOfCopies, TreeNode parent) {
                this.key = key;
                this.value = value;
                this.numOfCopiesInLibrary = numberOfCopies;
                this.numberOfCopiesAvailable = numberOfCopies;
                this.parent = parent;
            }
        }

        /**
         * @Author Elizabeth Martinez Mendoza Updates the number of available
         * copies of a book in a binary search tree (BST)
         * @param key identifier of the book
         * @param change the change of copies ex, 1, 2...
         * @return updated nunmber of copies
         */
        public int numberOfCopiesAvailable(String key, int change) {
            TreeNode node = bst.get(key, root); // Get the TreeNode containing the Book
            if (node != null) {
                node.numberOfCopiesAvailable += change;
                return node.numberOfCopiesAvailable; // Successfully updated the number of copies
            } else {
                return -1; // Book not found
            }
        }

        /**
         * @author Diya Prasanth Return number of available copies in the
         * library
         * @param key
         */
        public int getNumOfCopiesAvailable(String key) {
            TreeNode node = bst.get(key, root);
            if (node != null) {
                return node.numberOfCopiesAvailable;
            } else {
                return 0;
            }
        }

        private TreeNode findMinimumChild(TreeNode node) {
            while (node.leftChild != null) {
                node = node.leftChild;
            }
            return node;
        }

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

        public Book remove(String isbn) {
            TreeNode nodeToRemove = get(isbn, root);
            if (nodeToRemove == null) {
                throw new NoSuchElementException(isbn + " not in tree.");
            }
            Book oldValue = nodeToRemove.value;
            removeNode(nodeToRemove);
            size--;
            return oldValue;
        }

        private void removeNode(TreeNode currentNode) {
            if (currentNode.leftChild == null && currentNode.rightChild == null) {
                if (currentNode.parent.leftChild == currentNode) {
                    currentNode.parent.leftChild = null;
                } else {
                    currentNode.parent.rightChild = null;
                }
            } else if (currentNode.leftChild != null && currentNode.rightChild != null) {
                TreeNode successor = findSuccessor(currentNode);
                currentNode.key = successor.key;
                currentNode.value = successor.value;
                removeNode(successor);
            } else {
                TreeNode child = (currentNode.leftChild != null)
                        ? currentNode.leftChild : currentNode.rightChild;
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
                } else {
                    root = childOfRemoved; // If the removed node is root
                }
            }
        }
    }
}
