https://github.com/learning-zone/java-interview-questions/blob/master/java-design-pattern-questions.md

Important Patterns : creational design patterns, decorator, proxy, iterator, observer and visitor patterns.

## Creational Patterns:
Creational design patterns relate to how objects are constructed from classes. New-ing up objects may sound trivial but unthoughtfully littering 
code with object instance creations can lead to headaches down the road. The creational design pattern come with powerful suggestions on how best
to encapsulate the object creation process in a program.

#### Builder:
  - builder pattern encapsulates or hides the process of building a complex object and separates the representation of the object and its construction. 
    The separation allows us to construct different representations using the same construction process. 
  - different representations implies creating objects of different classes that may share the same construction process.
  - Used when we have too many arguments to send to constructor and its hard to maintain the order.
  - When we don't want to send all parameters in Object Initialization.
  - The builder pattern is a good choice when designing classes whose constructors or static factories would have more than a handful of parameters.
  - Whenever creation of new object requires setting many parameters and some of them (or all of them) are optional.
  ```
  public class Pizza {
    private int size;
    private boolean cheese;
    private boolean pepperoni;
    private boolean bacon;
  
    public static class Builder {
      //required
      private final int size;
  
      //optional
      private boolean cheese = false;
      private boolean pepperoni = false;
      private boolean bacon = false;
  
      public Builder(int size) {
        this.size = size;
      }
  
      public Builder cheese(boolean value) {
        cheese = value;
        return this;
      }
  
      public Builder pepperoni(boolean value) {
        pepperoni = value;
        return this;
      }
  
      public Builder bacon(boolean value) {
        bacon = value;
        return this;
      }
  
      public Pizza build() {
        return new Pizza(this);
      }
    }
  
    private Pizza(Builder builder) {
      size = builder.size;
      cheese = builder.cheese;
      pepperoni = builder.pepperoni;
      bacon = builder.bacon;
    }
  }
  
  
  Pizza pizza = new Pizza.Builder(12)
                         .cheese(true)
                         .pepperoni(true)
                         .bacon(true)
                         .build();
                         
               
  ```
  - This results in code that is easy to write and very easy to read and understand. he build method could be modified to check parameters after they have been copied from the builder to the Pizza object and throw an IllegalStateException if an invalid parameter value has been supplied
  
  ```
  class Vehicle {
    //required parameter
    private String engine;
    private int wheel;
    
    //optional parameter
    private int airbags;
    
    public String getEngine() {
      return this.engine;
    }
    
    public int getWheel() {
      return this.wheel;
    }
    
    public int getAirbags() {
      return this.airbags;
    }
    
    private Vehicle(VehicleBuilder builder) {
      this.engine = builder.engine;
      this.wheel = builder.wheel;
      this.airbags = builder.airbags;
    }
    
    public static class VehicleBuilder {
      private String engine;
      private int wheel;
      
      private int airbags;
      
      public VehicleBuilder(String engine, int wheel) {
        this.engine = engine;
        this.wheel = wheel;
      }
      
      public VehicleBuilder setAirbags(int airbags) {
        this.airbags = airbags;
        return this;
      }
      
      public Vehicle build() {
        return new Vehicle(this);
      }
    }
  }
  
  Vehicle car = new Vehicle.VehicleBuilder("1500cc", 4).setAirbags(4).build();
  Vehicle bike = new Vehicle.VehicleBuilder("250cc", 2).build();
  ```
  
#### Singleton:

  - Singleton pattern as the name suggests is used to create one and only instance of a class
  - Its trivial to new-up an object of a class but how do we ensure that only one object ever gets created? The answer is to make the constructor 
    private of the class we intend to define as singleton. That way, only the members of the class can access the private constructor and no one else.
  - ensuring that only a single instance of a class exists and a global point of access to it exists.
  - Eager initialization:


      public class EagerInitializedSingleton {
          
          private static final EagerInitializedSingleton instance = new EagerInitializedSingleton();
          
          // private constructor to avoid client applications to use constructor
          private EagerInitializedSingleton(){}
      
          public static EagerInitializedSingleton getInstance(){
              return instance;
          }
      }
  - Static block initialization:
  

     public class StaticBlockSingleton  {
     
         private static StaticBlockSingleton  instance;
         
         private StaticBlockSingleton (){}
         
         // static block initialization for exception handling
         static{
             try{
                 instance = new StaticBlockSingleton ();
             }catch(Exception e){
                 throw new RuntimeException("Exception occured in creating Singleton instance");
             }
         }
         
         public static StaticBlockSingleton getInstance(){
             return instance;
         }
     }
     
  - Lazy Initialization:
    
    
    public class LazyInitializedSingleton  {
    
        private static LazyInitializedSingleton  instance;
        
        private LazyInitializedSingleton(){}
        
        public static LazyInitializedSingleton  getInstance(){
            if(instance == null){
                instance = new LazyInitializedSingleton ();
            }
            return instance;
        }
    }
    
  - Thread Safe Singleton:
    
    
    public class ThreadSafeSingleton {
    
        private static ThreadSafeSingleton instance;
        
        private ThreadSafeSingleton(){}
        
        public static synchronized ThreadSafeSingleton getInstance(){
            if(instance == null){
                instance = new ThreadSafeSingleton();
            }
            return instance;
        }
    }
    
  - Bill Pugh Singleton Implementation:
  
     
    public class BillPughSingleton {
    
        private BillPughSingleton(){}
        
        private static class SingletonHelper{
            private static final BillPughSingleton INSTANCE = new BillPughSingleton();
        }
        
        public static BillPughSingleton getInstance(){
            return SingletonHelper.INSTANCE;
        }
    }
  
#### Prototype:
  - Prototype design pattern is used when the Object creation is a costly affair and requires a lot of time and resources and you have a similar object already existing.
  - Prototype pattern provides a mechanism to copy the original object to a new object and then modify it according to our needs. Prototype design pattern uses java cloning to copy the object.
  - Example, Suppose we have an Object that loads data from database. Now we need to modify this data in our program multiple times, 
    so it’s not a good idea to create the Object using new keyword and load all the data again from database. 
    The better approach would be to clone the existing object into a new object and then do the data manipulation.
  - Prototype design pattern mandates that the Object which you are copying should provide the copying feature. 
  
  ```public class Employees implements Cloneable{
     
     	private List<String> empList;
     	
     	public Employees(){
     		empList = new ArrayList<String>();
     	}
     	
     	public Employees(List<String> list){
     		this.empList=list;
     	}
     	public void loadData(){
     		//read all employees from database and put into the list
     		empList.add("Pankaj");
     		empList.add("Raj");
     		empList.add("David");
     		empList.add("Lisa");
     	}
     	
     	public List<String> getEmpList() {
     		return empList;
     	}
     
     	@Override
     	public Object clone() throws CloneNotSupportedException{
     			List<String> temp = new ArrayList<String>();
     			for(String s : this.getEmpList()){
     				temp.add(s);
     			}
     			return new Employees(temp);
     	}
     	
     }
     
     Notice that the clone method is overridden to provide a deep copy of the employees list.
     Employees emps = new Employees();
     emps.loadData();	
     //Use the clone method to get the Employee object
     Employees empsNew = (Employees) emps.clone();
     Employees empsNew1 = (Employees) emps.clone();
     List<String> list = empsNew.getEmpList();
     list.add("John");
     List<String> list1 = empsNew1.getEmpList();
     list1.remove("Pankaj");
   ```
    
#### Factory:

  - providing an interface for object creation but delegating the actual instantiation of objects to subclasses.
  - used when we have multiple sub classes of super class and based on input we want to return one class instance.
  - it provides abstraction between implementation and client classes,
  - removes the instantiation of client class from client code.
  - This pattern takes out the responsibility of the instantiation of a class from the client program to the factory class.
  - Factory pattern provides abstraction between implementation and client classes through inheritance.
  
  ```
  public class F16 {
  
      IEngine engine;
      ICockpit cockpit;
  
      protected F16 makeF16() {
          engine = new F16Engine();
          cockpit = new F16Cockpit();
          return this;
      }
  
      public void taxi() {
          System.out.println("F16 is taxing on the runway !");
      }
  
      public void fly() {
          // Note here carefully, the superclass F16 doesn't know
          // what type of F-16 variant it was returned.
          F16 f16 = makeF16();
          f16.taxi();
          System.out.println("F16 is in the air !");
      }
  }
  
  public class F16A extends F16 {
  
      @Override
      public F16 makeF16() {
          super.makeF16();
          engine = new F16AEngine();
          return this;
      }
  }
  
  public class F16B extends F16 {
  
      @Override
      public F16 makeF16() {
          super.makeF16();
          engine = new F16BEngine();
          return this;
      }
  }
  
  Collection<F16> myAirForce = new ArrayList<F16>();
          F16 f16A = new F16A();
          F16 f16B = new F16B();
          myAirForce.add(f16A);
          myAirForce.add(f16B);
  
          for (F16 f16 : myAirForce) {
              f16.fly();
          }
          
  abstract class Vehicle {
    public abstract int getWheel();
    
    public String toString() {
      return "Wheel: " + this.getWheel();
    }
  }
  
  class Car extends Vehicle {
    int wheel;
    
    Car(int wheel) {
      this.wheel = wheel;
    }
  
    @Override
    public int getWheel() {
      return this.wheel;
    }
  }
  
  class Bike extends Vehicle {
    int wheel;
    
    Bike(int wheel) {
      this.wheel = wheel;
    }
    
    @Override
    public int getWheel() {
      return this.wheel;
    }
  }
  
  class VehicleFactory {
    public static Vehicle getInstance(String type, int wheel) {
      if(type == "car") {
        return new Car(wheel);
      } else if(type == "bike") {
        return new Bike(wheel);
      }
      
      return null;
    }
  }
  
  Vehicle car = VehicleFactory.getInstance("car", 4);
  Vehicle bike = VehicleFactory.getInstance("bike", 2);
  ```
  
  
#### Abstract Factory Pattern:
  - Factory for Factories, defining an interface to create families of related or dependent objects without specifying their concrete classes.
  - Abstract Factory design pattern provides approach to code for interface rather than implementation.
  - Abstract Factory pattern is “factory of factories” and can be easily extended to accommodate more products, for example we can add another sub-class Laptop and a factory LaptopFactory.
  - Abstract Factory pattern is robust and avoid conditional logic of Factory pattern.
  ```
  public abstract class Computer {
       
      public abstract String getRAM();
      public abstract String getHDD();
      public abstract String getCPU();
       
      @Override
      public String toString(){
          return "RAM= "+this.getRAM()+", HDD="+this.getHDD()+", CPU="+this.getCPU();
      }
  }
  public class PC extends Computer {
   
      private String ram;
      private String hdd;
      private String cpu;
       
      public PC(String ram, String hdd, String cpu){
          this.ram=ram;
          this.hdd=hdd;
          this.cpu=cpu;
      }
      @Override
      public String getRAM() {
          return this.ram;
      }
   
      @Override
      public String getHDD() {
          return this.hdd;
      }
   
      @Override
      public String getCPU() {
          return this.cpu;
      }
   
  }
  public class Server extends Computer {
   
      private String ram;
      private String hdd;
      private String cpu;
       
      public Server(String ram, String hdd, String cpu){
          this.ram=ram;
          this.hdd=hdd;
          this.cpu=cpu;
      }
      @Override
      public String getRAM() {
          return this.ram;
      }
   
      @Override
      public String getHDD() {
          return this.hdd;
      }
   
      @Override
      public String getCPU() {
          return this.cpu;
      }
   
  }
  import com.journaldev.design.model.Computer;
   
   public interface ComputerAbstractFactory {
   
   	public Computer createComputer();
   
   }
   
   public class PCFactory implements ComputerAbstractFactory {
   
   	private String ram;
   	private String hdd;
   	private String cpu;
   	
   	public PCFactory(String ram, String hdd, String cpu){
   		this.ram=ram;
   		this.hdd=hdd;
   		this.cpu=cpu;
   	}
   	@Override
   	public Computer createComputer() {
   		return new PC(ram,hdd,cpu);
   	}
   
   }
   public class ServerFactory implements ComputerAbstractFactory {
   
   	private String ram;
   	private String hdd;
   	private String cpu;
   	
   	public ServerFactory(String ram, String hdd, String cpu){
   		this.ram=ram;
   		this.hdd=hdd;
   		this.cpu=cpu;
   	}
   	
   	@Override
   	public Computer createComputer() {
   		return new Server(ram,hdd,cpu);
   	}
   
   }
   public class ComputerFactory {
   
   	public static Computer getComputer(ComputerAbstractFactory factory){
   		return factory.createComputer();
   	}
   }
  ```


## Structural Patterns:
Structural patterns are concerned with the composition of classes i.e. how the classes are made up or constructed

#### Adapter Pattern:
  - Adapter design pattern is one of the structural design pattern and its used so that two unrelated interfaces can work together. The object that joins these unrelated interface is called an Adapter.
  - While implementing Adapter pattern, there are two approaches – class adapter(extend class) and object adapter(create object inside adapter) – however both these approaches produce same result.
  - You can use the Adapter design pattern when you have to deal with different interfaces with similar behavior (which usually means classes with similar behavior but with different methods). 
    An example of it would be a class to connect to a Samsung TV and another one to connect to a Sony TV. They will share common behavior like open menu, start playback, connect to a network and 
    etc but each library will have a different implementation of it (with different method names and signatures)
  ```public class Volt {
     
     	private int volts;
     	
     	public Volt(int v){
     		this.volts=v;
     	}
     
     	public int getVolts() {
     		return volts;
     	}
     
     	public void setVolts(int volts) {
     		this.volts = volts;
     	}
     }
     
     public class Socket {
     
     	public Volt getVolt(){
     		return new Volt(120);
     	}
     }
     
     public interface SocketAdapter {
     
     	public Volt get120Volt();
     		
     	public Volt get12Volt();
     	
     	public Volt get3Volt();
     }
     
    class base adapter
    
    public class SocketClassAdapterImpl extends Socket implements SocketAdapter{
    
    	@Override
    	public Volt get120Volt() {
    		return getVolt();
    	}
    
    	@Override
    	public Volt get12Volt() {
    		Volt v= getVolt();
    		return convertVolt(v,10);
    	}
    
    	@Override
    	public Volt get3Volt() {
    		Volt v= getVolt();
    		return convertVolt(v,40);
    	}
    	
    	private Volt convertVolt(Volt v, int i) {
    		return new Volt(v.getVolts()/i);
    	}
    
    }
    
    Object based Adapter
    
    public class SocketObjectAdapterImpl implements SocketAdapter{
    
    	//Using Composition for adapter pattern
    	private Socket sock = new Socket();
    	
    	@Override
    	public Volt get120Volt() {
    		return sock.getVolt();
    	}
    
    	@Override
    	public Volt get12Volt() {
    		Volt v= sock.getVolt();
    		return convertVolt(v,10);
    	}
    
    	@Override
    	public Volt get3Volt() {
    		Volt v= sock.getVolt();
    		return convertVolt(v,40);
    	}
    	
    	private Volt convertVolt(Volt v, int i) {
    		return new Volt(v.getVolts()/i);
    	}
    } 
     
     
    interface Shape {
        public int calculateArea(int r);
    }
    
   class Square implements Shape {
       @Override
       public int calculateArea(int r) {
           return r * r;
       }
   }
   
   class Circle {
       public double calculateCircularArea (int r) {
           return 3.14 * r * r;
       }
   }
   
   class CirCleAdaptor extends Circle implements Shape {
       @Override
       public int calculateArea(int r) {
           return (int) calculateCircularArea(r);
       }
   }
   CircleAdaptor - Is the Adaptor for Circle
   Circle - Is the Adaptee
   Shape - Is the Target Interface
  ```

#### Bridge Pattern:
  - bridge design pattern is used to decouple the interfaces from implementation and hiding the implementation details from the client programs.
  - Decouple an abstraction from its implementation so that the two can vary independently
  - Bridge design pattern can be used when both abstraction and implementation can have different hierarchies independently 
    and we want to hide the implementation from the client application.
  - Increases code re-usability, Reduces the duplicate code , Increase the productivity as it saves both development and testing efforts, 
  ```
  public interface Color {
  
  	public void applyColor();
  }
  public abstract class Shape {
  	//Composition - implementor
  	protected Color color;
  	
  	//constructor with implementor as input argument
  	public Shape(Color c){
  		this.color=c;
  	}
  	
  	abstract public void applyColor();
  }
  public class Triangle extends Shape{
  
  	public Triangle(Color c) {
  		super(c);
  	}
  
  	@Override
  	public void applyColor() {
  		System.out.print("Triangle filled with color ");
  		color.applyColor();
  	} 
  
  }
  
  public class Pentagon extends Shape{
  
  	public Pentagon(Color c) {
  		super(c);
  	}
  
  	@Override
  	public void applyColor() {
  		System.out.print("Pentagon filled with color ");
  		color.applyColor();
  	} 
  
  }
  
  public class RedColor implements Color{
  
  	public void applyColor(){
  		System.out.println("red.");
  	}
  }
  
  public class GreenColor implements Color{
  
  	public void applyColor(){
  		System.out.println("green.");
  	}
  }
  
  Shape tri = new Triangle(new RedColor());
  tri.applyColor();
  Shape pent = new Pentagon(new GreenColor());
  pent.applyColor();
  
  
                     ----Shape---
                    /            \
           Rectangle              Circle
          /         \            /      \
  BlueRectangle  RedRectangle BlueCircle RedCircle
  Refactor to
            ----Shape---                        Color
           /            \                       /   \
  Rectangle(Color)   Circle(Color)           Blue   Red
  
  
  /* Implementor interface*/
  interface Gear{
      void handleGear();
  }
  
  /* Concrete Implementor - 1 */
  class ManualGear implements Gear{
      public void handleGear(){
          System.out.println("Manual gear");
      }
  }
  /* Concrete Implementor - 2 */
  class AutoGear implements Gear{
      public void handleGear(){
          System.out.println("Auto gear");
      }
  }
  /* Abstraction (abstract class) */
  abstract class Vehicle {
      Gear gear;
      public Vehicle(Gear gear){
          this.gear = gear;
      }
      abstract void addGear();
  }
  /* RefinedAbstraction - 1*/
  class Car extends Vehicle{
      public Car(Gear gear){
          super(gear);
          // initialize various other Car components to make the car
      }
      public void addGear(){
          System.out.print("Car handles ");
          gear.handleGear();
      }
  }
  /* RefinedAbstraction - 2 */
  class Truck extends Vehicle{
      public Truck(Gear gear){
          super(gear);
          // initialize various other Truck components to make the car
      }
      public void addGear(){
          System.out.print("Truck handles " );
          gear.handleGear();
      }
  }
  /* Client program */
  public class BridgeDemo {    
      public static void main(String args[]){
          Gear gear = new ManualGear();
          Vehicle vehicle = new Car(gear);
          vehicle.addGear();
  
          gear = new AutoGear();
          vehicle = new Car(gear);
          vehicle.addGear();
  
          gear = new ManualGear();
          vehicle = new Truck(gear);
          vehicle.addGear();
  
          gear = new AutoGear();
          vehicle = new Truck(gear);
          vehicle.addGear();
      }
  }
  ```

#### Composite Pattern:
  - Composite design pattern is used when we have to represent a part-whole hierarchy.
  - Composite pattern should be applied only when the group of objects should behave as the single object.
  - Composite design pattern can be used to create a tree like structure.
  ```
  public interface Shape {
  	
  	public void draw(String fillColor);
  }
  public class Triangle implements Shape {
  
  	@Override
  	public void draw(String fillColor) {
  		System.out.println("Drawing Triangle with color "+fillColor);
  	}
  
  }
  public class Circle implements Shape {
  
  	@Override
  	public void draw(String fillColor) {
  		System.out.println("Drawing Circle with color "+fillColor);
  	}
  }
  public class Drawing implements Shape{
  
  	//collection of Shapes
  	private List<Shape> shapes = new ArrayList<Shape>();
  	
  	@Override
  	public void draw(String fillColor) {
  		for(Shape sh : shapes)
  		{
  			sh.draw(fillColor);
  		}
  	}
  	
  	//adding shape to drawing
  	public void add(Shape s){
  		this.shapes.add(s);
  	}
  	
  	//removing shape from drawing
  	public void remove(Shape s){
  		shapes.remove(s);
  	}
  	
  	//removing all the shapes
  	public void clear(){
  		System.out.println("Clearing all the shapes from drawing");
  		this.shapes.clear();
  	}
  }
  
  Shape tri = new Triangle();
  Shape tri1 = new Triangle();
  Shape cir = new Circle();
  
  Drawing drawing = new Drawing();
  drawing.add(tri1);
  drawing.add(tri1);
  drawing.add(cir);
  
  drawing.draw("Red");
  
  drawing.clear();
  
  drawing.add(tri);
  drawing.add(cir);
  drawing.draw("Green");
  ```

#### Decorator Pattern:
  - is used to modify the functionality of an object at runtime. At the same time other instances of the same class will not be affected by this, so individual object gets the modified behavior.
  - We use inheritance or composition to extend the behavior of an object but this is done at compile time and its applicable to all the instances of the class. 
    We can’t add any new functionality of remove any existing behavior at runtime – this is when Decorator pattern comes into picture.
  - Suppose we want to implement different kinds of cars – we can create interface Car to define the assemble method and then we can have a Basic car, further more we can extend it to Sports car and Luxury Car.
  - But if we want to get a car at runtime that has both the features of sports car and luxury car, then the implementation gets complex and if further more we want to specify which features should be added first,
    it gets even more complex. Now imagine if we have ten different kind of cars, the implementation logic using inheritance and composition will be impossible to manage. To solve this kind of programming situation, we apply decorator pattern in java.
  - Decorator design pattern is helpful in providing runtime modification abilities and hence more flexible. Its easy to maintain and extend when the number of choices are more.
  - he disadvantage of decorator design pattern is that it uses a lot of similar kind of objects (decorators).
  - Decorator pattern is used a lot in Java IO classes, such as FileReader, BufferedReader etc.
  
  ```
  public interface Car {
  
  	public void assemble();
  }
  
  public class BasicCar implements Car {
  
  	@Override
  	public void assemble() {
  		System.out.print("Basic Car.");
  	}
  
  }
  public class CarDecorator implements Car {
  
  	protected Car car;
  	
  	public CarDecorator(Car c){
  		this.car=c;
  	}
  	
  	@Override
  	public void assemble() {
  		this.car.assemble();
  	}
  
  }
  
  public class SportsCar extends CarDecorator {
  
  	public SportsCar(Car c) {
  		super(c);
  	}
  
  	@Override
  	public void assemble(){
  		super.assemble();
  		System.out.print(" Adding features of Sports Car.");
  	}
  }
  
  public class LuxuryCar extends CarDecorator {
  
  	public LuxuryCar(Car c) {
  		super(c);
  	}
  	
  	@Override
  	public void assemble(){
  		super.assemble();
  		System.out.print(" Adding features of Luxury Car.");
  	}
  }
  
  Car sportsCar = new SportsCar(new BasicCar());
  sportsCar.assemble();
  System.out.println("\n*****");
  
  Car sportsLuxuryCar = new SportsCar(new LuxuryCar(new BasicCar()));
  sportsLuxuryCar.assemble();
  
  ```

#### Facade Pattern:
  - Facade design pattern is used to help client applications to easily interact with the system.
  - Provide a unified interface to a set of interfaces in a subsystem. Facade Pattern defines a higher-level interface that makes the subsystem easier to use.
  - Suppose we have an application with set of interfaces to use MySql/Oracle database and to generate different types of reports, such as HTML report, PDF report etc
    So we will have different set of interfaces to work with different types of database. Now a client application can use these interfaces to get the required database connection and generate reports
    But when the complexity increases or the interface behavior names are confusing, client application will find it difficult to manage it.
    So we can apply Facade design pattern here and provide a wrapper interface on top of the existing interface to help client application.
  - Facade design pattern is more like a helper for client applications, it doesn’t hide subsystem interfaces from the client. Whether to use Facade or not is completely dependent on client code.
  - Facade design pattern can be applied at any point of development, usually when the number of interfaces grow and system gets complex.
  - Subsystem interfaces are not aware of Facade and they shouldn’t have any reference of the Facade interface.
  - Facade design pattern should be applied for similar kind of interfaces, its purpose is to provide a single interface rather than multiple interfaces that does the similar kind of jobs.
  - We can use Factory pattern with Facade to provide better interface to client systems
  ```
  public class MySqlHelper {
  	
  	public static Connection getMySqlDBConnection(){
  		//get MySql DB connection using connection parameters
  		return null;
  	}
  	
  	public void generateMySqlPDFReport(String tableName, Connection con){
  		//get data from table and generate pdf report
  	}
  	
  	public void generateMySqlHTMLReport(String tableName, Connection con){
  		//get data from table and generate pdf report
  	}
  }
  
  public class OracleHelper {
  
  	public static Connection getOracleDBConnection(){
  		//get Oracle DB connection using connection parameters
  		return null;
  	}
  	
  	public void generateOraclePDFReport(String tableName, Connection con){
  		//get data from table and generate pdf report
  	}
  	
  	public void generateOracleHTMLReport(String tableName, Connection con){
  		//get data from table and generate pdf report
  	}
  	
  }
  
  public class HelperFacade {
  
  	public static void generateReport(DBTypes dbType, ReportTypes reportType, String tableName){
  		Connection con = null;
  		switch (dbType){
  		case MYSQL: 
  			con = MySqlHelper.getMySqlDBConnection();
  			MySqlHelper mySqlHelper = new MySqlHelper();
  			switch(reportType){
  			case HTML:
  				mySqlHelper.generateMySqlHTMLReport(tableName, con);
  				break;
  			case PDF:
  				mySqlHelper.generateMySqlPDFReport(tableName, con);
  				break;
  			}
  			break;
  		case ORACLE: 
  			con = OracleHelper.getOracleDBConnection();
  			OracleHelper oracleHelper = new OracleHelper();
  			switch(reportType){
  			case HTML:
  				oracleHelper.generateOracleHTMLReport(tableName, con);
  				break;
  			case PDF:
  				oracleHelper.generateOraclePDFReport(tableName, con);
  				break;
  			}
  			break;
  		}
  		
  	}
  	
  	public static enum DBTypes{
  		MYSQL,ORACLE;
  	}
  	
  	public static enum ReportTypes{
  		HTML,PDF;
  	}
  }
  
  //generating MySql HTML report and Oracle PDF report without using Facade
  Connection con = MySqlHelper.getMySqlDBConnection();
  MySqlHelper mySqlHelper = new MySqlHelper();
  mySqlHelper.generateMySqlHTMLReport(tableName, con);
  
  Connection con1 = OracleHelper.getOracleDBConnection();
  OracleHelper oracleHelper = new OracleHelper();
  oracleHelper.generateOraclePDFReport(tableName, con1);
  
  //generating MySql HTML report and Oracle PDF report using Facade
  HelperFacade.generateReport(HelperFacade.DBTypes.MYSQL, HelperFacade.ReportTypes.HTML, tableName);
  HelperFacade.generateReport(HelperFacade.DBTypes.ORACLE, HelperFacade.ReportTypes.PDF, tableName);
  ```

#### FlyWeight Pattern:
  - Flyweight design pattern is used when we need to create a lot of Objects of a class. Since every object consumes memory space that can be crucial for low memory devices, such as mobile devices or embedded systems, flyweight design pattern can be applied to reduce the load on memory by sharing objects.
  - Following Factors :
        - The number of Objects to be created by application should be huge.
        - The object creation is heavy on memory and it can be time consuming too.
        - The object properties can be divided into intrinsic and extrinsic properties, extrinsic properties of an Object should be defined by the client program.
  - To apply flyweight pattern, we need to divide Object property into intrinsic and extrinsic properties. Intrinsic properties make the Object unique whereas 
    extrinsic properties are set by client code and used to perform different operations. For example, an Object Circle can have extrinsic properties such as color and width.
  
  ```
  public interface Shape {
  
  	public void draw(Graphics g, int x, int y, int width, int height,
  			Color color);
  }
  
  public class Line implements Shape {
  
  	public Line(){
  		System.out.println("Creating Line object");
  		//adding time delay
  		try {
  			Thread.sleep(2000);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
  	}
  	@Override
  	public void draw(Graphics line, int x1, int y1, int x2, int y2,
  			Color color) {
  		line.setColor(color);
  		line.drawLine(x1, y1, x2, y2);
  	}
  }
  
  public class Oval implements Shape {
  	
  	//intrinsic property
  	private boolean fill;
  	
  	public Oval(boolean f){
  		this.fill=f;
  		System.out.println("Creating Oval object with fill="+f);
  		//adding time delay
  		try {
  			Thread.sleep(2000);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
  	}
  	@Override
  	public void draw(Graphics circle, int x, int y, int width, int height,
  			Color color) {
  		circle.setColor(color);
  		circle.drawOval(x, y, width, height);
  		if(fill){
  			circle.fillOval(x, y, width, height);
  		}
  	}
  
  }
  
   I have intensionally introduced delay in creating the Object of concrete classes to make the point that flyweight pattern can be used for Objects that takes a lot of time while instantiated
   
   public class ShapeFactory {
   
   	private static final HashMap<ShapeType,Shape> shapes = new HashMap<ShapeType,Shape>();
   
   	public static Shape getShape(ShapeType type) {
   		Shape shapeImpl = shapes.get(type);
   
   		if (shapeImpl == null) {
   			if (type.equals(ShapeType.OVAL_FILL)) {
   				shapeImpl = new Oval(true);
   			} else if (type.equals(ShapeType.OVAL_NOFILL)) {
   				shapeImpl = new Oval(false);
   			} else if (type.equals(ShapeType.LINE)) {
   				shapeImpl = new Line();
   			}
   			shapes.put(type, shapeImpl);
   		}
   		return shapeImpl;
   	}
   	
   	public static enum ShapeType{
   		OVAL_FILL,OVAL_NOFILL,LINE;
   	}
   }
   
  for (int i = 0; i < 20; ++i) {
    Shape shape = ShapeFactory.getShape(getRandomShape());
    shape.draw(g, getRandomX(), getRandomY(), getRandomWidth(),
        getRandomHeight(), getRandomColor());
  }
  
  ```
  
  - Flyweight pattern introduces complexity and if number of shared objects are huge then there is a trade of between memory and time, so we need to use it judiciously based on our requirements.
  - Flyweight pattern implementation is not useful when the number of intrinsic properties of Object is huge, making implementation of Factory class complex

#### Proxy Pattern:
  - Provide a surrogate or placeholder for another object to control access to it. proxy design pattern is used when we want to provide controlled access of a functionality.
  - Let’s say we have a class that can run some command on the system. Now if we are using it, its fine but if we want to give this program to a client application, it can have severe issues because client program can issue command to delete some system files or change some settings that you don’t want.
 
  ```
  public interface CommandExecutor {
  
  	public void runCommand(String cmd) throws Exception;
  }
  
  public class CommandExecutorImpl implements CommandExecutor {
  
  	@Override
  	public void runCommand(String cmd) throws IOException {
                  //some heavy implementation
  		Runtime.getRuntime().exec(cmd);
  		System.out.println("'" + cmd + "' command executed.");
  	}
  
  }
  
  public class CommandExecutorProxy implements CommandExecutor {
  
  	private boolean isAdmin;
  	private CommandExecutor executor;
  	
  	public CommandExecutorProxy(String user, String pwd){
  		if("Pankaj".equals(user) && "J@urnalD$v".equals(pwd)) isAdmin=true;
  		executor = new CommandExecutorImpl();
  	}
  	
  	@Override
  	public void runCommand(String cmd) throws Exception {
  		if(isAdmin){
  			executor.runCommand(cmd);
  		}else{
  			if(cmd.trim().startsWith("rm")){
  				throw new Exception("rm command is not allowed for non-admin users.");
  			}else{
  				executor.runCommand(cmd);
  			}
  		}
  	}
  
  }
  
    CommandExecutor executor = new CommandExecutorProxy("Pankaj", "wrong_pwd");
    try {
      executor.runCommand("ls -ltr");
      executor.runCommand(" rm -rf abc.pdf");
    } catch (Exception e) {
      System.out.println("Exception Message::"+e.getMessage());
    }
    
  ```
  - Proxy design pattern common uses are to control access or to provide a wrapper implementation for better performance.

## Behavioral Pattern:
Behavioral design patterns dictate the interaction of classes and objects amongst eachother and the delegation of responsibility.

#### Chain of Responsible Pattern:
  - Chain of responsibility pattern is used to achieve loose coupling in software design where a request from client is passed to a chain of objects to process them. 
    Then the object in the chain will decide themselves who will be processing the request and whether the request is required to be sent to the next object in the chain or not.
  - We know that we can have multiple catch blocks in a try-catch block code. Here every catch block is kind of a processor to process that particular exception.
    So when any exception occurs in the try block, its send to the first catch block to process. If the catch block is not able to process it, it forwards the request to next object in chain i.e next catch block. If even the last catch block is not able to process it, the exception is thrown outside of the chain to the calling program.
  - One of the great example of Chain of Responsibility pattern is ATM Dispense machine. The user enters the amount to be dispensed and the machine dispense amount in terms of defined currency bills such as 50$, 20$, 10$ etc
  ```
  public class Currency {
  
  	private int amount;
  	
  	public Currency(int amt){
  		this.amount=amt;
  	}
  	
  	public int getAmount(){
  		return this.amount;
  	}
  }
  
  public interface DispenseChain {
  
  	void setNextChain(DispenseChain nextChain);
  	
  	void dispense(Currency cur);
  }
  
  public class Dollar50Dispenser implements DispenseChain {
  
  	private DispenseChain chain;
  	
  	@Override
  	public void setNextChain(DispenseChain nextChain) {
  		this.chain=nextChain;
  	}
  
  	@Override
  	public void dispense(Currency cur) {
  		if(cur.getAmount() >= 50){
  			int num = cur.getAmount()/50;
  			int remainder = cur.getAmount() % 50;
  			System.out.println("Dispensing "+num+" 50$ note");
  			if(remainder !=0) this.chain.dispense(new Currency(remainder));
  		}else{
  			this.chain.dispense(cur);
  		}
  	}
  
  }
  
  public class Dollar20Dispenser implements DispenseChain{
  
  	private DispenseChain chain;
  	
  	@Override
  	public void setNextChain(DispenseChain nextChain) {
  		this.chain=nextChain;
  	}
  
  	@Override
  	public void dispense(Currency cur) {
  		if(cur.getAmount() >= 20){
  			int num = cur.getAmount()/20;
  			int remainder = cur.getAmount() % 20;
  			System.out.println("Dispensing "+num+" 20$ note");
  			if(remainder !=0) this.chain.dispense(new Currency(remainder));
  		}else{
  			this.chain.dispense(cur);
  		}
  	}
  
  }
  
  public class Dollar10Dispenser implements DispenseChain {
  
  	private DispenseChain chain;
  	
  	@Override
  	public void setNextChain(DispenseChain nextChain) {
  		this.chain=nextChain;
  	}
  
  	@Override
  	public void dispense(Currency cur) {
  		if(cur.getAmount() >= 10){
  			int num = cur.getAmount()/10;
  			int remainder = cur.getAmount() % 10;
  			System.out.println("Dispensing "+num+" 10$ note");
  			if(remainder !=0) this.chain.dispense(new Currency(remainder));
  		}else{
  			this.chain.dispense(cur);
  		}
  	}
  
  }
  
  public class ATMDispenseChain {
  
  	private DispenseChain c1;
  
  	public ATMDispenseChain() {
  		// initialize the chain
  		this.c1 = new Dollar50Dispenser();
  		DispenseChain c2 = new Dollar20Dispenser();
  		DispenseChain c3 = new Dollar10Dispenser();
  
  		// set the chain of responsibility
  		c1.setNextChain(c2);
  		c2.setNextChain(c3);
  	}
    main() {
  	ATMDispenseChain atmDispenser = new ATMDispenseChain();
    while (true) {
      int amount = 0;
      System.out.println("Enter amount to dispense");
      Scanner input = new Scanner(System.in);
      amount = input.nextInt();
      if (amount % 10 != 0) {
        System.out.println("Amount should be in multiple of 10s.");
        return;
      }
      // process the request
      atmDispenser.c1.dispense(new Currency(amount));
    }
  	}
  	}
  
  ```
  - here important point to note here is the implementation of dispense method. You will notice that every implementation is trying to process the request and based on the amount, it might process some or full part of it.
  - Each object in the chain will have it’s own implementation to process the request, either full or partial or to send it to the next object in the chain.
  - Every object in the chain should have reference to the next object in chain to forward the request to, its achieved
  - Chain of Responsibility design pattern is good to achieve lose coupling but it comes with the trade-off of having a lot of implementation classes and maintenance problems if most of the code is common in all the implementations.
  

#### Observer Pattern:
  - Observer design pattern is useful when you are interested in the state of an object and want to get notified whenever there is any change
  - Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.
  - Observer design pattern is also called as publish-subscribe pattern
  ```
  public interface Subject {
  
  	//methods to register and unregister observers
  	public void register(Observer obj);
  	public void unregister(Observer obj);
  	
  	//method to notify observers of change
  	public void notifyObservers();
  	
  	//method to get updates from subject
  	public Object getUpdate(Observer obj);
  	
  }
  
  public interface Observer {
  	
  	//method to update the observer, used by subject
  	public void update();
  	
  	//attach with subject to observe
  	public void setSubject(Subject sub);
  }
  
  public class MyTopic implements Subject {
  
  	private List<Observer> observers;
  	private String message;
  	private boolean changed;
  	private final Object MUTEX= new Object();
  	
  	public MyTopic(){
  		this.observers=new ArrayList<>();
  	}
  	@Override
  	public void register(Observer obj) {
  		if(obj == null) throw new NullPointerException("Null Observer");
  		synchronized (MUTEX) {
  		if(!observers.contains(obj)) observers.add(obj);
  		}
  	}
  
  	@Override
  	public void unregister(Observer obj) {
  		synchronized (MUTEX) {
  		observers.remove(obj);
  		}
  	}
  
  	@Override
  	public void notifyObservers() {
  		List<Observer> observersLocal = null;
  		//synchronization is used to make sure any observer registered after message is received is not notified
  		synchronized (MUTEX) {
  			if (!changed)
  				return;
  			observersLocal = new ArrayList<>(this.observers);
  			this.changed=false;
  		}
  		for (Observer obj : observersLocal) {
  			obj.update();
  		}
  
  	}
  
  	@Override
  	public Object getUpdate(Observer obj) {
  		return this.message;
  	}
  	
  	//method to post message to the topic
  	public void postMessage(String msg){
  		System.out.println("Message Posted to Topic:"+msg);
  		this.message=msg;
  		this.changed=true;
  		notifyObservers();
  	}
  
  }
 
  public class MyTopicSubscriber implements Observer {
  	
  	private String name;
  	private Subject topic;
  	
  	public MyTopicSubscriber(String nm){
  		this.name=nm;
  	}
  	@Override
  	public void update() {
  		String msg = (String) topic.getUpdate(this);
  		if(msg == null){
  			System.out.println(name+":: No new message");
  		}else
  		System.out.println(name+":: Consuming message::"+msg);
  	}
  
  	@Override
  	public void setSubject(Subject sub) {
  		this.topic=sub;
  	}
  
  }
  
  //create subject
  MyTopic topic = new MyTopic();
  
  //create observers
  Observer obj1 = new MyTopicSubscriber("Obj1");
  Observer obj2 = new MyTopicSubscriber("Obj2");
  Observer obj3 = new MyTopicSubscriber("Obj3");
  
  //register observers to the subject
  topic.register(obj1);
  topic.register(obj2);
  topic.register(obj3);
  
  //attach observer to subject
  obj1.setSubject(topic);
  obj2.setSubject(topic);
  obj3.setSubject(topic);
  
  //check if any update is available
  obj1.update();
  
  //now send message to subject
  topic.postMessage("New Message");
  
  ```
  - Java Message Service (JMS) uses Observer design pattern along with Mediator pattern to allow applications to subscribe and publish data to other applications.
  - Model-View-Controller (MVC) frameworks also use Observer pattern where Model is the Subject and Views are observers that can register to get notified of any change to the model.

#### Interpreter Pattern:
  - The best example of interpreter design pattern is java compiler that interprets the java source code into byte code that is understandable by JVM. Google Translator is also an example of interpreter pattern where the input can be in any language and we can get the output interpreted in another language.
  - To implement interpreter pattern, we need to create Interpreter context engine that will do the interpretation work. Then we need to create different Expression implementations 
    that will consume the functionalities provided by the interpreter context. Finally we need to create the client that will take the input from user and decide which Expression to use and then generate output for the user.
  - example where the user input will be of two forms – “<Number> in Binary” or “<Number> in Hexadecimal.” Our interpreter client should return it in format “<Number> in Binary= <Number_Binary_String>” and “<Number> in Hexadecimal= <Number_Binary_String>” respectively.
  ```
  public class InterpreterContext {
  
  	public String getBinaryFormat(int i){
  		return Integer.toBinaryString(i);
  	}
  	
  	public String getHexadecimalFormat(int i){
  		return Integer.toHexString(i);
  	}
  }
  
  public interface Expression {
  
  	String interpret(InterpreterContext ic);
  }
  
  public class IntToBinaryExpression implements Expression {
  
  	private int i;
  	
  	public IntToBinaryExpression(int c){
  		this.i=c;
  	}
  	@Override
  	public String interpret(InterpreterContext ic) {
  		return ic.getBinaryFormat(this.i);
  	}
  
  }
  public class IntToHexExpression implements Expression {
  
  	private int i;
  	
  	public IntToHexExpression(int c){
  		this.i=c;
  	}
  	
  	@Override
  	public String interpret(InterpreterContext ic) {
  		return ic.getHexadecimalFormat(i);
  	}
  
  }
  
  public class InterpreterClient {
  
  	public InterpreterContext ic;
  	
  	public InterpreterClient(InterpreterContext i){
  		this.ic=i;
  	}
  	
  	public String interpret(String str){
  		Expression exp = null;
  		//create rules for expressions
  		if(str.contains("Hexadecimal")){
  			exp=new IntToHexExpression(Integer.parseInt(str.substring(0,str.indexOf(" "))));
  		}else if(str.contains("Binary")){
  			exp=new IntToBinaryExpression(Integer.parseInt(str.substring(0,str.indexOf(" "))));
  		}else return str;
  		
  		return exp.interpret(ic);
  	}
  	
  	public static void main(String args[]){
  		String str1 = "28 in Binary";
  		String str2 = "28 in Hexadecimal";
  		
  		InterpreterClient ec = new InterpreterClient(new InterpreterContext());
  		System.out.println(str1+"= "+ec.interpret(str1));
  		System.out.println(str2+"= "+ec.interpret(str2));
  
  	}
  }
  ```
  - Interpreter pattern can be used when we can create a syntax tree for the grammar we have.
  - Interpreter design pattern requires a lot of error checking and a lot of expressions and code to evaluate them. 
    It gets complicated when the grammar becomes more complicated and hence hard to maintain and provide efficiency.

#### Command Pattern:
  - In command pattern, the request is send to the invoker and invoker pass it to the encapsulated command object
    Command object passes the request to the appropriate method of Receiver to perform the specific action. 
    Client program create the receiver object and then attach it to the Command. Then it creates the invoker object and attach the command object to perform an action.
    client program executes the action, it’s processed based on the command and receiver object.
    
  ```
  public interface FileSystemReceiver {
  
  	void openFile();
  	void writeFile();
  	void closeFile();
  }
  
  public class UnixFileSystemReceiver implements FileSystemReceiver {
  
  	@Override
  	public void openFile() {
  		System.out.println("Opening file in unix OS");
  	}
  
  	@Override
  	public void writeFile() {
  		System.out.println("Writing file in unix OS");
  	}
  
  	@Override
  	public void closeFile() {
  		System.out.println("Closing file in unix OS");
  	}
  
  }
  
  public class WindowsFileSystemReceiver implements FileSystemReceiver {
  
  	@Override
  	public void openFile() {
  		System.out.println("Opening file in Windows OS");
  		
  	}
  
  	@Override
  	public void writeFile() {
  		System.out.println("Writing file in Windows OS");
  	}
  
  	@Override
  	public void closeFile() {
  		System.out.println("Closing file in Windows OS");
  	}
  
  }
  
  public interface Command {
  
  	void execute();
  }
  
  public class OpenFileCommand implements Command {
  
  	private FileSystemReceiver fileSystem;
  	
  	public OpenFileCommand(FileSystemReceiver fs){
  		this.fileSystem=fs;
  	}
  	@Override
  	public void execute() {
  		//open command is forwarding request to openFile method
  		this.fileSystem.openFile();
  	}
  
  }
  
  public class CloseFileCommand implements Command {
  
  	private FileSystemReceiver fileSystem;
  	
  	public CloseFileCommand(FileSystemReceiver fs){
  		this.fileSystem=fs;
  	}
  	@Override
  	public void execute() {
  		this.fileSystem.closeFile();
  	}
  
  }
  
  
  public class WriteFileCommand implements Command {
  
  	private FileSystemReceiver fileSystem;
  	
  	public WriteFileCommand(FileSystemReceiver fs){
  		this.fileSystem=fs;
  	}
  	@Override
  	public void execute() {
  		this.fileSystem.writeFile();
  	}
  
  }
  
  public class FileInvoker {
  
  	public Command command;
  	
  	public FileInvoker(Command c){
  		this.command=c;
  	}
  	
  	public void execute(){
  		this.command.execute();
  	}
  }
  
  public class FileSystemReceiverUtil {
  	
  	public static FileSystemReceiver getUnderlyingFileSystem(){
  		 String osName = System.getProperty("os.name");
  		 System.out.println("Underlying OS is:"+osName);
  		 if(osName.contains("Windows")){
  			 return new WindowsFileSystemReceiver();
  		 }else{
  			 return new UnixFileSystemReceiver();
  		 }
  	}
  	
  }
  
  //Creating the receiver object
  FileSystemReceiver fs = FileSystemReceiverUtil.getUnderlyingFileSystem();
  
  //creating command and associating with receiver
  OpenFileCommand openFileCommand = new OpenFileCommand(fs);
  
  //Creating invoker and associating with Command
  FileInvoker file = new FileInvoker(openFileCommand);
  
  //perform action on invoker object
  file.execute();
  
  WriteFileCommand writeFileCommand = new WriteFileCommand(fs);
  file = new FileInvoker(writeFileCommand);
  file.execute();
  
  CloseFileCommand closeFileCommand = new CloseFileCommand(fs);
  file = new FileInvoker(closeFileCommand);
  file.execute();
  
  ```
  - Command is the core of command design pattern that defines the contract for implementation. Receiver implementation is separate from command implementation.
  - Command design pattern is easily extendible, we can add new action methods in receivers and create new Command implementations without changing the client code.
  - The drawback with Command design pattern is that the code gets huge and confusing with high number of action methods and because of so many associations.
  
#### Iterator Pattern:
  - Provides a way to access the elements of an aggregate object without exposing its underlying represenation.
  - Iterator pattern is not only about traversing through a collection, we can provide different kind of iterators based on our requirements.
  - hides the actual implementation of traversal through the collection and client programs just use iterator methods.
  -  Suppose we have a list of Radio channels and the client program want to traverse through them one by one or based on the type of channel. For example some client programs are only interested in English channels and want to process only them, they don’t want to process other types of channels.
  
  ```
  public enum ChannelTypeEnum {
  
  	ENGLISH, HINDI, FRENCH, ALL;
  }
  
  public class Channel {
  
  	private double frequency;
  	private ChannelTypeEnum TYPE;
  	
  	public Channel(double freq, ChannelTypeEnum type){
  		this.frequency=freq;
  		this.TYPE=type;
  	}
  
  	public double getFrequency() {
  		return frequency;
  	}
  
  	public ChannelTypeEnum getTYPE() {
  		return TYPE;
  	}
  	
  	@Override
  	public String toString(){
  		return "Frequency="+this.frequency+", Type="+this.TYPE;
  	}
  	
  }
  
  public interface ChannelCollection {
  
  	public void addChannel(Channel c);
  	
  	public void removeChannel(Channel c);
  	
  	public ChannelIterator iterator(ChannelTypeEnum type);
  	
  }
  
  public interface ChannelIterator {
  
  	public boolean hasNext();
  	
  	public Channel next();
  }
  
  public class ChannelCollectionImpl implements ChannelCollection {
  
  	private List<Channel> channelsList;
  
  	public ChannelCollectionImpl() {
  		channelsList = new ArrayList<>();
  	}
  
  	public void addChannel(Channel c) {
  		this.channelsList.add(c);
  	}
  
  	public void removeChannel(Channel c) {
  		this.channelsList.remove(c);
  	}
  
  	@Override
  	public ChannelIterator iterator(ChannelTypeEnum type) {
  		return new ChannelIteratorImpl(type, this.channelsList);
  	}
  
  	private class ChannelIteratorImpl implements ChannelIterator {
  
  		private ChannelTypeEnum type;
  		private List<Channel> channels;
  		private int position;
  
  		public ChannelIteratorImpl(ChannelTypeEnum ty,
  				List<Channel> channelsList) {
  			this.type = ty;
  			this.channels = channelsList;
  		}
  
  		@Override
  		public boolean hasNext() {
  			while (position < channels.size()) {
  				Channel c = channels.get(position);
  				if (c.getTYPE().equals(type) || type.equals(ChannelTypeEnum.ALL)) {
  					return true;
  				} else
  					position++;
  			}
  			return false;
  		}
  
  		@Override
  		public Channel next() {
  			Channel c = channels.get(position);
  			position++;
  			return c;
  		}
  
  	}
  }
  
  ChannelCollection channels = populateChannels();
  ChannelIterator baseIterator = channels.iterator(ChannelTypeEnum.ALL);
  while (baseIterator.hasNext()) {
    Channel c = baseIterator.next();
    System.out.println(c.toString());
  }
  
  // Channel Type Iterator
  ChannelIterator englishIterator = channels.iterator(ChannelTypeEnum.ENGLISH);
  while (englishIterator.hasNext()) {
    Channel c = englishIterator.next();
    System.out.println(c.toString());
  }
  ```
  - Iterator pattern is useful when you want to provide a standard way to iterate over a collection and hide the implementation logic from client program.
  - The logic for iteration is embedded in the collection itself and it helps client program to iterate over them easily.
  
#### Mediator Pattern:
  - used to provide a centralized communication medium between different objects in a system. 
  - Allows loose coupling by encapsulating the way disparate sets of objects interact and communicate with each other. Allows for the actions of each object set to vary independently of one another.
  - Mediator pattern focuses on provide a mediator between objects for communication and help in implementing lose-coupling between objects.
  - Air traffic controller is a great example of mediator pattern where the airport control room works as a mediator for communication between different flights. Mediator works as a router between objects and it can have it’s own logic to provide way of communication.
  
  ```
  public interface ChatMediator {
  
  	public void sendMessage(String msg, User user);
  
  	void addUser(User user);
  }
  
  public abstract class User {
  	protected ChatMediator mediator;
  	protected String name;
  	
  	public User(ChatMediator med, String name){
  		this.mediator=med;
  		this.name=name;
  	}
  	
  	public abstract void send(String msg);
  	
  	public abstract void receive(String msg);
  }
  
  public class ChatMediatorImpl implements ChatMediator {
  
  	private List<User> users;
  	
  	public ChatMediatorImpl(){
  		this.users=new ArrayList<>();
  	}
  	
  	@Override
  	public void addUser(User user){
  		this.users.add(user);
  	}
  	
  	@Override
  	public void sendMessage(String msg, User user) {
  		for(User u : this.users){
  			//message should not be received by the user sending it
  			if(u != user){
  				u.receive(msg);
  			}
  		}
  	}
  
  }
  
  public class UserImpl extends User {
  
  	public UserImpl(ChatMediator med, String name) {
  		super(med, name);
  	}
  
  	@Override
  	public void send(String msg){
  		System.out.println(this.name+": Sending Message="+msg);
  		mediator.sendMessage(msg, this);
  	}
  	
  	@Override
  	public void receive(String msg) {
  		System.out.println(this.name+": Received Message:"+msg);
  	}
  
  }
  
  
  ChatMediator mediator = new ChatMediatorImpl();
  User user1 = new UserImpl(mediator, "Pankaj");
  User user2 = new UserImpl(mediator, "Lisa");
  User user3 = new UserImpl(mediator, "Saurabh");
  User user4 = new UserImpl(mediator, "David");
  mediator.addUser(user1);
  mediator.addUser(user2);
  mediator.addUser(user3);
  mediator.addUser(user4);
  		
  user1.send("Hi All");
  		
  
  ```
  - Mediator pattern is useful when the communication logic between objects is complex, we can have a central point of communication that takes care of communication logic.
  - Java Message Service (JMS) uses Mediator pattern along with Observer pattern to allow applications to subscribe and publish data to other applications.
  - We should not use mediator pattern just to achieve lose-coupling because if the number of mediators will grow, then it will become hard to maintain them.

#### State Pattern:
  -  State design pattern is used when an Object change its behavior based on its internal state. f we have to change the behavior of an object based on its state, 
     we can have a state variable in the Object. Then use if-else condition block to perform different actions based on the state. State design pattern 
     is used to provide a systematic and loosely coupled way to achieve this through Context and State implementations.
  - State Pattern Context is the class that has a State reference to one of the concrete implementations of the State. Context forwards the request to the state object for processing.
  - Suppose we want to implement a TV Remote with a simple button to perform action. If the State is ON, it will turn on the TV and if state is OFF, it will turn off the TV.
  ```
  public interface State {
  
  	public void doAction();
  }
  
  public class TVStartState implements State {
  
  	@Override
  	public void doAction() {
  		System.out.println("TV is turned ON");
  	}
  
  }
  public class TVStopState implements State {
  
  	@Override
  	public void doAction() {
  		System.out.println("TV is turned OFF");
  	}
  
  }
  
  public class TVContext implements State {
  
  	private State tvState;
  
  	public void setState(State state) {
  		this.tvState=state;
  	}
  
  	public State getState() {
  		return this.tvState;
  	}
  
  	@Override
  	public void doAction() {
  		this.tvState.doAction();
  	}
  
  }
  
  
  TVContext context = new TVContext();
  State tvStartState = new TVStartState();
  State tvStopState = new TVStopState();
  
  context.setState(tvStartState);
  context.doAction();
  
  
  context.setState(tvStopState);
  context.doAction();
  ```
  
  - The benefits of using State pattern to implement polymorphic behavior is clearly visible. The chances of error are less and it’s very easy to add more states for additional behavior. Thus making our code more robust, easily maintainable and flexible. Also State pattern helped in avoiding if-else or switch-case conditional logic in this scenario.

#### Strategy Pattern:
  - Strategy pattern is also known as Policy Pattern. We define multiple algorithms and let client application pass the algorithm to be used as a parameter.
  - Collections.sort() method that takes Comparator parameter. Based on the different implementations of Comparator interfaces, the Objects are getting sorted in different ways.
  
  ```
  public interface PaymentStrategy {
  
  	public void pay(int amount);
  }
  
  public class CreditCardStrategy implements PaymentStrategy {
  
  	private String name;
  	private String cardNumber;
  	private String cvv;
  	private String dateOfExpiry;
  	
  	public CreditCardStrategy(String nm, String ccNum, String cvv, String expiryDate){
  		this.name=nm;
  		this.cardNumber=ccNum;
  		this.cvv=cvv;
  		this.dateOfExpiry=expiryDate;
  	}
  	@Override
  	public void pay(int amount) {
  		System.out.println(amount +" paid with credit/debit card");
  	}
  
  }
  
  public class PaypalStrategy implements PaymentStrategy {
  
  	private String emailId;
  	private String password;
  	
  	public PaypalStrategy(String email, String pwd){
  		this.emailId=email;
  		this.password=pwd;
  	}
  	
  	@Override
  	public void pay(int amount) {
  		System.out.println(amount + " paid using Paypal.");
  	}
  
  }
  
  public class Item {
  
  	private String upcCode;
  	private int price;
  	
  	public Item(String upc, int cost){
  		this.upcCode=upc;
  		this.price=cost;
  	}
  
  	public String getUpcCode() {
  		return upcCode;
  	}
  
  	public int getPrice() {
  		return price;
  	}
  	
  }
  
  public class ShoppingCart {
  
  	//List of items
  	List<Item> items;
  	
  	public ShoppingCart(){
  		this.items=new ArrayList<Item>();
  	}
  	
  	public void addItem(Item item){
  		this.items.add(item);
  	}
  	
  	public void removeItem(Item item){
  		this.items.remove(item);
  	}
  	
  	public int calculateTotal(){
  		int sum = 0;
  		for(Item item : items){
  			sum += item.getPrice();
  		}
  		return sum;
  	}
  	
  	public void pay(PaymentStrategy paymentMethod){
  		int amount = calculateTotal();
  		paymentMethod.pay(amount);
  	}
  }
  
  Notice that payment method of shopping cart requires payment algorithm as argument and doesn’t store it anywhere as instance variable.
  ShoppingCart cart = new ShoppingCart();
  		
  Item item1 = new Item("1234",10);
  Item item2 = new Item("5678",40);
  
  cart.addItem(item1);
  cart.addItem(item2);
  
  //pay by paypal
  cart.pay(new PaypalStrategy("myemail@example.com", "mypwd"));
  
  //pay by credit card
  cart.pay(new CreditCardStrategy("Pankaj Kumar", "1234567890123456", "786", "12/15"));

  ```
  - Strategy Pattern is very similar to State Pattern. One of the difference is that Context contains state as instance variable and there can
    be multiple tasks whose implementation can be dependent on the state whereas in strategy pattern strategy is passed as argument to the method and context object doesn’t have any variable to store it.
  - Strategy pattern is useful when we have multiple algorithms for specific task and we want our application to be flexible to chose any of the algorithm at runtime for specific task.
  
#### Template Pattern:
  - Template Method design pattern is used to create a method stub and deferring some of the steps of implementation to the subclasses.
  - Template method defines the steps to execute an algorithm and it can provide default implementation that might be common for all or some of the subclasses.
  - example, suppose we want to provide an algorithm to build a house. The steps need to be performed to build a house are – building foundation,
    building pillars, building walls and windows. The important point is that the we can’t change the order of execution because we can’t build windows before building the foundation. So in this case we can create a template method that will use different methods to build the house.
  ```
  public abstract class HouseTemplate {
  
  	//template method, final so subclasses can't override
  	public final void buildHouse(){
  		buildFoundation();
  		buildPillars();
  		buildWalls();
  		buildWindows();
  		System.out.println("House is built.");
  	}
  
  	//default implementation
  	private void buildWindows() {
  		System.out.println("Building Glass Windows");
  	}
  
  	//methods to be implemented by subclasses
  	public abstract void buildWalls();
  	public abstract void buildPillars();
  
  	private void buildFoundation() {
  		System.out.println("Building foundation with cement,iron rods and sand");
  	}
  }
  
  public class WoodenHouse extends HouseTemplate {
  
  	@Override
  	public void buildWalls() {
  		System.out.println("Building Wooden Walls");
  	}
  
  	@Override
  	public void buildPillars() {
  		System.out.println("Building Pillars with Wood coating");
  	}
  
  }
  
  public class GlassHouse extends HouseTemplate {
  
  	@Override
  	public void buildWalls() {
  		System.out.println("Building Glass Walls");
  	}
  
  	@Override
  	public void buildPillars() {
  		System.out.println("Building Pillars with glass coating");
  	}
  
  }
  
  HouseTemplate houseType = new WoodenHouse();
  
  //using template method
  houseType.buildHouse();
  System.out.println("************");
  
  houseType = new GlassHouse();
  
  houseType.buildHouse();
  ```
  - Template method should consists of certain steps whose order is fixed and for some of the methods, implementation differs from base class to subclass. Template method should be final.
  - Most of the times, subclasses calls methods from super class but in template pattern, superclass template method calls methods from subclasses, this is known as Hollywood Principle – “don’t call us, we’ll call you.
  - Methods in base class with default implementation are referred as Hooks and they are intended to be overridden by subclasses, if you want some of the methods to be not overridden, you can make them final, 
    for example in our case we can make buildFoundation() method final because if we don’t want subclasses to override it.

#### Visitor Pattern:
  - Visitor pattern is used when we have to perform an operation on a group of similar kind of Objects. With the help of visitor pattern, we can move the operational logic from the objects to another class.
  -  example, think of a Shopping cart where we can add different type of items (Elements). When we click on checkout button, it calculates the total amount to be paid. 
     Now we can have the calculation logic in item classes or we can move out this logic to another class using visitor pattern.
  ```
  public interface ItemElement {
  
  	public int accept(ShoppingCartVisitor visitor);
  }
  
  public class Book implements ItemElement {
  
  	private int price;
  	private String isbnNumber;
  	
  	public Book(int cost, String isbn){
  		this.price=cost;
  		this.isbnNumber=isbn;
  	}
  	
  	public int getPrice() {
  		return price;
  	}
  
  	public String getIsbnNumber() {
  		return isbnNumber;
  	}
  
  	@Override
  	public int accept(ShoppingCartVisitor visitor) {
  		return visitor.visit(this);
  	}
  
  }
  
  public class Fruit implements ItemElement {
  	
  	private int pricePerKg;
  	private int weight;
  	private String name;
  	
  	public Fruit(int priceKg, int wt, String nm){
  		this.pricePerKg=priceKg;
  		this.weight=wt;
  		this.name = nm;
  	}
  	
  	public int getPricePerKg() {
  		return pricePerKg;
  	}
  
  
  	public int getWeight() {
  		return weight;
  	}
  
  	public String getName(){
  		return this.name;
  	}
  	
  	@Override
  	public int accept(ShoppingCartVisitor visitor) {
  		return visitor.visit(this);
  	}
  
  }
  
  public interface ShoppingCartVisitor {
  
  	int visit(Book book);
  	int visit(Fruit fruit);
  }
  
  public class ShoppingCartVisitorImpl implements ShoppingCartVisitor {
  
  	@Override
  	public int visit(Book book) {
  		int cost=0;
  		//apply 5$ discount if book price is greater than 50
  		if(book.getPrice() > 50){
  			cost = book.getPrice()-5;
  		}else cost = book.getPrice();
  		System.out.println("Book ISBN::"+book.getIsbnNumber() + " cost ="+cost);
  		return cost;
  	}
  
  	@Override
  	public int visit(Fruit fruit) {
  		int cost = fruit.getPricePerKg()*fruit.getWeight();
  		System.out.println(fruit.getName() + " cost = "+cost);
  		return cost;
  	}
  
  }
  
  public class ShoppingCartClient {
  
  	public static void main(String[] args) {
  		ItemElement[] items = new ItemElement[]{new Book(20, "1234"),new Book(100, "5678"),
  				new Fruit(10, 2, "Banana"), new Fruit(5, 5, "Apple")};
  		
  		int total = calculatePrice(items);
  		System.out.println("Total Cost = "+total);
  	}
  
  	private static int calculatePrice(ItemElement[] items) {
  		ShoppingCartVisitor visitor = new ShoppingCartVisitorImpl();
  		int sum=0;
  		for(ItemElement item : items){
  			sum = sum + item.accept(visitor);
  		}
  		return sum;
  	}
  
  }
  
  ```
  - The drawback of visitor pattern is that we should know the return type of visit() methods at the time of designing otherwise we will have to change the interface and all of its implementations. 
    Another drawback is that if there are too many implementations of visitor interface, it makes it hard to extend.
  - The benefit of this pattern is that if the logic of operation changes, then we need to make change only in the visitor implementation rather than doing it in all the item classes.
  - Another benefit is that adding a new item to the system is easy, it will require change only in visitor interface and implementation and existing item classes will not be affected.
  







