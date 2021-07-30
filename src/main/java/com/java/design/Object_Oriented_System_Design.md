

#### Design a Library Management System:

##### Features:
  - Search Books By title, author, subject category, and by publication date.
  - Book unique Id with rack it belongs to locate book physically
  - Book, resver book, multiple same books, book item
  - Retrieve information of who took book, what are the books checked out of by perticale member
  - max limit of 5 books a member can barrow
  - max number of day a memeber can keep a book
  - fine after book retured due date
  - memebr should be able to resver books which are ot available
  - notification to memeber when book is available
  - each book and member have bar code, system should be able to read barcode from books and memeber lib crads
##### Models:

```
public enum BookFormat {
  HARDCOVER,
  PAPERBACK,
  AUDIO_BOOK,
  EBOOK,
  NEWSPAPER,
  MAGAZINE,
  JOURNAL
}

public enum BookStatus {
  AVAILABLE,
  RESERVED,
  LOANED,
  LOST
}

public enum ReservationStatus{
  WAITING,
  PENDING,
  CANCELED,
  NONE
}

public enum AccountStatus{
  ACTIVE,
  CLOSED,
  CANCELED,
  BLACKLISTED,
  NONE
}

public class Address {
  private String streetAddress;
  private String city;
  private String state;
  private String zipCode;
  private String country;
}

public class Person {
  private String name;
  private Address address;
  private String email;
  private String phone;
}

public class Constants {
  public static final int MAX_BOOKS_ISSUED_TO_A_USER = 5;
  public static final int MAX_LENDING_DAYS = 10;
}

public abstract class Account {
  private String id;
  private String password;
  private AccountStatus status;
  private Person person;

  public boolean resetPassword();
}

public class Librarian extends Account {
  public boolean addBookItem(BookItem bookItem);

  public boolean blockMember(Member member);

  public boolean unBlockMember(Member member);
}

public class Member extends Account {
  private Date dateOfMembership;
  private int totalBooksCheckedout;

  public int getTotalBooksCheckedout();

  public boolean reserveBookItem(BookItem bookItem);

  private void incrementTotalBooksCheckedout();

  public boolean checkoutBookItem(BookItem bookItem) {
    if (this.getTotalBooksCheckedOut() >= Constants.MAX_BOOKS_ISSUED_TO_A_USER) {
      ShowError("The user has already checked-out maximum number of books");
      return false;
    }
    BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
    if (bookReservation != null && bookReservation.getMemberId() != this.getId()) {
      // book item has a pending reservation from another user
      ShowError("This book is reserved by another member");
      return false;
    } else if (bookReservation != null) {
      // book item has a pending reservation from the give member, update it
      bookReservation.updateStatus(ReservationStatus.COMPLETED);
    }

    if (!bookItem.checkout(this.getId())) {
      return false;
    }

    this.incrementTotalBooksCheckedout();
    return true;
  }

  private void checkForFine(String bookItemBarcode) {
    BookLending bookLending = BookLending.fetchLendingDetails(bookItemBarcode);
    Date dueDate = bookLending.getDueDate();
    Date today = new Date();
    // check if the book has been returned within the due date
    if (today.compareTo(dueDate) > 0) {
      long diff = todayDate.getTime() - dueDate.getTime();
      long diffDays = diff / (24 * 60 * 60 * 1000);
      Fine.collectFine(memberId, diffDays);
    }
  }

  public void returnBookItem(BookItem bookItem) {
    this.checkForFine(bookItem.getBarcode());
    BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
    if (bookReservation != null) {
      // book item has a pending reservation
      bookItem.updateBookItemStatus(BookStatus.RESERVED);
      bookReservation.sendBookAvailableNotification();
    }
    bookItem.updateBookItemStatus(BookStatus.AVAILABLE);
  }

  public bool renewBookItem(BookItem bookItem) {
    this.checkForFine(bookItem.getBarcode());
    BookReservation bookReservation = BookReservation.fetchReservationDetails(bookItem.getBarcode());
    // check if this book item has a pending reservation from another member
    if (bookReservation != null && bookReservation.getMemberId() != this.getMemberId()) {
      ShowError("This book is reserved by another member");
      member.decrementTotalBooksCheckedout();
      bookItem.updateBookItemState(BookStatus.RESERVED);
      bookReservation.sendBookAvailableNotification();
      return false;
    } else if (bookReservation != null) {
      // book item has a pending reservation from this member
      bookReservation.updateStatus(ReservationStatus.COMPLETED);
    }
    BookLending.lendBook(bookItem.getBarCode(), this.getMemberId());
    bookItem.updateDueDate(LocalDate.now().plusDays(Constants.MAX_LENDING_DAYS));
    return true;
  }
}


public class BookReservation {
  private Date creationDate;
  private ReservationStatus status;
  private String bookItemBarcode;
  private String memberId;

  public static BookReservation fetchReservationDetails(String barcode);
}

public class BookLending {
  private Date creationDate;
  private Date dueDate;
  private Date returnDate;
  private String bookItemBarcode;
  private String memberId;

  public static boolean lendBook(String barcode, String memberId);
  public static BookLending fetchLendingDetails(String barcode);
}

public class Fine {
  private Date creationDate;
  private double bookItemBarcode;
  private String memberId;

  public static void collectFine(String memberId, long days) {}
}

public abstract class Book {
  private String ISBN;
  private String title;
  private String subject;
  private String publisher;
  private String language;
  private int numberOfPages;
  private List<Author> authors;
}

public class BookItem extends Book {
  private String barcode;
  private boolean isReferenceOnly;
  private Date borrowed;
  private Date dueDate;
  private double price;
  private BookFormat format;
  private BookStatus status;
  private Date dateOfPurchase;
  private Date publicationDate;
  private Rack placedAt;

  public boolean checkout(String memberId) {
    if(bookItem.getIsReferenceOnly()) {
      ShowError("This book is Reference only and can't be issued");
      return false;
    }
    if(!BookLending.lendBook(this.getBarCode(), memberId)){
      return false;
    }
    this.updateBookItemStatus(BookStatus.LOANED);
    return true;
  }
}

public class Rack {
  private int number;
  private String locationIdentifier;
}

public interface Search {
  public List<Book> searchByTitle(String title);
  public List<Book> searchByAuthor(String author);
  public List<Book> searchBySubject(String subject);
  public List<Book> searchByPubDate(Date publishDate);
}

public class Catalog implements Search {
  private HashMap<String, List<Book>> bookTitles;
  private HashMap<String, List<Book>> bookAuthors;
  private HashMap<String, List<Book>> bookSubjects;
  private HashMap<String, List<Book>> bookPublicationDates;

  public List<Book> searchByTitle(String query) {
    // return all books containing the string query in their title.
    return bookTitles.get(query);
  }

  public List<Book> searchByAuthor(String query) {
    // return all books containing the string query in their author's name.
    return bookAuthors.get(query);
  }
}


```

#### Design a Parking Lot:
##### Features:
  - Parking lot should have multiple floors
  - parking lot should have multiple entry and exit points
  - customer should allow to get ticket at entry and pay at way out
  - customer can pay at exit automated panel or attendee
  - can pay via both cash ro card
  - customer can also pay at each floor or displace panel
  - the system should not allow more vachiles than parking lot
  - each parking floor will have many parking spots, Compact, Large, Handicapped, Motorcycle
  - Parking lot should have some parking spots specified for electric cars. with electric panel to  pay and charge vehicles.
  - parking for different types car, truck, van, motorcycle,
  - display board showing any free parking spot for each spot type.
  - system should support a per-hour parking fee model. For example,
    pay $4 for the first hour, $3.5 for the second and third hours, and $2.5 for all the remaining hours.
##### Models:
```
public enum VehicleType {
  CAR, TRUCK, ELECTRIC, VAN, MOTORBIKE
}

public enum ParkingSpotType {
  HANDICAPPED, COMPACT, LARGE, MOTORBIKE, ELECTRIC
}

public enum AccountStatus {
  ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED, UNKNOWN
}

public enum ParkingTicketStatus {
  ACTIVE, PAID, LOST
}

public class Address {
  private String streetAddress;
  private String city;
  private String state;
  private String zipCode;
  private String country;
}

public class Person {
  private String name;
  private Address address;
  private String email;
  private String phone;
}

public abstract class Account {
  private String userName;
  private String password;
  private AccountStatus status;
  private Person person;

  public boolean resetPassword();
}

public class Admin extends Account {
  public bool addParkingFloor(ParkingFloor floor);
  public bool addParkingSpot(String floorName, ParkingSpot spot);
  public bool addParkingDisplayBoard(String floorName, ParkingDisplayBoard displayBoard);
  public bool addCustomerInfoPanel(String floorName, CustomerInfoPanel infoPanel);

  public bool addEntrancePanel(EntrancePanel entrancePanel);
  public bool addExitPanel(ExitPanel exitPanel);
}

public class ParkingAttendant extends Account {
  public bool processTicket(string TicketNumber);
}

public abstract class ParkingSpot {
  private String number;
  private boolean free;
  private Vehicle vehicle;
  private final ParkingSpotType type;

  public boolean IsFree();

  public ParkingSpot(ParkingSpotType type) {
    this.type = type;
  }

  public boolean assignVehicle(Vehicle vehicle) {
    this.vehicle = vehicle;
    free = false;
  }

  public boolean removeVehicle() {
    this.vehicle = null;
    free = true;
  }
}

public class HandicappedSpot extends ParkingSpot {
  public HandicappedSpot() {
    super(ParkingSpotType.HANDICAPPED);
  }
}

public class CompactSpot extends ParkingSpot {
  public CompactSpot() {
    super(ParkingSpotType.COMPACT);
  }
}

public class LargeSpot extends ParkingSpot {
  public LargeSpot() {
    super(ParkingSpotType.LARGE);
  }
}

public class MotorbikeSpot extends ParkingSpot {
  public MotorbikeSpot() {
    super(ParkingSpotType.MOTORBIKE);
  }
}

public class ElectricSpot extends ParkingSpot {
  public ElectricSpot() {
    super(ParkingSpotType.ELECTRIC);
  }
}

public abstract class Vehicle {
  private String licenseNumber;
  private final VehicleType type;
  private ParkingTicket ticket;

  public Vehicle(VehicleType type) {
    this.type = type;
  }

  public void assignTicket(ParkingTicket ticket) {
    this.ticket = ticket;
  }
}

public class Car extends Vehicle {
  public Car() {
    super(VehicleType.CAR);
  }
}

public class Van extends Vehicle {
  public Van() {
    super(VehicleType.VAN);
  }
}

public class Truck extends Vehicle {
  public Truck() {
    super(VehicleType.TRUCK);
  }
}

public class ParkingFloor {
  private String name;
  private HashMap<String, HandicappedSpot> handicappedSpots;
  private HashMap<String, CompactSpot> compactSpots;
  private HashMap<String, LargeSpot> largeSpots;
  private HashMap<String, MotorbikeSpot> motorbikeSpots;
  private HashMap<String, ElectricSpot> electricSpots;
  private HashMap<String, CustomerInfoPortal> infoPortals;
  private ParkingDisplayBoard displayBoard;

  public ParkingFloor(String name) {
    this.name = name;
  }

  public void addParkingSpot(ParkingSpot spot) {
    switch (spot.getType()) {
    case ParkingSpotType.HANDICAPPED:
      handicappedSpots.put(spot.getNumber(), spot);
      break;
    case ParkingSpotType.COMPACT:
      compactSpots.put(spot.getNumber(), spot);
      break;
    case ParkingSpotType.LARGE:
      largeSpots.put(spot.getNumber(), spot);
      break;
    case ParkingSpotType.MOTORBIKE:
      motorbikeSpots.put(spot.getNumber(), spot);
      break;
    case ParkingSpotType.ELECTRIC:
      electricSpots.put(spot.getNumber(), spot);
      break;
    default:
      print("Wrong parking spot type!");
    }
  }

  public void assignVehicleToSpot(Vehicle vehicle, ParkingSpot spot) {
    spot.assignVehicle(vehicle);
    switch (spot.getType()) {
    case ParkingSpotType.HANDICAPPED:
      updateDisplayBoardForHandicapped(spot);
      break;
    case ParkingSpotType.COMPACT:
      updateDisplayBoardForCompact(spot);
      break;
    case ParkingSpotType.LARGE:
      updateDisplayBoardForLarge(spot);
      break;
    case ParkingSpotType.MOTORBIKE:
      updateDisplayBoardForMotorbike(spot);
      break;
    case ParkingSpotType.ELECTRIC:
      updateDisplayBoardForElectric(spot);
      break;
    default:
      print("Wrong parking spot type!");
    }
  }

  private void updateDisplayBoardForHandicapped(ParkingSpot spot) {
    if (this.displayBoard.getHandicappedFreeSpot().getNumber() == spot.getNumber()) {
      // find another free handicapped parking and assign to displayBoard
      for (String key : handicappedSpots.keySet()) {
        if (handicappedSpots.get(key).isFree()) {
          this.displayBoard.setHandicappedFreeSpot(handicappedSpots.get(key));
        }
      }
      this.displayBoard.showEmptySpotNumber();
    }
  }

  private void updateDisplayBoardForCompact(ParkingSpot spot) {
    if (this.displayBoard.getCompactFreeSpot().getNumber() == spot.getNumber()) {
      // find another free compact parking and assign to displayBoard
      for (String key : compactSpots.keySet()) {
        if (compactSpots.get(key).isFree()) {
          this.displayBoard.setCompactFreeSpot(compactSpots.get(key));
        }
      }
      this.displayBoard.showEmptySpotNumber();
    }
  }

  public void freeSpot(ParkingSpot spot) {
    spot.removeVehicle();
    switch (spot.getType()) {
    case ParkingSpotType.HANDICAPPED:
      freeHandicappedSpotCount++;
      break;
    case ParkingSpotType.COMPACT:
      freeCompactSpotCount++;
      break;
    case ParkingSpotType.LARGE:
      freeLargeSpotCount++;
      break;
    case ParkingSpotType.MOTORBIKE:
      freeMotorbikeSpotCount++;
      break;
    case ParkingSpotType.ELECTRIC:
      freeElectricSpotCount++;
      break;
    default:
      print("Wrong parking spot type!");
    }
  }
}

public class ParkingDisplayBoard {
  private String id;
  private HandicappedSpot handicappedFreeSpot;
  private CompactSpot compactFreeSpot;
  private LargeSpot largeFreeSpot;
  private MotorbikeSpot motorbikeFreeSpot;
  private ElectricSpot electricFreeSpot;

  public void showEmptySpotNumber() {
    String message = "";
    if(handicappedFreeSpot.IsFree()){
      message += "Free Handicapped: " + handicappedFreeSpot.getNumber();
    } else {
      message += "Handicapped is full";
    }
    message += System.lineSeparator();

    if(compactFreeSpot.IsFree()){
      message += "Free Compact: " + compactFreeSpot.getNumber();
    } else {
      message += "Compact is full";
    }
    message += System.lineSeparator();

    if(largeFreeSpot.IsFree()){
      message += "Free Large: " + largeFreeSpot.getNumber();
    } else {
      message += "Large is full";
    }
    message += System.lineSeparator();

    if(motorbikeFreeSpot.IsFree()){
      message += "Free Motorbike: " + motorbikeFreeSpot.getNumber();
    } else {
      message += "Motorbike is full";
    }
    message += System.lineSeparator();

    if(electricFreeSpot.IsFree()){
      message += "Free Electric: " + electricFreeSpot.getNumber();
    } else {
      message += "Electric is full";
    }

    Show(message);
  }
}


public class ParkingLot {
  private String name;
  private Location address;
  private ParkingRate parkingRate;

  private int compactSpotCount;
  private int largeSpotCount;
  private int motorbikeSpotCount;
  private int electricSpotCount;
  private final int maxCompactCount;
  private final int maxLargeCount;
  private final int maxMotorbikeCount;
  private final int maxElectricCount;

  private HashMap<String, EntrancePanel> entrancePanels;
  private HashMap<String, ExitPanel> exitPanels;
  private HashMap<String, ParkingFloor> parkingFloors;

  // all active parking tickets, identified by their ticketNumber
  private HashMap<String, ParkingTicket> activeTickets;

  // singleton ParkingLot to ensure only one object of ParkingLot in the system,
  // all entrance panels will use this object to create new parking ticket: getNewParkingTicket(),
  // similarly exit panels will also use this object to close parking tickets
  private static ParkingLot parkingLot = null;

  // private constructor to restrict for singleton
  private ParkingLot() {
    // 1. initialize variables: read name, address and parkingRate from database
    // 2. initialize parking floors: read the parking floor map from database,
    //  this map should tell how many parking spots are there on each floor. This
    //  should also initialize max spot counts too.
    // 3. initialize parking spot counts by reading all active tickets from database
    // 4. initialize entrance and exit panels: read from database
  }

  // static method to get the singleton instance of ParkingLot
  public static ParkingLot getInstance() {
    if (parkingLot == null) {
      parkingLot = new ParkingLot();
    }
    return parkingLot;
  }

  // note that the following method is 'synchronized' to allow multiple entrances
  // panels to issue a new parking ticket without interfering with each other
  public synchronized ParkingTicket getNewParkingTicket(Vehicle vehicle) throws ParkingFullException {
    if (this.isFull(vehicle.getType())) {
      throw new ParkingFullException();
    }
    ParkingTicket ticket = new ParkingTicket();
    vehicle.assignTicket(ticket);
    ticket.saveInDB();
    // if the ticket is successfully saved in the database, we can increment the parking spot count
    this.incrementSpotCount(vehicle.getType());
    this.activeTickets.put(ticket.getTicketNumber(), ticket);
    return ticket;
  }

  public boolean isFull(VehicleType type) {
    // trucks and vans can only be parked in LargeSpot
    if (type == VehicleType.Truck || type == VehicleType.Van) {
      return largeSpotCount >= maxLargeCount;
    }

    // motorbikes can only be parked at motorbike spots
    if (type == VehicleType.Motorbike) {
      return motorbikeSpotCount >= maxMotorbikeCount;
    }

    // cars can be parked at compact or large spots
    if (type == VehicleType.Car) {
      return (compactSpotCount + largeSpotCount) >= (maxCompactCount + maxLargeCount);
    }

    // electric car can be parked at compact, large or electric spots
    return (compactSpotCount + largeSpotCount + electricSpotCount) >= (maxCompactCount + maxLargeCount
        + maxElectricCount);
  }

  // increment the parking spot count based on the vehicle type
  private boolean incrementSpotCount(VehicleType type) {
    if (type == VehicleType.Truck || type == VehicleType.Van) {
      largeSpotCount++;
    } else if (type == VehicleType.Motorbike) {
      motorbikeSpotCount++;
    } else if (type == VehicleType.Car) {
      if (compactSpotCount < maxCompactCount) {
        compactSpotCount++;
      } else {
        largeSpotCount++;
      }
    } else { // electric car
      if (electricSpotCount < maxElectricCount) {
        electricSpotCount++;
      } else if (compactSpotCount < maxCompactCount) {
        compactSpotCount++;
      } else {
        largeSpotCount++;
      }
    }
  }

  public boolean isFull() {
    for (String key : parkingFloors.keySet()) {
      if (!parkingFloors.get(key).isFull()) {
        return false;
      }
    }
    return true;
  }

  public void addParkingFloor(ParkingFloor floor) {
    /* store in database */ }

  public void addEntrancePanel(EntrancePanel entrancePanel) {
    /* store in database */ }

  public void addExitPanel(ExitPanel exitPanel) {
    /* store in database */ }
}

```

#### Design Amazon - Online Shopping System:
##### Features:
  - User should be able to add new products to sell
  - Search by product name and category
  - Users can search and view all products but have to register to buy
  - Add/remove/modiy products from cart
  - checkout and buy from cart
  - rate and review products
  - user can specify shipping address where their product will be delivered
  - can order if the shipping not happend
  - notification of order status 
  - pay through credit cards or ebank
  - shipping status 
##### Models:
```
public class Address {
  private String streetAddress;
  private String city;
  private String state;
  private String zipCode;
  private String country;
}

public enum OrderStatus {
  UNSHIPPED, PENDING, SHIPPED, COMPLETED, CANCELED, REFUND_APPLIED
}

public enum AccountStatus {
  ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED, UNKNOWN
}

public enum ShipmentStatus {
  PENDING, SHIPPED, DELIVERED, ON_HOLD,
}

public enum PaymentStatus {
  UNPAID, PENDING, COMPLETED, FILLED, DECLINED, CANCELLED, ABANDONED, SETTLING, SETTLED, REFUNDED
}


public class Account {
  private String userName;
  private String password;
  private AccountStatus status;
  private String name;
  private Address shippingAddress;
  private String email;
  private String phone;

  private List<CreditCard> creditCards;
  private List<ElectronicBankTransfer> bankAccounts;

  public boolean addProduct(Product product);
  public boolean addProductReview(ProductReview review);
  public boolean resetPassword();
}

public abstract class Customer {
  private ShoppingCart cart;
  private Order order;

  public ShoppingCart getShoppingCart();
  public bool addItemToCart(Item item);
  public bool removeItemFromCart(Item item);
}

public class Guest extends Customer {
  public bool registerAccount();
}

public class Member extends Customer {
  private Account account;
  public OrderStatus placeOrder(Order order);
}

public class ProductCategory {
  private String name;
  private String description;
}

public class ProductReview {
  private int rating;
  private String review;

  private Member reviewer;
}

public class Product {
  private String productID;
  private String name;
  private String description;
  private double price;
  private ProductCategory category;
  private int availableItemCount;

  private Account seller;

  public int getAvailableCount();
  public boolean updatePrice(double newPrice);
}

public class Item {
  private String productID;
  private int quantity;
  private double price;

  public boolean updateQuantity(int quantity);
}

public class ShoppingCart {
  private List<Items> items;

  public boolean addItem(Item item);
  public boolean removeItem(Item item);
  public boolean updateItemQuantity(Item item, int quantity);
  public List<Item> getItems();
  public boolean checkout();
}

public class OrderLog {
  private String orderNumber;
  private Date creationDate;
  private OrderStatus status;
}

public class Order {
  private String orderNumber;
  private OrderStatus status;
  private Date orderDate;
  private List<OrderLog> orderLog;

  public boolean sendForShipment();
  public boolean makePayment(Payment payment);
  public boolean addOrderLog(OrderLog orderLog);
}

public class ShipmentLog {
  private String shipmentNumber;
  private ShipmentStatus status;
  private Date creationDate;
}

public class Shipment {
  private String shipmentNumber;
  private Date shipmentDate;
  private Date estimatedArrival;
  private String shipmentMethod;
  private List<ShipmentLog> shipmentLogs;

  public boolean addShipmentLog(ShipmentLog shipmentLog);
}

public abstract class Notification {
  private int notificationId;
  private Date createdOn;
  private String content;

  public boolean sendNotification(Account account);
}

public interface Search {
  public List<Product> searchProductsByName(String name);
  public List<Product> searchProductsByCategory(String category);
}

public class Catalog implements Search {
   HashMap<String, List<Product>> productNames;
   HashMap<String, List<Product>> productCategories;

  public List<Product> searchProductsByName(String name) {
    return productNames.get(name);
  }

  public List<Product> searchProductsByCategory(String category) {
    return productCategories.get(category);
  }
}


```

#### Design a Stack Overflow:
##### Features:
  - Any one can search and view questions and to add or comment need to become member
  - Member should be able to post questions
  - Memeber should be able to add answer to  open questions
  - Member can comment on any question answer or comment
  - Member can flag a question question or comment, for seroius problems or moderated questions
  - Any memeber can add a bounty to their question to draw attention
  - Member will get  badge for being helpful
  - Memeber can vote to close questions, moderator can close or reopen questions
  - Members can add tags to their questions, a tag is word or phrase that describes the topic of the question
  - Members can vote to delete questions
  - Can delete and undelete questions
  - Most frequently used tags in questions.
##### Models:
```
public enum QuestionStatus{
  OPEN,
  CLOSED,
  ON_HOLD,
  DELETED
}

public enum QuestionClosingRemark{
  DUPLICATE,
  OFF_TOPIC,
  TOO_BROAD,
  NOT_CONSTRUCTIVE,
  NOT_A_REAL_QUESTION,
  PRIMARILY_OPINION_BASED
}

public enum AccountStatus{
  ACTIVE,
  CLOSED,
  CANCELED,
  BLACKLISTED,
  BLOCKED
}

public class Account {
  private String id;
  private String password;
  private AccountStatus status;
  private String name;
  private Address address;
  private String email;
  private String phone;
  private int reputation;

  public boolean resetPassword();
}

public class Member {
  private Account account;
  private List<Badge> badges;

  public int getReputation();
  public String getEmail();
  public boolean createQuestion(Question question);
  public boolean createTag(Tag tag);
}

public class Admin extends Member {
  public boolean blockMember(Member member);
  public boolean unblockMember(Member member);
}

public class Moderator extends Member {
  public boolean closeQuestion(Question question);
  public boolean undeleteQuestion(Question question);
}

public class Badge {
  private String name;
  private String description;
}

public class Tag {
  private String name;
  private String description;
  private long dailyAskedFrequency;
  private long weeklyAskedFrequency;
}

public class Notification {
  private int notificationId;
  private Date createdOn;
  private String content;

  public boolean sendNotification();
}

public class Photo {
  private int photoId;
  private String photoPath;
  private Date creationDate;

  private Member creatingMember;

  public boolean delete();
}

public class Bounty {
  private int reputation;
  private Date expiry;

  public boolean modifyReputation(int reputation);
}

public interface Search {
  public static List<Question> search(String query);
}

public class Question implements Search {
  private String title;
  private String description;
  private int viewCount;
  private int voteCount;
  private Date creationTime;
  private Date updateTime;
  private QuestionStatus status;
  private QuestionClosingRemark closingRemark;

  private Member askingMember;
  private Bounty bounty;
  private List<Photo> photos;
  private List<Comment> comments;
  private List<Answer> answers;

  public boolean close();
  public boolean undelete();
  public boolean addComment(Comment comment);
  public boolean addBounty(Bounty bounty);

  public static List<Question> search(String query) {
    // return all questions containing the string query in their title or description.
  }
}

public class Comment {
  private String text;
  private Date creationTime;
  private int flagCount;
  private int voteCount;

  private Member askingMember;

  public boolean incrementVoteCount();
}

public class Answer {
  private String answerText;
  private boolean accepted;
  private int voteCount;
  private int flagCount;
  private Date creationTime;

  private Member creatingMember;
  private List<Photo> photos;

  public boolean incrementVoteCount();
}
  

```



#### Design a Movie Ticket Booking System:
##### Features:
  - List the cities where affilated cinemas located
  - Each cinema can have multiples halls, can run one cinema.
  - each movie will have multiple shows
  - search movies, name, generes, title, languages, release date, city name
  - one movie is selected, display  should show all cinemas running that movie and its shows.
  - customer should be able to select show at perticular cinema and book tickets
  - service should show customer seating arrangment of the cinema hall and customer should be able to select multiple seats according their preferance
  - distinggues available and booked seats
  - notification on new movie release and booking made or canceled.
  - pay through credit cards or cash
  - no two customers should book same seat
  - customer should be able to add coupons to booking
##### Models:
```
public enum BookingStatus {
  REQUESTED, PENDING, CONFIRMED, CHECKED_IN, CANCELED, ABANDONED
}

public enum SeatType {
  REGULAR, PREMIUM, ACCESSIBLE, SHIPPED, EMERGENCY_EXIT, OTHER
}

public enum AccountStatus {
  ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED, UNKNOWN
}

public enum PaymentStatus {
  UNPAID, PENDING, COMPLETED, FILLED, DECLINED, CANCELLED, ABANDONED, SETTLING, SETTLED, REFUNDED
}

public class Address {
  private String streetAddress;
  private String city;
  private String state;
  private String zipCode;
  private String country;
}

public class Account {
  private String id;
  private String password;
  private AccountStatus status;

  public boolean resetPassword();
}

public abstract class Person {
  private String name;
  private Address address;
  private String email;
  private String phone;

  private Account account;
}

public class Customer extends Person {
  public boolean makeBooking(Booking booking);
  public List<Booking> getBookings();
}

public class Admin extends Person {
  public boolean addMovie(Movie movie);
  public boolean addShow(Show show);
  public boolean blockUser(Customer customer);
}

public class FrontDeskOfficer extends Person {
  public boolean createBooking(Booking booking);
}

public class Guest {
  public bool registerAccount();
}

public class Show {
  private int showId;
  private Date createdOn;
  private Date startTime;
  private Date endTime;
  private CinemaHall playedAt;
  private Movie movie;
}

public class Movie {
  private String title;
  private String description;
  private int durationInMins;
  private String language;
  private Date releaseDate;
  private String country;
  private String genre;
  private Admin movieAddedBy;

  private List<Show> shows;
  public List<Show> getShows();
}

public class Booking {
  private String bookingNumber;
  private int numberOfSeats;
  private Date createdOn;
  private BookingStatus status;

  private Show show;
  private List<ShowSeat> seats;
  private Payment payment;

  public boolean makePayment(Payment payment);
  public boolean cancel();
  public boolean assignSeats(List<ShowSeat> seats);
}

public class ShowSeat extends CinemaHallSeat{
  private int showSeatId;
  private boolean isReserved;
  private double price;
}

public class Payment {
  private double amount;
  private Date createdOn;
  private int transactionId;
  private PaymentStatus status;
}

public class City {
  private String name;
  private String state;
  private String zipCode;
}

public class Cinema {
  private String name;
  private int totalCinemaHalls;
  private Address location;

  private List<CinemaHall> halls;
}

public class CinemaHall {
  private String name;
  private int totalSeats;

  private List<CinemaHallSeat> seats;
  private List<Show> shows;
}

public interface Search {
  public List<Movie> searchByTitle(String title);
  public List<Movie> searchByLanguage(String language);
  public List<Movie> searchByGenre(String genre);
  public List<Movie> searchByReleaseDate(Date relDate);
  public List<Movie> searchByCity(String cityName);
}

public class Catalog implements Search {
   HashMap<String, List<Movie>> movieTitles;
   HashMap<String, List<Movie>> movieLanguages;
   HashMap<String, List<Movie>> movieGenres;
   HashMap<Date, List<Movie>> movieReleaseDates;
   HashMap<String, List<Movie>> movieCities;

  public List<Movie> searchByTitle(String title) {
    return movieTitles.get(title);
  }

  public List<Movie> searchByLanguage(String language) {
    return movieLanguages.get(language);
  }

  //...

  public List<Movie> searchByCity(String cityName) {
    return movieCities.get(cityName);
  }
}

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

BEGIN TRANSACTION;

    -- Suppose we intend to reserve three seats (IDs: 54, 55, 56) for ShowID=99 
    Select * From ShowSeat where ShowID=99 && ShowSeatID in (54, 55, 56) && isReserved=0 

    -- if the number of rows returned by the above statement is NOT three, we can return failure to the user.
    update ShowSeat table...
    update Booking table ...

COMMIT TRANSACTION;

```



#### Design a an ATM:
##### Features:
  - Crad Reader - read user cards
  - KeyPad - PIN and Selections
  - Screen to display msgs to user
  - Cash Dispenser
  - Printer
  - Communication/Network Infrastructure
  - Balance InQuery
  - Deposite Cash
  - Desposit Check
  - Withdraw cash
  - Trasnfer funds
##### Models:
```
public enum TransactionType {
  BALANCE_INQUIRY, DEPOSIT_CASH, DEPOSIT_CHECK, WITHDRAW, TRANSFER
}

public enum TransactionStatus {
  SUCCESS, FAILURE, BLOCKED, FULL, PARTIAL, NONE
}

public enum CustomerStatus {
  ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED, CLOSED, UNKNOWN
}


public class Address {
  private String streetAddress;
  private String city;
  private String state;
  private String zipCode;
  private String country;
}

public class Customer {
  private String name;
  private String email;
  private String phone;
  private Address address;
  private CustomerStatus status;

  private Card card;
  private Account account;

  public boolean makeTransaction(Transaction transaction);
  public Address getBillingAddress();
}

public class Card {
  private String cardNumber;
  private String customerName;
  private Date cardExpiry;
  private int pin;

  public Address getBillingAddress();
}

public class Account {
  private int accountNumber;
  private double totalBalance;
  private double availableBalance;

  public double getAvailableBalance();
}

public class SavingAccount extends Account {
  private double withdrawLimit;
}

public class CheckingAccount extends Account {
  private String debitCardNumber;
}

public class Bank {
  private String name;
  private String bankCode;

  public String getBankCode();
  public boolean addATM();
}

public class ATM {
  private int atmID;
  private Address location;

  private CashDispenser cashDispenser;
  private Keypad keypad;
  private Screen screen;
  private Printer printer;
  private CheckDeposit checkDeposit;
  private CashDeposit cashDeposit;

  public boolean authenticateUser();
  public boolean makeTransaction(Customer customer, Transaction transaction);
}

public class CashDispenser {
  private int totalFiveDollarBills;
  private int totalTwentyDollarBills;

  public boolean dispenseCash(double amount);
  public boolean canDispenseCash();
}

public class Keypad {
  public String getInput();
}

public class Screen {
  public boolean showMessage(String message);
  public TransactionType getInput();
}

public class Printer {
  public boolean printReceipt(Transaction transaction);
}

public abstract class DepositSlot {
  private double totalAmount;
  public double getTotalAmount();
}

public class CheckDepositSlot extends DepositSlot {
  public double getCheckAmount();
}

public class CashDepositSlot extends DepositSlot {
  public double receiveDollarBill();
}


public abstract class Transaction {
  private int transactionId;
  private Date creationTime;
  private TransactionStatus status;
  public boolean makeTransation();
}

public class BalanceInquiry extends Transaction {
  private int accountId;
  public double getAccountId();
}

public abstract class Deposit extends Transaction {
  private double amount;
  public double getAmount();
}

public class CheckDeposit extends Deposit {
  private String checkNumber;
  private String bankCode;

  public String getCheckNumber();
}

public class CashDeposit extends Deposit {
  private double cashDepositLimit;
}

```



#### Design a  Airline Management System:
##### Features:
  - Search flights for given date, source and destination
  - Reserve tickets for flights and also multiple itinerary
  - check flight schedule, departure time, available seats, arrival time and other flight details 
  - multiple passanger under one itinerary
  - only admin can add new flights, flight schedule and cancle flights
  - Customer can cancel their flights
  - assigmnet of pilots, crew of flights
  - payments for reservations
  - notifications to customers, revervation made modified, and updated flights.
  
##### Models:
```
public enum FlightStatus{
  ACTIVE,
  SCHEDULED,
  DELAYED,
  DEPARTED,
  LANDED,
  IN_AIR,
  ARRIVED,
  CANCELLED,
  DIVERTED,
  UNKNOWN
}

public enum PaymentStatus{
  UNPAID,
  PENDING,
  COMPLETED,
  FILLED,
  DECLINED,
  CANCELLED,
  ABANDONED,
  SETTLING,
  SETTLED,
  REFUNDED
}

public enum ReservationStatus{
  REQUESTED,
  PENDING,
  CONFIRMED,
  CHECKED_IN,
  CANCELLED,
  ABANDONED
}
public enum SeatClass {
  ECONOMY,
  ECONOMY_PLUS,
  PREFERRED_ECONOMY,
  BUSINESS,
  FIRST_CLASS
}

public enum SeatType {
  REGULAR,
  ACCESSIBLE,
  EMERGENCY_EXIT,
  EXTRA_LEG_ROOM
}

public enum AccountStatus{
  ACTIVE,
  CLOSED,
  CANCELED,
  BLACKLISTED,
  BLOCKED
}

public class Address {
  private String streetAddress;
  private String city;
  private String state;
  private String zipCode;
  private String country;
}

public class Account {
  private String id;
  private String password;
  private AccountStatus status;

  public boolean resetPassword();
}


public abstract class Person {
  private String name;
  private Address address;
  private String email;
  private String phone;

  private Account account;
}

public class Customer extends Person {
  private String frequentFlyerNumber;

  public List<Itinerary> getItineraries();
}

public class Passenger {
  private String name;
  private String passportNumber;
  private Date dateOfBirth;

  public String getPassportNumber() {
    return this.passportNumber;
  }
}


public class Airport {
  private String name;
  private Address address;
  private String code;

  public List<Flight> getFlights();
}

public class Aircraft {
  private String name;
  private String model;
  private int manufacturingYear;
  private List<Seat> seats;

  public List<FlightInstance> getFlights();
}

public class Seat {
  private String seatNumber;
  private SeatType type;
  private SeatClass _class;
}

public class FlightSeat extends Seat {
  private double fare;
  public double getFare();
}

public class WeeklySchedule {
  private int dayOfWeek;
  private Time departureTime;
}

public class CustomSchedule {
  private Date customDate;
  private Time departureTime;
}

public class Flight {
  private String flightNumber;
  private Airport departure;
  private Airport arrival;
  private int durationInMinutes;

  private List<WeeklySchedules> weeklySchedules;
  private List<CustomSchedules> customSchedules;
  private List<FlightInstance> flightInstances;
}

public class FlightInstance {
  private Date departureTime;
  private String gate;
  private FlightStatus status;
  private Aircraft aircraft;

  public bool cancel();
  public void updateStatus(FlightStatus status);
}

public class FlightReservation {
  private String reservationNumber;
  private FlightInstance flight;
  private Map<Passenger, FlightSeat> seatMap;
  private Date creationDate;
  private ReservationStatus status;

  public static FlightReservation fetchReservationDetails(String reservationNumber);
  public List<Passenger> getPassengers();
}

public class Itinerary {
  private String customerId;
  private Airport startingAirport;
  private Airport finalAirport;
  private Date creationDate;
  private List<FlightReservation> reservations;

  public List<FlightReservation> getReservations();
  public boolean makeReservation();
  public boolean makePayment();
}

```

#### Hotel Management System:
##### Features:
  - Booking different rooms, standard, deluxe, familly suite
  - search room inventary and book any availability
  - 
##### Models:



#### Design a Library Management System:
##### Features:
##### Models:
