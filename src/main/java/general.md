
### Constructor: ### 

    Every class has a constructor whether it’s a normal class or a abstract class.
    Constructors are not methods and they don’t have any return type.
    Constructor name should match with class name .
    Constructor can use any access specifier, they can be declared as private also. Private constructors are possible in java but there scope is within the class only.
    Like constructors method can also have name same as class name, but still they have return type, though which we can identify them that they are methods not constructors.
    If you don’t implement any constructor within the class, compiler will do it for.
    this() and super() should be the first statement in the constructor code. If you don’t mention them, compiler does it for you accordingly.
    Constructor overloading is possible but overriding is not possible. Which means we can have overloaded constructor in our class but we can’t override a constructor.
    Constructors can not be inherited.
    If Super class doesn’t have a no-arg(default) constructor then compiler would not insert a default constructor in child class as it does in normal scenario.
    Interfaces do not have constructors.
    Abstract class can have constructor and it gets invoked when a class, which implements interface, is instantiated. (i.e. object creation of concrete class).
    A constructor can also invoke another constructor of the same class – By using this(). If you want to invoke a parameterized constructor then do it like this: this(parameter list).
    Whenever a child class constructor gets invoked it implicitly invokes the constructor of parent class. You can also say that the compiler inserts a super();
    When A constructor calls another constructor of same class then this is called constructor chaining. 

### Static Class, Block, Methods and Variables: ### 

    Static members belong to the class instead of a specific instance, this means if you make a member static, you can access it without object.
    Static block is used for initializing the static variables.This block gets executed when the class is loaded in the memory. A class can have multiple Static blocks, which will execute in the same sequence in which they have been written into the program.
    A static variable is common to all the instances (or objects) of the class because it is a class level variable. In other words you can say that only a single copy of static variable is created and shared among all the instances of the class. Memory allocation for such variables only happens once when the class is loaded in the memory.
    Static variables can be accessed directly in Static method, Static variables are shared among all the instances of class.
    Static Methods can access class variables(static variables) without using object(instance) of the class, however non-static methods and non-static variables can only be accessed using objects. Static methods can be accessed directly in static and non-static methods.
    A class can be made static only if it is a nested class. Nested static class doesn’t need reference of Outer class, A static class cannot access non-static members of the Outer class

### Inheritance: ### 

    The process by which one class acquires the properties(data members) and functionalities(methods) of another class is called inheritance.
    The derived class inherits all the members and methods that are declared as public or protected. If the members or methods of super class are declared as private then the derived class cannot use them directly. The private members can be accessed only in its own class. Such private members can only be accessed using public or protected getter and setter methods of super class.
    Single Inheritance: refers to a child and parent class relationship where a class extends the another class.
    Multilevel inheritance: refers to a child and parent class relationship where a class extends the child class. For example class C extends class B and class B extends class A.
    Hierarchical inheritance: refers to a child and parent class relationship where more than one classes extends the same class. For example, classes B, C & D extends the same class A.
    Multiple Inheritance: refers to the concept of one class extending more than one classes, which means a child class has two parent classes.
    Hybrid inheritance: Combination of more than one types of inheritance in a single program. For example class A & B extends class C and another class D extends class A then this is a hybrid inheritance
    constructor of sub class is invoked when we create the object of subclass, it by default invokes the default constructor of super class. Hence, in inheritance the objects are constructed top-down. The superclass constructor can be called explicitly using the super keyword, but it should be first statement in a constructor. 
    Inheritance and Method Overriding: When we declare the same method in child class which is already present in the parent class  this is called method overriding.

### Super Keyword: ### 

    To access the data members of parent class when both parent and child class have member with same name, To explicitly call the no-arg and parameterized constructor of parent class, To access the method of parent class when child class has overridden that method.

### Method Overloading: ### 

    Method Overloading is a feature that allows a class to have more than one method having the same name, if their argument lists are different.

### Method Overriding: ### 

    Declaring a method in sub class which is already present in parent class is known as method overriding. 
    The main advantage of method overriding is that the class can give its own specific implementation to a inherited method without even modifying the parent class code.
    Access Modifier of parent class method is public then the overriding method (child class method ) cannot have private, protected and default Access modifier,because all of these three access modifiers are more restrictive than public.
    private, static and final methods cannot be overridden as they are local to the class. However static methods can be re-declared in the sub class, in this case the sub-class method would act differently and will have nothing to do with the same static method of parent class.
    If a class is extending an abstract class or implementing an interface then it has to override all the abstract methods unless the class itself is a abstract class.

### Polymorphism: ### 

    Polymorphism is the capability of a method to do different things based on the object that it is acting upon.
    Method Overloading in Java – This is an example of compile time (or static polymorphism), Method Overriding in Java – This is an example of runtime time (or dynamic polymorphism), Types of Polymorphism – Runtime and compile time.

### Static and dynamic binding: ### 

    Association of method call to the method body is known as binding.
    Static binding happens at compile-time while dynamic binding happens at runtime.
    Binding of private, static and final methods always happen at compile time since these methods cannot be overridden. When the method overriding is actually happening and the reference of parent type is assigned to the object of child class type then such binding is resolved during runtime.
    The binding of overloaded methods is static and the binding of overridden methods is dynamic.

### Abstract class: ### 

    A class that is declared using “abstract” keyword is known as abstract class. It can have abstract methods(methods without body) as well as concrete methods (regular methods with body).
    Abstract class cannot be instantiated which means you cannot create the object of it. To use this class, you need to create another class that extends this class and provides the implementation of abstract methods, then you can use the object of that child class to call non-abstract methods of parent class as well as implemented methods.
    we can declare the parent class as abstract, which makes it a special class which is not complete on its own.
    Since abstract class allows concrete methods as well, it does not provide 100% abstraction. You can say that it provides partial abstraction. Abstraction is a process where you show only “relevant” data and “hide” unnecessary details of an object from the user.
    Interfaces on the other hand are used for 100% abstraction.
    if you declare an abstract method in a class then you must declare the class abstract as well. you can’t have abstract method in a concrete class. It’s vice versa is not always true: If a class is not having any abstract method then also it can be marked as abstract.
    An abstract class can extend only one class or one abstract class at a time, An abstract class can extend another concrete (regular) class or abstract class, An abstract class can have protected and public abstract methods, An abstract class can have static, final or static final variable with any access specifier.

### Interface: ### 

    interface looks like a class but it is not a class. An interface can have methods and variables just like the class but the methods declared in interface are by default abstract
    the variables declared in an interface are public, static & final by default.
    We can’t instantiate an interface in java. That means we cannot create the object of an interface
    Interface provides full abstraction as none of its methods have body. On the other hand abstract class provides partial abstraction as it can have abstract and concrete(methods with body) methods both.
    implements keyword is used by classes to implement an interface.
    While providing implementation in class of any method of an interface, it needs to be mentioned as public.
    Class that implements any interface must implement all the methods of that interface, else the class should be declared abstract.
    Interface cannot be declared as private, protected or transient.
    All the interface methods are by default abstract and public.
    Variables declared in interface are public, static and final by default.
    Interface variables must be initialized at the time of declaration otherwise compiler will throw an error.
    Inside any implementation class, you cannot change the variables declared in interface because by default, they are public, static and final.
    An interface can extend any interface but cannot implement it. Class implements interface and interface extends interface, A class can implement any number of interfaces.
    If there are two or more same methods in two interfaces and a class implements both interfaces, implementation of the method once is enough.
    A class cannot implement two interfaces that have methods with same name but different return type. Variable names conflicts can be resolved by interface name.
    Without bothering about the implementation part, we can achieve the security of implementation.

### Encapsulation: ### 

    Encapsulation simply means binding object state(fields) and behaviour(methods) together. If you are creating class, you are doing encapsulation. 
    The whole idea behind encapsulation is to hide the implementation details from users. 
    If a data member is private it means it can only be accessed within the same class. No outside class can access private data member (variable) of other class.
    However if we setup public getter and setter methods to update (for example void setSSN(int ssn))and read (for example  int getSSN()) the private data fields then the outside class can access those private data fields via public methods.
    It improves maintainability and flexibility and re-usability.
    
###  Final variable, Method and Class: ### 

    We cannot change the value of a final variable once it is initialized.
    A final variable that is not initialized at the time of declaration is known as blank final variable. We must initialize the blank final variable in constructor of the class otherwise it will throw a compilation error.
    A static final variable that is not initialized during declaration can only be initialized in static block. 
    A final method cannot be overridden. Which means even though a sub class can call the final method of parent class without any issues but it cannot override it.
    A final class not be inherited.
    A constructor cannot be declared as final, Local final variable must be initializing during declaration, All variables declared in an interface are by default final, If method parameters are declared final then the value of these parameters cannot be changed, It is a good practice to name final variable in all CAPS, 

### Exception Handling: ### 

    Exception handling ensures that the flow of the program doesn’t break when an exception occurs. By handling we make sure that all the statements execute and the flow of program doesn’t break.
    Errors indicate that something severe enough has gone wrong, the application should crash rather than try to handle the error.
    Exceptions are events that occurs in the code. A programmer can handle such conditions and take necessary corrective actions.
    Object<-Throwable<-All Exceptions<-IOException, SQLException, NoSuchMethod, RunTime Exception<-Arithmetic, ArrayIndexoutof, NullPointer, Numberformat
    Try block:
        The try block contains set of statements where an exception can occur. A try block is always followed by a catch block, which handles the exception that occurs in associated try block. A try block must be followed by catch blocks or finally block or both.			  
    Catch block
        A catch block is where you handle the exceptions, this block must follow the try block. A single try block can have several catch blocks associated with it. 
    Finally block:
        contains all the crucial statements that must be executed whether exception occurs or not. The statements present in this block will always execute regardless of whether exception occurs in try block or not such as closing a connection, stream 
        The statements present in the finally block execute even if the try block contains control transfer statements like return, break or continue.
    User-defined exception must extend Exception class. The exception is thrown using throw keyword.

### Java Enum: ### 

    An enum is a special type of data type which is basically a collection (set) of constants.
    While defining Enums, the constants should be declared first, prior to any fields or methods. When there are fields and methods declared inside Enum, the list of enum constants must end with a semicolon(;).

### Java Annotations: ### 

    allow us to add metadata information into our source code, although they are not a part of the program itself.
    Instructions to the compiler, Compile-time instructors, Runtime instructions.

### Java Collection's: ### 

    ArrayList:
    LinkedList:
    Vector:
    HashMap:
    TreeMap:
    LinkedHashMap:
    HashSet:
    TreeSet:
    HashTable:
    Queue:
    PriorityQueue:
    Deque and ArrayQueue:
    Comparable Interface:
    Comparator Interface: